package funlearn.endpoints

import sttp.tapir.*
import sttp.tapir.files.*
import sttp.model.*

import funlearn.model.{ Deck, CardType }
import funlearn.html
import funlearn.service
import funlearn.endpoints.Headers


val editCardTypeEndpoint = endpoint
  .get.in("card_types" / path[Long] / "edit").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))

val serverEditCardTypeEndpoint = editCardTypeEndpoint
  .handleSuccess:
    (cardTypeId: Long) =>
      val cardType = service.cardTypes.getCardTypeById(cardTypeId)
      val body = html.upsertCardType(Some(cardType)).toString
      (body, s"/card_types/$cardTypeId/edit")

val serverUpdateCardTypeEndpoint = endpoint
  .put.in("card_types").in(formBody[Seq[(String, String)]])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (body: Seq[(String, String)]) =>
      val id = body.find(_._1 == "id").map(_._2).get.toLong
      val name = body.find(_._1 == "name").map(_._2).get
      val frontTml = body.find(_._1 == "front_tml").map(_._2).get
      val backTml = body.find(_._1 == "back_tml").map(_._2).get

      val originalCardType = service.cardTypes.getCardTypeById(id)
      val newCardType = originalCardType.copy(name = name, frontTml = frontTml, backTml = backTml)
      service.cardTypes.updateCardType(newCardType)
      s"/card_types/"

val cardTypeDetailEndpoint = endpoint
  .get.in("card_types" / path[Long]).out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))

val newCardTypeEndpoint = endpoint
  .get.in("card_types" / "new").in(query[Long]("deck")).out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))

val createCardTypeEndpoint = endpoint
  .post.in("card_types").in(formBody[Seq[(String, String)]])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))

val deleteCardTypeEndpoint = endpoint
  .delete.in("card_types" / path[Long])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))

val serverCardTypeDetailEndpoint = cardTypeDetailEndpoint
  .handleSuccess:
    (cardTypeId: Long) =>
      val cardType = service.cardTypes.getCardTypeById(cardTypeId)
      val body = html.cardTypeDetail(cardType).toString
      (body, s"/card_types/$cardTypeId")

val serverNewCardTypeEndpoint = newCardTypeEndpoint
  .handleSuccess:
    (deckId: Long) =>
      val body = html.upsertCardType(None, Some(deckId)).toString
      (body, s"/card_types/new?deck=$deckId")

val serverCreateCardTypeEndpoint = createCardTypeEndpoint
  .handleSuccess:
    (body: Seq[(String, String)]) =>
      val name = body.find(_._1 == "name").map(_._2).get
      val deckId = body.find(_._1 == "deck_id").map(_._2).get.toLong
      val frontTml = body.find(_._1 == "front_tml").map(_._2).get
      val backTml = body.find(_._1 == "back_tml").map(_._2).get

      val cardType = CardType(-1, name, deckId, frontTml, backTml)
      val cardTypeId = service.cardTypes.createCardType(cardType)
      s"/card_types/$cardTypeId"

val serverDeleteCardTypeEndpoint = deleteCardTypeEndpoint
  .handleSuccess:
    (cardTypeId: Long) =>
      val cardType = service.cardTypes.getCardTypeById(cardTypeId)
      service.cardTypes.deleteCardType(cardTypeId)
      s"/decks/${cardType.deckId}"

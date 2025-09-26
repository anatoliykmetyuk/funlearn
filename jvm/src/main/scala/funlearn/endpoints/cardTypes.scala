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
      s"/card_types/$id/edit"

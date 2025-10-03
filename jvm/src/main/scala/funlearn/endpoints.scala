package funlearn.endpoints

import sttp.tapir.*
import sttp.tapir.files.*
import sttp.model.*

import funlearn.model.{ Deck, CardType }
import funlearn.html
import funlearn.db
import funlearn.services.*
import funlearn.endpoints.Headers

object Headers:
  def hxPushUrl = "HX-Push-Url"

val decks_GET = endpoint
  .get.in("decks").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header(Headers.hxPushUrl, "/decks"))
  .handleSuccess:
    _ =>
      val decks = db.decks.getAllDecks()
      html.decks(decks).toString

val decks_new_GET = endpoint
  .get.in("decks" / "new").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header(Headers.hxPushUrl, "/decks/new"))
  .handleSuccess:
    _ =>
      html.newDeck().toString

val decks_POST = endpoint
  .post.in("decks").in(formBody[Seq[(String, String)]])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (body: Seq[(String, String)]) =>
      val data = IMPL_decks_POST.mkData(body)
      val cardTypeId = IMPL_decks_POST.impl(data)
      s"/card_types/$cardTypeId/edit"

val decks_id_GET = endpoint
  .get.in("decks" / path[Long]).out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (deckId: Long) =>
      val deck = db.decks.getDeckById(deckId)
      val cardTypes = db.cardTypes.getCardTypesByDeckId(deckId)
      val body = html.deckDetail(deck, cardTypes).toString
      (body, s"/decks/$deckId")

val decks_id_edit_GET = endpoint
  .get.in("decks" / path[Long] / "edit").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (deckId: Long) =>
      val deck = db.decks.getDeckById(deckId)
      val body = html.editDeck(deck).toString
      (body, s"/decks/$deckId/edit")

val decks_id_PATCH = endpoint
  .patch.in("decks" / path[Long]).in(formBody[Seq[(String, String)]])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (deckId: Long, body: Seq[(String, String)]) =>
      val name = body.find(_._1 == "name").map(_._2).get
      val description = body.find(_._1 == "description").map(_._2).get

      val originalDeck = db.decks.getDeckById(deckId)
      val updatedDeck = originalDeck.copy(name = name, description = description)
      db.decks.updateDeck(updatedDeck)
      s"/decks/$deckId"

val decks_id_DELETE = endpoint
  .delete.in("decks" / path[Long])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (deckId: Long) =>
      db.decks.deleteDeck(deckId)
      "/decks"

val card_types_id_edit_GET = endpoint
  .get.in("card_types" / path[Long] / "edit").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (cardTypeId: Long) =>
      val cardType = db.cardTypes.getCardTypeById(cardTypeId)
      val body = html.upsertCardType(Some(cardType)).toString
      (body, s"/card_types/$cardTypeId/edit")

val card_types_PUT = endpoint
  .put.in("card_types").in(formBody[Seq[(String, String)]])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (body: Seq[(String, String)]) =>
      val id = body.find(_._1 == "id").map(_._2).get.toLong
      val name = body.find(_._1 == "name").map(_._2).get
      val frontTml = body.find(_._1 == "front_tml").map(_._2).get
      val backTml = body.find(_._1 == "back_tml").map(_._2).get

      val originalCardType = db.cardTypes.getCardTypeById(id)
      val newCardType = originalCardType.copy(name = name, frontTml = frontTml, backTml = backTml)
      db.cardTypes.updateCardType(newCardType)
      s"/card_types/"

val card_types_id_GET = endpoint
  .get.in("card_types" / path[Long]).out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (cardTypeId: Long) =>
      val cardType = db.cardTypes.getCardTypeById(cardTypeId)
      val body = html.cardTypeDetail(cardType).toString
      (body, s"/card_types/$cardTypeId")

val card_types_new_GET = endpoint
  .get.in("card_types" / "new").in(query[Long]("deck")).out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (deckId: Long) =>
      val body = html.upsertCardType(None, Some(deckId)).toString
      (body, s"/card_types/new?deck=$deckId")

val card_types_POST = endpoint
  .post.in("card_types").in(formBody[Seq[(String, String)]])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (body: Seq[(String, String)]) =>
      val name = body.find(_._1 == "name").map(_._2).get
      val deckId = body.find(_._1 == "deck_id").map(_._2).get.toLong
      val frontTml = body.find(_._1 == "front_tml").map(_._2).get
      val backTml = body.find(_._1 == "back_tml").map(_._2).get

      val cardType = CardType(-1, name, deckId, frontTml, backTml)
      val cardTypeId = db.cardTypes.createCardType(cardType)
      s"/card_types/$cardTypeId"

val card_types_id_DELETE = endpoint
  .delete.in("card_types" / path[Long])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (cardTypeId: Long) =>
      val cardType = db.cardTypes.getCardTypeById(cardTypeId)
      db.cardTypes.deleteCardType(cardTypeId)
      s"/decks/${cardType.deckId}"

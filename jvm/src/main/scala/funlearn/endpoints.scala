package funlearn.endpoints

import sttp.tapir.*
import sttp.tapir.files.*
import sttp.model.*

import funlearn.model.{ Deck, CardType }
import funlearn.html
import funlearn.service
import funlearn.endpoints.Headers

object Headers:
  def hxPushUrl = "HX-Push-Url"

val GET_decks = endpoint
  .get.in("decks").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header(Headers.hxPushUrl, "/decks"))
  .handleSuccess:
    _ =>
      val decks = service.decks.getAllDecks()
      html.decks(decks).toString

val GET_decks_new = endpoint
  .get.in("decks" / "new").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header(Headers.hxPushUrl, "/decks/new"))
  .handleSuccess:
    _ =>
      html.newDeck().toString

val POST_decks = endpoint
  .post.in("decks").in(formBody[Seq[(String, String)]])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (body: Seq[(String, String)]) =>
      val name = body.find(_._1 == "name").map(_._2).get
      val description = body.find(_._1 == "description").map(_._2).get
      val labelKeyId = body.find(_._1 == "label_key").map(_._2).get
      val labelKey = body.find(_._1 == s"keys[$labelKeyId]").map(_._2).get

      val keys = body.filter(_._1.startsWith("keys")).map(_._2)
      val prompts = body.filter(_._1.startsWith("prompts")).map(_._2)
      val schemaModel = keys.zip(prompts).map { case (k, p) => Map("name" -> k, "prompt" -> p) }

      // Schema is a JSON string formed from the keys and prompts
      val schema = upickle.default.write(schemaModel)
      val deck = Deck(-1, name, description, schema, labelKey)

      val deckId = service.decks.createDeck(deck)
      println(s"Deck created, deckId: $deckId")

      // Create the first card type
      val cardType = CardType(-1, "Default", deckId, "", "")
      val cardTypeId = service.cardTypes.createCardType(cardType)
      val newCardType = service.cardTypes.getCardTypeById(cardTypeId)
      println(s"Card type created: $newCardType")

      // Return the edit page for the new card type, by redirecting the user to page /card_type/{id}/edit
      s"/card_types/$cardTypeId/edit"

val GET_decks_id = endpoint
  .get.in("decks" / path[Long]).out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (deckId: Long) =>
      val deck = service.decks.getDeckById(deckId)
      val cardTypes = service.cardTypes.getCardTypesByDeckId(deckId)
      val body = html.deckDetail(deck, cardTypes).toString
      (body, s"/decks/$deckId")

val GET_decks_id_edit = endpoint
  .get.in("decks" / path[Long] / "edit").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (deckId: Long) =>
      val deck = service.decks.getDeckById(deckId)
      val body = html.editDeck(deck).toString
      (body, s"/decks/$deckId/edit")

val PATCH_decks_id = endpoint
  .patch.in("decks" / path[Long]).in(formBody[Seq[(String, String)]])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (deckId: Long, body: Seq[(String, String)]) =>
      val name = body.find(_._1 == "name").map(_._2).get
      val description = body.find(_._1 == "description").map(_._2).get

      val originalDeck = service.decks.getDeckById(deckId)
      val updatedDeck = originalDeck.copy(name = name, description = description)
      service.decks.updateDeck(updatedDeck)
      s"/decks/$deckId"

val DELETE_decks_id = endpoint
  .delete.in("decks" / path[Long])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (deckId: Long) =>
      service.decks.deleteDeck(deckId)
      "/decks"

val GET_card_types_id_edit = endpoint
  .get.in("card_types" / path[Long] / "edit").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (cardTypeId: Long) =>
      val cardType = service.cardTypes.getCardTypeById(cardTypeId)
      val body = html.upsertCardType(Some(cardType)).toString
      (body, s"/card_types/$cardTypeId/edit")

val PUT_card_types = endpoint
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

val GET_card_types_id = endpoint
  .get.in("card_types" / path[Long]).out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (cardTypeId: Long) =>
      val cardType = service.cardTypes.getCardTypeById(cardTypeId)
      val body = html.cardTypeDetail(cardType).toString
      (body, s"/card_types/$cardTypeId")

val GET_card_types_new = endpoint
  .get.in("card_types" / "new").in(query[Long]("deck")).out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header[String](Headers.hxPushUrl))
  .handleSuccess:
    (deckId: Long) =>
      val body = html.upsertCardType(None, Some(deckId)).toString
      (body, s"/card_types/new?deck=$deckId")

val POST_card_types = endpoint
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
      val cardTypeId = service.cardTypes.createCardType(cardType)
      s"/card_types/$cardTypeId"

val DELETE_card_types_id = endpoint
  .delete.in("card_types" / path[Long])
  .out(header[String](HeaderNames.Location))
  .out(statusCode(StatusCode.SeeOther))
  .handleSuccess:
    (cardTypeId: Long) =>
      val cardType = service.cardTypes.getCardTypeById(cardTypeId)
      service.cardTypes.deleteCardType(cardTypeId)
      s"/decks/${cardType.deckId}"

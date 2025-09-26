package funlearn.endpoints

import sttp.tapir.*
import sttp.tapir.files.*
import sttp.model.*

import funlearn.model.{ Deck, CardType }
import funlearn.html
import funlearn.service
import funlearn.endpoints.Headers

val decksEndpoint = endpoint
  .get.in("decks").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header(Headers.hxPushUrl, "/decks"))

val newDecksEndoint = endpoint
  .get.in("decks" / "new").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header(Headers.hxPushUrl, "/decks/new"))

val createDeckEndpoint = endpoint
  .post.in("decks").in(formBody[Seq[(String, String)]]).out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))

val serverDeckEndpoint = decksEndpoint
  .handleSuccess:
    _ =>
      val decks = service.decks.getAllDecks()
      html.decks(decks).toString

val serverNewDeckEndpoint = newDecksEndoint
  .handleSuccess:
    _ =>
      html.newDeck().toString

val serverCreateDeckEndpoint = createDeckEndpoint
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
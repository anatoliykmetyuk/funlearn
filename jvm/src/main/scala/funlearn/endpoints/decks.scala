package funlearn.endpoints

import sttp.tapir.*
import sttp.tapir.files.*
import sttp.model.*

import funlearn.model.Deck
import funlearn.html
import funlearn.service
import funlearn.endpoints.Headers

val decksEndpoint = endpoint
  .get.in("decks").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))

val newDecksEndoint = endpoint
  .get.in("decks" / "new").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))
  .out(header(Headers.hxPushUrl, "/decks/new"))

val serverDeckEndpoint = decksEndpoint
  .handleSuccess:
    _ =>
      val decks = service.decks.getAllDecks()
      html.decks(decks).toString

val serverNewDeckEndpoint = newDecksEndoint
  .handleSuccess:
    _ =>
      html.newDeck().toString

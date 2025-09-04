package funlearn.endpoints

import sttp.tapir.*
import sttp.tapir.files.*
import sttp.model.*

import funlearn.model.Deck
import funlearn.html
import funlearn.service

val decksEndpoint = endpoint
  .get.in("decks").out(stringBody)
  .out(header(Header.contentType(MediaType.TextHtml)))

val serverDeckEndpoint = decksEndpoint
  .handleSuccess:
    _ =>
      // TODO: detect the request header and reply with a fragment or full html
      //       depending on whether the request comes from another app's page.
      val decks = service.decks.getAllDecks()
      html.decks(decks).toString

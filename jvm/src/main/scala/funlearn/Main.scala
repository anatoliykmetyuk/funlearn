import ox.*
import sttp.tapir.*
import sttp.tapir.files.*
import sttp.model.*

import sttp.tapir.server.netty.sync.NettySyncServer

import funlearn.html
import funlearn.endpoints.*
import funlearn.model.Card
import funlearn.model.Deck

object Main extends OxApp:
  override def run(args: Vector[String])(using Ox): ExitCode =
    println(s"Access current session at: http://localhost:8080/session")
    println(s"Access decks at: http://localhost:8080/decks")
    NettySyncServer()
      .port(8080)
      .addEndpoint(staticFilesGetServerEndpoint("static")("../static"))
      .addEndpoints(List(
        GET_decks,
        GET_decks_new,
        POST_decks,
        GET_decks_id,
        GET_decks_id_edit,
        PATCH_decks_id,
        DELETE_decks_id,
        GET_card_types_id_edit,
        PUT_card_types,
        GET_card_types_new,
        GET_card_types_id,
        POST_card_types,
        DELETE_card_types_id,
      ))
      .startAndWait()
    ExitCode.Success

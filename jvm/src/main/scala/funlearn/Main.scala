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
        decks_new_GET,
        decks_GET,
        decks_POST,
        decks_id_edit_GET,
        decks_id_GET,
        decks_id_PATCH,
        decks_id_DELETE,
        card_types_new_GET,
        card_types_id_edit_GET,
        card_types_id_GET,
        card_types_id_DELETE,
        card_types_PUT,
        card_types_POST,
      ))
      .startAndWait()
    ExitCode.Success

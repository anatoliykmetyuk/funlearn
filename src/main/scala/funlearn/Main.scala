import ox.*
import sttp.tapir.server.netty.sync.NettySyncServer
import sttp.tapir.*
import sttp.model.Header
import sttp.model.MediaType

import funlearn.ui
import funlearn.model.Card
import sttp.tapir.server.netty.NettyConfig
import sttp.tapir.server.netty.sync.NettySyncServerOptions

object Main extends OxApp:

  var dueCards = List(
    // Animals
    Card(front = "いぬ", back = "Dog"),
    Card(front = "ねこ", back = "Cat"),
    Card(front = "うま", back = "Horse"),

    // Foods
    Card(front = "りんご", back = "Apple"),
    Card(front = "ごはん", back = "Rice"),
    Card(front = "パン", back = "Bread"),

    // Plants
    Card(front = "はな", back = "Flower"),
    Card(front = "き", back = "Tree"),

    // Household Items
    Card(front = "テーブル", back = "Table"),
    Card(front = "いす", back = "Chair")
  )

  val indexEndpoint = endpoint
    .get.in("session").out(stringBody)
    .out(header(Header.contentType(MediaType.TextHtml)))
    .handleSuccess:
      _ => ui.session(dueCards.head, dueCards.length).toString

  val passCurrentCardEndpoint = endpoint
    .post.in("session" / "pass").out(stringBody).errorOut(stringBody)
    .out(header(Header.contentType(MediaType.TextHtml)))
    .handle:
      _ => dueCards match
        case Nil => Left("Error: No cards remaining")
        case current :: next :: xs =>
          dueCards = next :: xs
          Right(ui.session(next, dueCards.length).toString)
        case current :: Nil =>
          dueCards = Nil
          Right(ui.congratulations().toString)

  val lapseCurrentCardEndpoint = endpoint
    .post.in("session" / "lapse").out(stringBody).errorOut(stringBody)
    .out(header(Header.contentType(MediaType.TextHtml)))
    .handle:
      _ => dueCards match
        case Nil => Left("Error: No cards remaining")
        case current :: next :: xs =>
          dueCards = (next :: xs) :+ current
          Right(ui.session(next, dueCards.length).toString)
        case current :: Nil =>
          Right(ui.session(current, dueCards.length).toString)

  override def run(args: Vector[String])(using Ox): ExitCode =
    println(s"Access current session at: http://localhost:8080/session")
    NettySyncServer()
      .port(8080)
      .addEndpoints(List(
        indexEndpoint,
        passCurrentCardEndpoint,
        lapseCurrentCardEndpoint
      ))
      .startAndWait()
    ExitCode.Success

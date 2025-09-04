import ox.*
import sttp.tapir.*
import sttp.tapir.files.*
import sttp.model.*

import sttp.tapir.server.netty.sync.NettySyncServer

import funlearn.html
import funlearn.endpoints.serverDeckEndpoint
import funlearn.model.Card
import funlearn.model.Deck


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
      _ => html.session(dueCards.head, dueCards.length).toString

  val passCurrentCardEndpoint = endpoint
    .post.in("session" / "pass").out(stringBody).errorOut(stringBody)
    .out(header(Header.contentType(MediaType.TextHtml)))
    .handle:
      _ => dueCards match
        case Nil => Left("Error: No cards remaining")
        case current :: next :: xs =>
          dueCards = next :: xs
          Right(html.session(next, dueCards.length).toString)
        case current :: Nil =>
          dueCards = Nil
          Right(html.congratulations().toString)

  val lapseCurrentCardEndpoint = endpoint
    .post.in("session" / "lapse").out(stringBody).errorOut(stringBody)
    .out(header(Header.contentType(MediaType.TextHtml)))
    .handle:
      _ => dueCards match
        case Nil => Left("Error: No cards remaining")
        case current :: next :: xs =>
          dueCards = (next :: xs) :+ current
          Right(html.session(next, dueCards.length).toString)
        case current :: Nil =>
          Right(html.session(current, dueCards.length).toString)

  override def run(args: Vector[String])(using Ox): ExitCode =
    println(s"Access current session at: http://localhost:8080/session")
    println(s"Access decks at: http://localhost:8080/decks")
    NettySyncServer()
      .port(8080)
      .addEndpoint(staticFilesGetServerEndpoint("static")("static"))
      .addEndpoints(List(
        indexEndpoint,
        passCurrentCardEndpoint,
        lapseCurrentCardEndpoint,
        serverDeckEndpoint,
      ))
      .startAndWait()
    ExitCode.Success

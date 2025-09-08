package funlearn.html

import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as titleTag, attr as _, * }
import funlearn.model.Card

object hx:
  def get = attr("hx-get")
  def post = attr("hx-post")
  def target = attr("hx-target")

def layout(pageTitle: String)(payload: Frag*) =
  html(
    head(
      meta(charset := "UTF-8"),
      meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
      titleTag(s"FunLearn - $pageTitle"),
      link(rel := "stylesheet", href := "https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css"),
      script(src := "https://cdn.jsdelivr.net/npm/htmx.org@2.0.6/dist/htmx.min.js"),
      link(rel := "stylesheet", href := "/static/styles.css"),
      script(src := "/static/script.js"),
    ),
    body(`class` := "container")(
      payload
    )
  )

def session(currentCard: Card, remainingCards: Int) = layout("Session")(
  header(
    p(id := "session-status")(s"${remainingCards} cards remaining")
  ),

  main(
    article(id := "card-front", `class` := "card")(
      h1(currentCard.front)
    ),

    article(id := "card-back", `class` := "card", hidden)(
      h1(currentCard.back),
    )
  ),

  section(`class` := "grid")(
    button(
      hx.post := "/session/lapse",
      hx.target := "body"
    )("Lapse"),
    button(
      hx.post := "/session/pass",
      hx.target := "body"
    )("Pass")
  ),

  section(`class` := "")(
    button(onclick := """
      document.getElementById('card-back').hidden = false;
      this.hidden = true;
    """)("Reveal")
  )
)

def congratulations() = article(
  h1("Congratulations!"),
  p("You have completed all the cards.")
)

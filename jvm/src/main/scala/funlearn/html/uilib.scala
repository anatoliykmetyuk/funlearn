package funlearn.html

import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as titleTag, attr as _, * }

object hx:
  def get = attr("hx-get")
  def post = attr("hx-post")
  def put = attr("hx-put")
  def patch = attr("hx-patch")
  def delete = attr("hx-delete")
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
      script(src := "/static/funlearn-fastopt.js"),
    ),
    body(`class` := "container")(
      header(`class` := "container")(
        nav(
          a(
            hx.get := "/decks",
            hx.target := "body",
            `class` := "logo"
          )(img(src := "/static/logo.png", alt := "FunLearn Logo"))
        )
      ),
      h1(pageTitle),
      payload
    )
  )

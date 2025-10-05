package funlearn.html

import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

import funlearn.model.Deck


def HTML_decks_id_edit(deck: Deck) = layout(s"Edit Deck: ${deck.name}")(
  main(
    // Navigation
    section(
      a(hx.get := s"/decks/${deck.id}", hx.target := "body", `class` := "secondary")("⬅️ Back to Deck")
    ),

    form(
      hx.patch := s"/decks/${deck.id}",
      hx.target := "body"
    )(
      fieldset(
        legend("Deck Information"),

        label("Name", `for` := "name"),
        input(`type` := "text", id := "name", name := "name", required, value := deck.name),

        label("Description", `for` := "description"),
        textarea(id := "description", name := "description", rows := 3)(deck.description),
      ),

      section(`class` := "grid")(
        button(`type` := "submit", `class` := "primary")("➡️ Update Deck"),
        a(hx.get := s"/decks/${deck.id}", hx.target := "body", `class` := "secondary")("❌ Cancel")
      )
    )
  )
)
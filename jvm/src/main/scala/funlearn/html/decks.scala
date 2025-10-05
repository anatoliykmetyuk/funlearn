package funlearn.html

import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

import funlearn.model.Deck

def HTML_decks(decks: List[Deck]) = layout("Decks")(
  main(
    // Deck buttons
    section(`class` := "grid")(
      for deck <- decks yield
        article(`class` := "deck-card")(
          h3(
            a(
              hx.get := s"/decks/${deck.id}",
              hx.target := "body"
            )(deck.name)
          ),
        )
    ),

    // Add new deck button
    section(
      button(hx.get := "/decks/new", hx.target := "body", `class` := "primary")("➕ Add New Deck")
    )
  )
)


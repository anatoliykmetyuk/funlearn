package funlearn.html

import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

import funlearn.model.Deck


def decks(decks: List[Deck]) = layout(
  main(
    h1("Decks"),

    // Deck buttons
    section(`class` := "grid")(
      for deck <- decks yield
        article(`class` := "deck-card")(
          h3(deck.name),
          button(`class` := "secondary deck-edit-btn")("✏️")
        )
    ),

    // Add new deck button
    section(
      button(`class` := "primary")("➕ Add New Deck")
    )
  )
)
package funlearn.html

import funlearn.model.{Deck, CardType}
import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }


def HTML_decks_id(deck: Deck, cardTypes: Seq[CardType]) = layout(s"Deck: ${deck.name}")(
  main(
    // Navigation
    section(
      a(hx.get := "/decks", hx.target := "body", `class` := "secondary")("⬅️ Back to Decks")
    ),

    // Deck info section
    article(`class` := "deck-info")(
      h2(deck.name),
      p(deck.description),
      div(`class` := "grid")(
        button(hx.get := s"/decks/${deck.id}/edit", hx.target := "body", `class` := "secondary")("✏️ Edit Name"),
        button(hx.delete := s"/decks/${deck.id}", hx.target := "body", `class` := "contrast")("🗑️ Delete Deck")
      )
    ),

    // Card types section
    section(
      h3("Card Types"),
      div(`class` := "grid")(
        for cardType <- cardTypes yield
          article(`class` := "card-type-card")(
            h4(cardType.name),
            div(`class` := "grid")(
              button(hx.get := s"/card_types/${cardType.id}", hx.target := "body", `class` := "secondary")("👁️ View"),
              button(hx.get := s"/card_types/${cardType.id}/edit", hx.target := "body", `class` := "secondary")("✏️ Edit"),
              button(hx.delete := s"/card_types/${cardType.id}", hx.target := "body", `class` := "contrast")("🗑️ Delete")
            )
          )
      ),

      // Add new card type button
      section(
        button(hx.get := s"/card_types/new?deck=${deck.id}", hx.target := "body", `class` := "primary")("➕ Add New Card Type")
      )
    )
  )
)

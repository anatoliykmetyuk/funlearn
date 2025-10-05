package funlearn.html

import funlearn.model.CardType
import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

def HTML_cardTypes_id(cardType: CardType) = layout(s"Card Type: ${cardType.name}")(
  main(
    article(`class` := "card-type-info")(
      h2(cardType.name),
      div(`class` := "grid")(
        button(hx.get := s"/card_types/${cardType.id}/edit", hx.target := "body", `class` := "secondary")("✏️ Edit"),
        button(hx.delete := s"/card_types/${cardType.id}", hx.target := "body", `class` := "contrast")("🗑️ Delete"),
        a(hx.get := s"/decks/${cardType.deckId}", hx.target := "body", `class` := "secondary")("⬅️ Back to Deck")
      )
    ),

    section(
      h3("Front Template"),
      div(`class` := "template-preview")(raw(cardType.frontTml))
    ),

    section(
      h3("Back Template"),
      div(`class` := "template-preview")(raw(cardType.backTml))
    )
  )
)
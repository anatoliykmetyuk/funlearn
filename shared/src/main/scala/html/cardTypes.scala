package funlearn.html

import funlearn.model.CardType
import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

def upsertCardType(editCardType: Option[CardType] = None, deckId: Option[Long] = None) =
  val action = editCardType.fold(hx.post := "/card_types")(ct => hx.put := s"/card_types")

  layout(editCardType.fold("New Card Type")(ct => s"Edit Card Type: ${ct.name}"))(
    main(
      form(
        action,
        hx.target := "body"
      )(
        fieldset(
          input(`type` := "hidden", id := "id", name := "id", value := editCardType.map(_.id.toString).getOrElse("")),
          input(`type` := "hidden", id := "deck_id", name := "deck_id", value := editCardType.map(_.deckId.toString).orElse(deckId.map(_.toString)).getOrElse("")),

          label("Name", `for` := "name"),
          input(`type` := "text", id := "name", name := "name", required, value := editCardType.map(_.name).getOrElse("")),

          label("Front HTML Template", `for` := "front_tml"),
          textarea(id := "front_tml", name := "front_tml", rows := 10)(editCardType.map(_.frontTml).getOrElse("")),

          label("Back HTML Template", `for` := "back_tml"),
          textarea(id := "back_tml", name := "back_tml", rows := 10)(editCardType.map(_.backTml).getOrElse("")),
        ),

        section(
          button(`type` := "submit", `class` := "primary")(editCardType.fold("➡️ Create Card Type")(ct => "➡️ Update Card Type"))
        )
      )
    )
  )

def cardTypeDetail(cardType: CardType) = layout(s"Card Type: ${cardType.name}")(
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
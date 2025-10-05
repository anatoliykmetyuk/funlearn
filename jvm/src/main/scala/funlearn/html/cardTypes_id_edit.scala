package funlearn.html

import funlearn.model.CardType
import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

def HTML_cardTypes_id_edit(cardType: CardType) =
  val action = hx.put := s"/card_types"

  layout(s"Edit Card Type: ${cardType.name}")(
    main(
      form(
        action,
        hx.target := "body"
      )(
        fieldset(
          input(`type` := "hidden", id := "id", name := "id", value := cardType.id.toString),
          input(`type` := "hidden", id := "deck_id", name := "deck_id", value := cardType.deckId.toString),

          label("Name", `for` := "name"),
          input(`type` := "text", id := "name", name := "name", required, value := cardType.name),

          label("Front HTML Template", `for` := "front_tml"),
          textarea(id := "front_tml", name := "front_tml", rows := 10)(cardType.frontTml),

          label("Back HTML Template", `for` := "back_tml"),
          textarea(id := "back_tml", name := "back_tml", rows := 10)(cardType.backTml),
        ),

        section(
          button(`type` := "submit", `class` := "primary")("➡️ Update Card Type")
        )
      )
    )
  )


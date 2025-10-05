package funlearn.html

import funlearn.model.CardType
import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

def HTML_cardTypes_new(deckId: Long) =
  val action = hx.post := "/card_types"

  layout("New Card Type")(
    main(
      form(
        action,
        hx.target := "body"
      )(
        fieldset(
          input(`type` := "hidden", id := "deck_id", name := "deck_id", value := deckId.toString),

          label("Name", `for` := "name"),
          input(`type` := "text", id := "name", name := "name", required),

          label("Front HTML Template", `for` := "front_tml"),
          textarea(id := "front_tml", name := "front_tml", rows := 10),

          label("Back HTML Template", `for` := "back_tml"),
          textarea(id := "back_tml", name := "back_tml", rows := 10),
        ),

        section(
          button(`type` := "submit", `class` := "primary")("➡️ Create Card Type")
        )
      )
    )
  )


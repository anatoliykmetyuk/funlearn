package funlearn.html

import funlearn.model.CardType
import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

def upsertCardType(editCardType: Option[CardType] = None) =
  layout(editCardType.fold("New Card Type")(ct => s"Edit Card Type: ${ct.name}"))(
    main(
      form(
        hx.post := "/cardTypes",
        hx.target := "body"
      )(
        fieldset(
          label("Name", `for` := "name"),
          input(`type` := "text", id := "name", name := "name", required, value := editCardType.map(_.name).getOrElse("")),

          label("Front HTML Template", `for` := "front_tml"),
          textarea(id := "front_tml", name := "front_tml", rows := 10, value := editCardType.map(_.frontTml).getOrElse("")),

          label("Back HTML Template", `for` := "back_tml"),
          textarea(id := "back_tml", name := "back_tml", rows := 10, value := editCardType.map(_.backTml).getOrElse("")),
        ),

        section(
          button(`type` := "submit", `class` := "primary")(editCardType.fold("➡️ Create Card Type")(ct => "➡️ Update Card Type"))
        )
      )
    )
  )
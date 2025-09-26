package funlearn.html

import funlearn.model.CardType
import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

def upsertCardType(editCardType: Option[CardType] = None) =
  val action = editCardType.fold(hx.post := "/card_types")(ct => hx.put := s"/card_types")


  layout(editCardType.fold("New Card Type")(ct => s"Edit Card Type: ${ct.name}"))(
    main(
      form(
        action,
        hx.target := "body"
      )(
        fieldset(
          input(`type` := "hidden", id := "id", name := "id", value := editCardType.map(_.id.toString).getOrElse("")),

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
package funlearn.html

import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, tag as _, * }


def HTML_decks_new() = layout("Create New Deck")(
    main(
      form(
        hx.post := "/decks",
        hx.target := "body"
      )(
        fieldset(
          legend("Deck Information"),

          label("Name", `for` := "name"),
          input(`type` := "text", id := "name", name := "name", required),

          label("Description", `for` := "description"),
          textarea(id := "description", name := "description", rows := 3),
        ),

        fieldset(
          legend("Card Structure"),

          div(`class` := "card-type-fields")(
            tag("template")(id := s"card-type-field-template")(
              cardTypeField(0, isLabelKey = true)
            )
          ),

          button(`type` := "button", `class` := "secondary", id := "add-card-field", onclick := "addCardField()")("➕ Add Card Field"),
        ),

        section(
          button(`type` := "submit", `class` := "primary")("➡️ Create Deck")
        )
      )
    )
  )

private def cardTypeField(uid: Int, isLabelKey: Boolean = false) =
  div(`class` := "card-type-field")(
    label("Key", `for` := "key"),
    input(`type` := "text", id := s"key-$uid", name := s"keys[$uid]", required),

    label("Prompt", `for` := "prompt"),
    input(`type` := "text", id := s"prompt-$uid", name := s"prompts[$uid]", required),

    div(`class` := "grid")(
      label("Label key?", `for` := "label_key"),
      input(`type` := "radio", id := s"label_key-$uid", name := s"label_key", value := s"$uid", if (isLabelKey) checked else ()),
    ),

    button(`type` := "button", `class` := "secondary", onclick := "removeCardField(this)")("➖ Remove Card Field"),
  )

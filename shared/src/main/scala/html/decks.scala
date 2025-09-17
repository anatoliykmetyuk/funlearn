package funlearn.html

import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }

import funlearn.model.Deck


def decks(decks: List[Deck]) = layout("Decks")(
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
      button(hx.get := "/decks/new", hx.target := "body", `class` := "primary")("➕ Add New Deck")
    )
  )
)

def newDeck() = layout("Create New Deck")(
  main(
    h1("Create New Deck"),

    form(action := "/decks", method := "POST")(
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
          // Hidden template for cloning
          div(`class` := "card-type-field template", hidden)(
            label("Key", `for` := "key_template"),
            input(`type` := "text", id := "key_template", name := "keys[]", required),

            label("Prompt", `for` := "prompt_template"),
            input(`type` := "text", id := "prompt_template", name := "prompts[]", required),

            button(`type` := "button", `class` := "secondary", id := "remove-card-field")("➖ Remove Card Field"),
          )
        ),

        button(`type` := "button", `class` := "secondary", id := "add-card-field")("➕ Add Card Field"),
      ),

      section(
        button(`type` := "submit", `class` := "primary")("➡️ Create Deck")
      )
    )
  )
)
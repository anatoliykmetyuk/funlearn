package funlearn.html

import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }
import org.scalajs.dom

import funlearn.model.Deck
import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.Event


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

        div(`class` := "card-type-fields")(),

        button(`type` := "button", `class` := "secondary", id := "add-card-field", onclick := "addCardField()")("➕ Add Card Field"),
      ),

      section(
        button(`type` := "submit", `class` := "primary")("➡️ Create Deck")
      )
    )
  )
)

@JSExportTopLevel("addCardField")
def addCardField(): Unit =
  val cardTypeFields = dom.document.querySelector(".card-type-fields")
  val newField = cardTypeField()
  cardTypeFields.appendChild(newField.render)

@JSExportTopLevel("removeCardField")
def removeCardField(btn: HTMLElement): Unit =
  val cardTypeField = btn.closest(".card-type-field")
  val cardTypeFields = cardTypeField.parentNode
  cardTypeFields.removeChild(cardTypeField)

def cardTypeField() =
  import scalatags.JsDom.all._
  div(`class` := "card-type-field")(
    label("Key", `for` := "key"),
    input(`type` := "text", id := "key", name := "keys[]", required),

    label("Prompt", `for` := "prompt"),
    input(`type` := "text", id := "prompt", name := "prompts[]", required),

    button(`type` := "button", `class` := "secondary", onclick := "removeCardField(this)")("➖ Remove Card Field"),
  )
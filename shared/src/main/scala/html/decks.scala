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

var decksFieldsCounter = 0
def newDeck() =
  decksFieldsCounter = 0
  layout("Create New Deck")(
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
  val newField = cardTypeField(decksFieldsCounter, isLabelKey = cardTypeFields.children.length == 0)
  cardTypeFields.appendChild(newField.render)
  decksFieldsCounter += 1

@JSExportTopLevel("removeCardField")
def removeCardField(btn: HTMLElement): Unit =
  val cardTypeField = btn.closest(".card-type-field")
  val cardTypeFields = cardTypeField.parentNode
  cardTypeFields.removeChild(cardTypeField)

def cardTypeField(uid: Int, isLabelKey: Boolean = false) =
  import scalatags.JsDom.all._
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
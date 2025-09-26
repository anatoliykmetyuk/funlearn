package funlearn.html

import scalatags.Text.all.*
import scalatags.Text.tags2.{ title as _, attr as _, * }
import org.scalajs.dom

import funlearn.model.{Deck, CardType}
import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.Event


def decks(decks: List[Deck]) = layout("Decks")(
  main(
    // Deck buttons
    section(`class` := "grid")(
      for deck <- decks yield
        article(`class` := "deck-card")(
          h3(
            a(
              hx.get := s"/decks/${deck.id}",
              hx.target := "body"
            )(deck.name)
          ),
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

def deckDetail(deck: Deck, cardTypes: List[CardType]) = layout(s"Deck: ${deck.name}")(
  main(
    // Navigation
    section(
      a(hx.get := "/decks", hx.target := "body", `class` := "secondary")("⬅️ Back to Decks")
    ),

    // Deck info section
    article(`class` := "deck-info")(
      h2(deck.name),
      p(deck.description),
      div(`class` := "grid")(
        button(hx.get := s"/decks/${deck.id}/edit", hx.target := "body", `class` := "secondary")("✏️ Edit Name"),
        button(hx.delete := s"/decks/${deck.id}", hx.target := "body", `class` := "contrast")("🗑️ Delete Deck")
      )
    ),

    // Card types section
    section(
      h3("Card Types"),
      div(`class` := "grid")(
        for cardType <- cardTypes yield
          article(`class` := "card-type-card")(
            h4(cardType.name),
            div(`class` := "grid")(
              button(hx.get := s"/card_types/${cardType.id}", hx.target := "body", `class` := "secondary")("👁️ View"),
              button(hx.get := s"/card_types/${cardType.id}/edit", hx.target := "body", `class` := "secondary")("✏️ Edit"),
              button(hx.delete := s"/card_types/${cardType.id}", hx.target := "body", `class` := "contrast")("🗑️ Delete")
            )
          )
      ),

      // Add new card type button
      section(
        button(hx.get := s"/card_types/new?deck=${deck.id}", hx.target := "body", `class` := "primary")("➕ Add New Card Type")
      )
    )
  )
)

def editDeck(deck: Deck) = layout(s"Edit Deck: ${deck.name}")(
  main(
    // Navigation
    section(
      a(hx.get := s"/decks/${deck.id}", hx.target := "body", `class` := "secondary")("⬅️ Back to Deck")
    ),

    form(
      hx.patch := s"/decks/${deck.id}",
      hx.target := "body"
    )(
      fieldset(
        legend("Deck Information"),

        label("Name", `for` := "name"),
        input(`type` := "text", id := "name", name := "name", required, value := deck.name),

        label("Description", `for` := "description"),
        textarea(id := "description", name := "description", rows := 3)(deck.description),
      ),

      section(`class` := "grid")(
        button(`type` := "submit", `class` := "primary")("➡️ Update Deck"),
        a(hx.get := s"/decks/${deck.id}", hx.target := "body", `class` := "secondary")("❌ Cancel")
      )
    )
  )
)
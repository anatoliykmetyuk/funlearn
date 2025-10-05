package funlearn

import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom
import org.scalajs.dom.{ HTMLElement, Event }


@JSExportTopLevel("decks_new_JS")
object decks_new_JS:
  def addCardField(): Unit =
    val uid = System.currentTimeMillis()
    val cardTypeFields = dom.document.querySelector(".card-type-fields")

    /* Clone the template #card-type-field-template, append the cloned item to the template's parent */
    val template = dom.document.querySelector("#card-type-field-template")
    val newField = template.cloneNode(true).asInstanceOf[HTMLElement]
    newField.id = s"card-type-field-$uid"

    newField.querySelector("input[name='keys[0]']").name = s"keys[$uid]"
    newField.querySelector("input[name='prompts[0]']").name = s"prompts[$uid]"
    newField.querySelector("input[name='label_key']").name = s"label_key[$uid]"
    newField.querySelector("button").addEventListener("click", removeCardField)

    cardTypeFields.appendChild(newField)

  def removeCardField(btn: HTMLElement): Unit =
    val cardTypeField = btn.closest(".card-type-field")
    val cardTypeFields = cardTypeField.parentNode
    cardTypeFields.removeChild(cardTypeField)

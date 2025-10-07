package funlearn

import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom
import org.scalajs.dom.{ HTMLElement, Event }
import typings.jquery.{ mod as jq }
import typings.jquery.JQuery


@JSExportTopLevel("decks_new_JS")
object decks_new_JS:
  def addCardField(): Unit =
    val uid = System.currentTimeMillis()
    val cardTypeFields = jq(".card-type-fields")

    /* Clone the template #card-type-field-template, append the cloned item to the template's parent */
    // val template = dom.document.querySelector("#card-type-field-template")
    // And in jquery...
    val template = jq("#card-type-field-template")
    val newField = template.clone(true, true).asInstanceOf[JQuery[HTMLElement]]

    newField.find("input[name='keys[0]']").attr("name", s"keys[$uid]")
    newField.find("input[name='prompts[0]']").attr("name", s"prompts[$uid]")
    newField.find("input[name='label_key']").attr("name", s"label_key[$uid]")

    val removeBtn = newField.find("button")
    removeBtn.on("click", removeCardField(removeBtn, _))

    cardTypeFields.appendTo(newField)
  end addCardField

  def removeCardField(btn: typings.jquery.JQuery[Nothing], evt: typings.jquery.JQueryEventObject): Unit =
    btn.closest(".card-type-field").remove()

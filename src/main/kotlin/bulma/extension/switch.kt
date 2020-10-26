@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package bulma.extension

import bulma.ElementColor
import bulma.FieldElement
import bulma.Size
import bulma.booleanAttribute
import bulma.className
import kotlinx.html.InputType
import kotlinx.html.dom.create
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.onChangeFunction
import kotlinx.html.label
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event
import kotlinx.browser.document
import kotlin.random.Random

/** [Switch](https://wikiki.github.io/form/switch/) */
class Switch(
    text: String, color: ElementColor = ElementColor.None, size: Size = Size.None,
    rounded: Boolean = false, thin: Boolean = false, outlined: Boolean = false, checked: Boolean = false,
    onChange: (event: Event, value: Boolean) -> Unit = { _, _ -> }
) : FieldElement {

    private val id = "id-${Random.nextLong()}"

    override val root = document.create.div("field") {
        input(type = InputType.checkBox, classes = "switch") {
            this.checked = checked
            onChangeFunction = {
                val target = it.target
                if (target is HTMLInputElement) {
                    onChange(it, target.checked)
                }
            }
        }
        label {
            htmlFor = this@Switch.id
            +text
        }
    }

    private val inputNode = (root.querySelector("input") as HTMLInputElement).also {
        it.id = id
    }
    private val labelNode = root.querySelector("label") as HTMLLabelElement

    override var text: String
        get() = labelNode.innerText
        set(value) { labelNode.innerText = value }

    var checked
        get() = inputNode.checked
        set(value) { inputNode.checked = value }

    var disabled by booleanAttribute(false, "disabled", inputNode)

    var color by className(color, inputNode)

    var size by className(size, inputNode)

    var rounded by className(rounded, "is-rounded", inputNode)

    var thin by className(thin, "is-thin", inputNode)

    var outlined by className(outlined, "is-outlined", inputNode)
}

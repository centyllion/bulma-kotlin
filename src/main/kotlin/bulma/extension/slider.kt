@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package bulma.extension

import bulma.ControlElement
import bulma.ElementColor
import bulma.Size
import bulma.booleanAttribute
import bulma.className
import kotlinx.html.InputType
import kotlinx.html.dom.create
import kotlinx.html.js.input
import kotlinx.html.js.onInputFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import kotlinx.browser.document

/**
 * [Slider](https://wikiki.github.io/form/slider/) element .
 * The extension needs to be imported.
 */
class Slider(
    value: String = "", min: String = "0", max: String = "100", step: String = "5",
    color: ElementColor = ElementColor.None, size: Size = Size.None,
    circle: Boolean = false, fullWidth: Boolean = false,
    var onChange: (event: Event, value: String) -> Unit = { _, _ -> }
) : ControlElement {

    override val root = document.create.input(InputType.range, classes = "slider") {
        this.value = value
        this.min = min
        this.max = max
        this.step = step
        onInputFunction = {
            val target = it.target
            if (target is HTMLInputElement) {
                onChange(it, target.value)
            }
        }
    }

    var value: String
        get() = root.value
        set(value) {
            root.value = value
        }

    var min: String
        get() = root.min
        set(value) {
            root.min = value
        }

    var max: String
        get() = root.max
        set(value) {
            root.max = value
        }

    var step: String
        get() = root.step
        set(value) {
            root.step = value
        }

    var color by className(color, root)

    var size by className(size, root)

    var circle by className(circle, "is-circle", root)

    var fullWidth by className(fullWidth, "is-fullwidth", root)

    var disabled by booleanAttribute(false, "disabled", root)

}

package bulma.extension

import bulma.*
import kotlinx.browser.document
import kotlinx.html.InputType
import kotlinx.html.dom.create
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.span
import kotlinx.html.label
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event

enum class BadgePosition(override val className: String): HasClassName {
    IsTopLeft("is-top-left"), IsTop("is-top"), IsTopRight("is-top-right"),
    IsRight("is-right"), IsBottomRight("is-bottom-right"), IsBottom("is-bottom"),
    IsBottomLeft("is-bottom-left"), IsLeft("is-left"), None("")
}

/**
 * [Badge](https://github.com/CreativeBulma/bulma-badge)
*/
class Badge(
    text: String = "", color: ElementColor = ElementColor.None,
    outlined: Boolean = false, position: BadgePosition = BadgePosition.None
): BulmaElement {

    override val root = document.create.span ("badge") { +text }

    var color by className(color, root)

    var position by className(position, root)

    var outlined by className(outlined, "is-outlined", root)
}
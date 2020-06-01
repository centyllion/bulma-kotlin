package bulma

import kotlinx.html.DIV
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.figure
import kotlinx.html.h1
import kotlinx.html.i
import kotlinx.html.img
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.table
import kotlinx.html.js.tbody
import kotlinx.html.js.td
import kotlinx.html.js.tfoot
import kotlinx.html.js.th
import kotlinx.html.js.thead
import kotlinx.html.js.tr
import kotlinx.html.progress
import kotlinx.html.span
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLTableElement
import kotlin.browser.document

/** [Box](https://bulma.io/documentation/elements/box) element. */
class Box(vararg body: BulmaElement) : BulmaElement {
    override val root: HTMLElement = document.create.div("box")

    var body by bulmaList(body.toList(), root)
}

/** [Button](https://bulma.io/documentation/elements/button) element. */
class Button(
    title: String? = null, icon: Icon? = null, color: ElementColor = ElementColor.None,
    rounded: Boolean = false, outlined: Boolean = false, inverted: Boolean = false, size: Size = Size.None,
    light: Boolean = false, disabled: Boolean = false, var onClick: (Button) -> Unit = {}
) : ControlElement {

    override val root: HTMLElement = document.create.button(classes = "button") {
        onClickFunction = { if (!this@Button.disabled) onClick(this@Button) }
    }

    var title by html(title, root, Position.AfterBegin) { document.create.span { +it } }

    /** Left [Icon](https://bulma.io/documentation/form/general/#with-icons) */
    var icon by bulma(icon, root, Position.AfterBegin)

    var rounded by className(rounded, "is-rounded", root)

    var outlined by className(outlined, "is-outlined", root)

    var inverted by className(inverted, "is-inverted", root)

    var loading by className(false, "is-loading", root)

    var color by className(color, root)

    /** Adds support for [light colors](https://bulma.io/documentation/elements/button/#colors) (since `0.8.0). */
    var light by className(light, "is-light", root)

    var size by className(size, root)

    var disabled by booleanAttribute(disabled, "disabled", root)

}

fun iconButton(
    icon: Icon? = null, color: ElementColor = ElementColor.None, rounded: Boolean = false,
    outlined: Boolean = false, inverted: Boolean = false, size: Size = Size.None,
    light: Boolean = false, disabled: Boolean = false, onClick: (Button) -> Unit = {}
) = Button(null, icon, color, rounded, outlined, inverted, size, light,  disabled, onClick)

fun textButton(
    title: String? = null, color: ElementColor = ElementColor.None, rounded: Boolean = false,
    outlined: Boolean = false, inverted: Boolean = false, size: Size = Size.None,
    light: Boolean = false, disabled: Boolean = false, onClick: (Button) -> Unit = {}
) = Button(title, null, color, rounded, outlined, inverted, size, light, disabled, onClick)

/** [List of buttons](https://bulma.io/documentation/elements/button/#list-of-buttons). */
class ButtonsList(
    vararg buttons: Button, size: ButtonsSize = ButtonsSize.None,
    aligned: Alignment = Alignment.Left, addons: Boolean = false
) : BulmaElement {
    override val root: HTMLElement = document.create.div("buttons")

    var size by className(size, root)

    var aligned by className(aligned, root)

    var addons by className(addons, "has-addons", root)

    var buttons by bulmaList(buttons.toList(), root)
}

/** [Content](https://bulma.io/documentation/elements/content) element. */
class Content(block: DIV.() -> Unit = {}) : BulmaElement {
    override val root: HTMLElement = document.create.div("content") {
        block()
    }
}

/** [Delete](https://bulma.io/documentation/elements/delete) element. */
class Delete(var onClick: (Delete) -> Unit = {}) : BulmaElement {
    override val root: HTMLElement = document.create.button(classes = "delete") {
        onClickFunction = {
            if (!this@Delete.disabled) {
                onClick(this@Delete)
                it.stopPropagation()
            }
        }
    }

    var size by className(Size.None, root)

    var color by className(ElementColor.None, root)

    var disabled by booleanAttribute(false, "disabled", root)

}

enum class IconStyle(override val className: String): HasClassName {
    Solid("fas"), Regular("far"), Light("fal"), Brands("fab")
}

/** [Icon](https://bulma.io/documentation/elements/icon) element. */
class Icon(
    icon: String, size: Size = Size.None, color: TextColor = TextColor.None,
    rotate: FaRotate = FaRotate.None, flip: FaFlip = FaFlip.None,
    spin: Boolean = false, pulse: Boolean = false, style: IconStyle = IconStyle.Solid
) : ControlElement {
    override val root: HTMLElement = document.create.span("icon") {
        i("${style.className} fa-$icon") {
            attributes["aria-hidden"] = "true"
        }
    }

    private val iconNode = root.querySelector("i") as HTMLElement

    var icon by className(icon, iconNode, "fa-")

    var size
        get() = outerSize
        set(value) {
            outerSize = value
            iconSize = value.toFas()
        }

    var outerSize by className(size, root)

    var iconSize by className(size.toFas(), iconNode)

    var color by className(color, root)

    var style by className(style, iconNode)

    var rotate by className(rotate, iconNode)

    var flip by className(flip, iconNode)

    var spin by className(spin, "fa-spin", iconNode)

    var pulse by className(pulse, "fa-pulse", iconNode)

}

enum class ImageSize(override val className: String): HasClassName {
    None(""), S16("is-16x16"), S24("is-24x24"), S32("is-32x32"),
    S48("is-48x48"), S64("is-64x64"), S96("is-96x96"), S128("is-128x128"),
    Square("is-square"), S1by1("is-1by1"), S5by4("is-5by4"), S4by3("is-4by3"),
    S3by2("is-3by2"), S5by3("is-5by3"), S16by9("is-16by9"), S2by1("is-2by1"),
    S3by1("is-3by1"), S4by5("is-4by5"), S3by4("is-3by4"), S2by3("is-2by3"),
    S3by5("is-3by5"), S9by16("is-9by16"), S1by2("is-1by2"), S1by3("is-1by3")
}

/** [Image](https://bulma.io/documentation/elements/image/) */
class Image(src: String, size: ImageSize = ImageSize.None, rounded: Boolean = false): BulmaElement {

    override val root: HTMLElement = document.create.figure("image") {
        img(null, src)
    }

    private val imgNode = root.querySelector("img") as HTMLImageElement

    var src: String
        get() = imgNode.src
        set(value) { imgNode.src = value }

    var rounded by className(rounded, "is-rounded", imgNode)

    var size by className(size, root)

}

/** [Notification](https://bulma.io/documentation/elements/notification) element. */
class Notification(
    vararg body: BulmaElement, color: ElementColor = ElementColor.None,
    val onDelete: ((Notification) -> Unit)? = null
) : BulmaElement {

    override val root: HTMLElement = document.create.div("notification") {
        if (onDelete != null) {
            button(classes = "delete") {
                onClickFunction = { onDelete.invoke(this@Notification) }
            }
        }
    }

    var color by className(color, root)

    var body by bulmaList(body.toList(), root)
}

/** [Progress Bar](https://bulma.io/documentation/elements/progress) element. */
class ProgressBar(
    color: ElementColor = ElementColor.None, size: Size = Size.None,
    min: Int? = null, max: Int? = null, value: Int? = null
) : BulmaElement {

    override val root: HTMLElement = document.create.progress("progress")

    var color by className(color, root)

    var size by className(size, root)

    var min by intAttribute(min, "min", root)

    var value by intAttribute(value, "value", root)

    var max by intAttribute(max, "max", root)
}

class TableHeaderCell(text: String = "", vararg body: BulmaElement = emptyArray()): BulmaElement {
    override val root = document.create.th { +text }
    var body by bulmaList(body.asList(), root)
}

class TableHeaderRow(vararg cells: TableHeaderCell = emptyArray()): BulmaElement {
    override val root = document.create.tr()
    var cells by bulmaList(cells.asList(), root)
}

class TableCell(text: String = "", vararg body: BulmaElement = emptyArray()): BulmaElement {
    override val root = document.create.td { +text }
    var body by bulmaList(body.asList(), root)
}

class TableRow(vararg cells: TableCell = emptyArray()): BulmaElement {
    override val root = document.create.tr()
    var cells by bulmaList(cells.asList(), root)
}

/** [Table](https://bulma.io/documentation/elements/table) */
class Table(
    body: List<TableRow> = emptyList(), head: List<TableHeaderRow> = emptyList(), foot: List<TableHeaderRow> = emptyList(),
    bordered: Boolean = false, striped: Boolean = false, narrow: Boolean = false,
    hoverable: Boolean = false, fullWidth: Boolean = false
) : BulmaElement {

    override val root: HTMLTableElement = document.create.table("table")

    var head by
        embeddedBulmaList(head, root, Position.AfterBegin) { document.create.thead() }

    var body by
        embeddedBulmaList(body, root, Position.AfterBegin) { document.create.tbody() }

    var foot by
        embeddedBulmaList(foot, root, Position.BeforeEnd) { document.create.tfoot() }

    var bordered by className(bordered, "is-bordered", root)
    var striped by className(striped, "is-striped", root)
    var narrow by className(narrow, "is-narrow", root)
    var hoverable by className(hoverable, "is-hoverable", root)
    var fullWidth by className(fullWidth, "is-fullwidth", root)
}


/** [Tag](https://bulma.io/documentation/elements/tag) element. */
class Tag(
    text: String, color: ElementColor = ElementColor.None,
    size: Size = Size.None, rounded: Boolean = false, light: Boolean = false,
    delete: Delete? = null
) : ControlElement {

    override val root: HTMLElement = document.create.span("tag") {
        +text
    }

    var color by className(color, root)

    var size by className(size, root)

    var rounded by className(rounded, "is-rounded", root)

    /** Adds support for [light colors](https://bulma.io/documentation/elements/button/#colors) (since `0.8.0). */
    var light by className(light, "is-light", root)

    var delete by bulma(delete, root)
}

class TagDelete(
    color: ElementColor = ElementColor.None, size: Size = Size.None, rounded: Boolean = false
): ControlElement {
    override val root: HTMLElement = document.create.span("tag is-delete")

    var color by className(color, root)

    var size by className(size, root)

    var rounded by className(rounded, "is-rounded", root)
}

/** [Tags](https://bulma.io/documentation/elements/tag/#list-of-tags) element */
class Tags(tags: List<Tag> = emptyList(), size: TagsSize = TagsSize.None, addons: Boolean = false) : BulmaElement {

    override val root: HTMLElement = document.create.div("tags")

    var tags by bulmaList(tags, root)

    var size by className(size, root)

    var addons by className(addons, "has-addons", root)

}

class Title(text: String, size: TextSize = TextSize.None) : BulmaElement {

    override val root: HTMLElement = document.create.h1("title") { +text }

    var size by className(size, root)
}

class SubTitle(text: String, size: TextSize = TextSize.None) : BulmaElement {

    override val root: HTMLElement = document.create.h1("subtitle") { +text }

    var size by className(size, root)
}

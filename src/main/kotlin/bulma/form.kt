package bulma

import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.i
import kotlinx.html.input
import kotlinx.html.js.div
import kotlinx.html.js.input
import kotlinx.html.js.label
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onFocusFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.js.option
import kotlinx.html.js.p
import kotlinx.html.js.span
import kotlinx.html.js.textArea
import kotlinx.html.label
import kotlinx.html.select
import kotlinx.html.span
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.InputEvent
import org.w3c.files.FileList
import kotlin.browser.document

interface FieldElement : BulmaElement

/** [Field](https://bulma.io/documentation/form/general) */
class Field(
    vararg body: FieldElement, narrow: Boolean = false,
    addons: Boolean = false, addonsCentered: Boolean = false,
    addonsRight: Boolean = false, grouped: Boolean = false,
    groupedCentered: Boolean = false, groupedRight: Boolean = false,
    groupedMultiline: Boolean = false
) : FieldElement {
    override val root: HTMLElement = document.create.div("field")

    var body by bulmaList(body.toList(), root)

    /** Narrow property */
    var narrow by className(narrow, "is-narrow", root)

    /** See https://bulma.io/documentation/form/general/#form-addons */
    var addons by className(addons, "has-addons", root)

    /** See https://bulma.io/documentation/form/general/#form-addons */
    var addonsCentered by className(addonsCentered, "has-addons-centered", root)

    /** See https://bulma.io/documentation/form/general/#form-addons */
    var addonsRight by className(addonsRight, "has-addons-right", root)

    /** See https://bulma.io/documentation/form/general/#form-group */
    var grouped by className(grouped, "is-grouped", root)

    /** See https://bulma.io/documentation/form/general/#form-group */
    var groupedCentered by className(groupedCentered, "is-grouped-centered", root)

    /** See https://bulma.io/documentation/form/general/#form-group */
    var groupedRight by className(groupedRight, "is-grouped-right", root)

    /** See https://bulma.io/documentation/form/general/#form-group */
    var groupedMultiline by className(groupedMultiline, "is-grouped-multiline", root)
}

/** [Horizontal Field](https://bulma.io/documentation/form/general/#horizontal-form) */
class HorizontalField(label: FieldElement, vararg body: Field) : BulmaElement {

    override val root: HTMLElement = document.create.div("field is-horizontal") {
        div("field-label")
        div("field-body")
    }

    private val labelNode = root.querySelector(".field-label") as HTMLElement
    private val bodyNode = root.querySelector(".field-body") as HTMLElement

    var label by bulma(label, labelNode)

    var labelSize by className(Size.Normal, labelNode)

    var body by bulmaList(body.toList(), bodyNode)

}

class Label(text: String = "") : ControlElement, FieldElement {
    override val root: HTMLElement = document.create.label("label") {
        +text
    }
}

class Value(text: String = "") : ControlElement {
    override val root: HTMLElement = document.create.span("value") {
        +text
    }
}

class Help(text: String = "") : ControlElement {
    override val root: HTMLElement = document.create.p("help") {
        +text
    }

    var color by className(ElementColor.None, root)
}

interface ControlElement : BulmaElement

/** [Control](https://bulma.io/documentation/form/general/#form-control) */
class Control(
    element: ControlElement, leftIcon: Icon? = null, rightIcon: Icon? = null, expanded: Boolean = false
) : FieldElement {
    override val root: HTMLElement = document.create.div("control")

    var body by bulma(element, root)

    var expanded by className(expanded, "is-expanded", root)

    /** Left [Icon](https://bulma.io/documentation/form/general/#with-icons) */
    var leftIcon: Icon? = leftIcon
        set(value) {
            if (value != field) {
                updateIcon("left", value)
                field = value
            }
        }

    /** Right [Icon](https://bulma.io/documentation/form/general/#with-icons) */
    var rightIcon: Icon? = rightIcon
        set(value) {
            if (value != field) {
                updateIcon("right", value)
                field = value
            }
        }

    private fun updateIcon(place: String, icon: Icon?) {
        // removes previous if any
        val previousIcon = root.querySelector("control > span.is-$place")
        if (previousIcon != null) root.removeChild(previousIcon)

        // sets the has-icons-left class
        root.classList.toggle("has-icons-$place", icon != null)

        if (icon != null) {
            // prepares the new icon
            icon.root.classList.toggle("is-$place", true)
            // adds the new icon
            root.append(icon.root)
        }
    }

    init {
        updateIcon("left", leftIcon)
        updateIcon("right", rightIcon)
    }
}

interface TextView: ControlElement {
    var value: String
    var placeholder: String?
    var readonly: Boolean
    var static: Boolean

    var color: ElementColor
    var size: Size

    var onFocus: (Boolean) -> Unit
    var onChange: (event: InputEvent, value: String) -> Unit
}

/** [Input](https://bulma.io/documentation/form/input/) */
class Input(
    value: String = "", placeholder: String = "",
    color: ElementColor = ElementColor.None, size: Size = Size.None, columns: Int? = null,
    rounded: Boolean = false, loading: Boolean = false,
    readonly: Boolean = false, static: Boolean = false,
    override var onFocus: (Boolean) -> Unit = { },
    override var onChange: (event: InputEvent, value: String) -> Unit = { _, _ -> }
) : TextView {

    override val root = document.create.input(InputType.text, classes = "input") {
        this.value = value
        if (columns != null) this.size = "$columns"
        onInputFunction = {
            val target = it.target
            if (it is InputEvent && target is HTMLInputElement) {
                onChange(it, target.value)
            }
        }
        onBlurFunction = {
            val target = it.target
            if (it is FocusEvent && target is HTMLInputElement) {
                onFocus(false)
            }
        }
        onFocusFunction = {
            onFocus(true)
        }
    }

    override var value: String
        get() = root.value
        set(value) {
            if (root.value != value) {
                root.value = value
                onChange(InputEvent("change"), value)
            }
        }

    override var placeholder by attribute(placeholder, "placeholder", root)

    // TODO support input type (text, password, email, tel)

    override var color by className(color, root)

    override var size by className(size, root)

    var rounded by className(rounded, "is-rounded", root)

    var loading by className(loading, "is-loading", root)

    var disabled by booleanAttribute(false, "disabled", root)

    override var readonly by booleanAttribute(readonly, "readonly", root)

    override var static by className(static, "is-static", root)

}

/** [Text Area](https://bulma.io/documentation/form/textarea). */
class TextArea(
    value: String = "", placeholder: String = "", rows: String = "",
    color: ElementColor = ElementColor.None, size: Size = Size.None,
    loading: Boolean = false, readonly: Boolean = false, static: Boolean = false, fixedSize: Boolean = false,
    override var onFocus: (Boolean) -> Unit = { },
    override var onChange: (event: InputEvent, value: String) -> Unit = { _, _ -> }
) : TextView {

    override val root: HTMLTextAreaElement = document.create.textArea(classes = "textarea") {
        +value
        onInputFunction = {
            val target = it.target
            if (it is InputEvent && target is HTMLTextAreaElement) {
                onChange(it, target.value)
            }
        }
        onBlurFunction = {
            val target = it.target
            if (it is FocusEvent && target is HTMLTextAreaElement) {
                onFocus(false)
            }
        }
        onFocusFunction = {
            onFocus(true)
        }
    }

    var rows by attribute(rows, "rows", root)

    override var value: String
        get() = root.value
        set(value) {
            if (root.value != value) {
                root.value = value
                onChange(InputEvent("change"), value)
            }
        }

    override var placeholder by attribute(placeholder, "placeholder", root)

    override var color by className(color, root)

    override var size by className(size, root)

    var fixedSize by className(fixedSize, "has-fixed-size", root)

    override var readonly by booleanAttribute(readonly, "readonly", root)

    override var static by className(static, "is-static", root)

    var loading by className(loading, "is-loading", root)

    var disabled by booleanAttribute(false, "disabled", root)

}

class Option(text: String, value: String = "") : BulmaElement {
    override val root: HTMLElement = document.create.option {
        +text
        this.value = value
    }

    private val optionNode = root as HTMLOptionElement

    var value
        get() = optionNode.value
        set(value) {
            optionNode.value = value
        }

    val index get() = optionNode.index
}

/** [Select](http://bulma.io/documentation/form/select/) */
class Select(
    options: List<Option> = emptyList(), selectedIndex: Int = 0, color: ElementColor = ElementColor.None,
    size: Size = Size.None, rounded: Boolean = false,
    loading: Boolean = false, multiple: Boolean = false,
    onChange: (event: Event, value: String) -> Unit = { _, _ -> }
) : ControlElement {

    override val root: HTMLElement = document.create.div("select") {
        select {
            onInputFunction = {
                val target = it.target
                if (target is HTMLSelectElement) {
                    // TODO support multiple
                    onChange(it, target.value)
                }
            }
        }
    }

    private val selectNode = root.querySelector("select") as HTMLSelectElement
    init { selectNode.selectedIndex = selectedIndex }

    var selectedIndex
        get() = selectNode.selectedIndex
        set(value) { selectNode.selectedIndex = value }

    val selectedOption get() = options[selectedIndex]

    var options by bulmaList(options, selectNode)

    var color by className(color, root)

    var size by className(size, root)

    var rounded by className(rounded, "is-rounded", root)

    var loading by className(loading, "is-loading", root)

    var multiple
        get() = rootMultiple
        set(value) {
            rootMultiple = value
            selectMultiple = value
        }

    var disabled by booleanAttribute(false, "disabled", root)

    private var rootMultiple by className(multiple, "is-multiple", root)
    private var selectMultiple by booleanAttribute(multiple, "multiple", selectNode)

}

/** [Checkbox](https://bulma.io/documentation/form/checkbox) */
class Checkbox(
    text: String, checked: Boolean = false,
    onChange: (event: Event, value: Boolean) -> Unit = { _, _ -> }
) : ControlElement {
    override val root: HTMLElement = document.create.span("help checkbox") {
        +text
        input(type = InputType.checkBox) {
            this.checked = checked
            onChangeFunction = {
                val target = it.target
                if (target is HTMLInputElement) {
                    onChange(it, target.checked)
                }
            }
        }
    }

    private val inputNode = root.querySelector("input") as HTMLInputElement

    var checked
        get() = inputNode.checked
        set(value) { inputNode.checked = value }

    var disabled
        get() = disabledRoot
        set(_) {
            disabledInput = true
            disabledRoot = true
        }

    private var disabledInput by booleanAttribute(false, "disabled", inputNode)
    private var disabledRoot by booleanAttribute(false, "disabled", root)

}

// TODO radio groups http://bulma.io/documentation/form/radio/

/** (File)[https://bulma.io/documentation/form/file/] element */
class FileInput(
    label: String? = null, icon: String? = "upload",
    fileName: String? = null, boxed: Boolean = false, fullWidth: Boolean = false,
    color: ElementColor = ElementColor.None, size: Size = Size.None, alignment: Alignment = Alignment.Left,
    var onChange: (FileInput, FileList?) -> Unit = { _, _ ->}
): ControlElement {
    override val root: HTMLElement = document.create.div("file") {
        label("file-label") {
            input(InputType.file, classes = "file-input")
            span("file-cta")
        }
        onChangeFunction = {
            console.log("changed")
            onChange(this@FileInput, inputNode.files)
        }
    }

    private val labelNode = root.querySelector("label.file-label") as HTMLLabelElement
    private val inputNode = root.querySelector("input.file-input") as HTMLInputElement
    private val ctaNode = root.querySelector("span.file-cta") as HTMLSpanElement

    var icon by html(icon, ctaNode, Position.AfterBegin) {
        document.create.span("file-icon") { i("fas fa-$it") }
    }

    var label by html(label, ctaNode) { document.create.span("file-label") { +it } }

    var fileName by html(fileName, labelNode,
        onChange = { _, _, value -> root.classList.toggle("has-name", value != null)},
        prepare = { document.create.span("file-name") { +it }
    })

    var color by className(color, root)

    var size by className(size, root)

    var alignment by className(alignment, root)

    var boxed by className(boxed, "is-boxed", root)

    var fullWidth by className(fullWidth, "is-fullwidth", root)

    val files get() = inputNode.files
}


/**
 * [Slider](https://wikiki.github.io/form/slider/) element .
 * The extension needs to be imported.
 */
class Slider(
    value: String = "", min: String = "0", max: String = "100", step: String = "5",
    color: ElementColor = ElementColor.None, size: Size = Size.None, fullWidth: Boolean = false,
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

    var fullWidth by className(fullWidth, "is-fullwidth", root)

    var disabled by booleanAttribute(false, "disabled", root)

}

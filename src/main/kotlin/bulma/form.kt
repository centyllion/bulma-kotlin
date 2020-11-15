@file:Suppress("unused", "MemberVisibilityCanBePrivate")
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
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.FocusEvent
import org.w3c.dom.events.UIEvent
import org.w3c.files.FileList
import kotlinx.browser.document
import kotlinx.browser.window
import kotlin.random.Random

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

class Label(text: String = "", size: Size = Size.None) : ControlElement, FieldElement {
    override val root: HTMLElement = document.create.label("label") {
        +text
    }

    val size by className(size, root)
}

class Value(text: String = "") : ControlElement, FieldElement {
    override val root: HTMLElement = document.create.span("value") {
        +text
    }
}

class Help(text: String = "") :  ControlElement, FieldElement {
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
                updateIcon("left", field, value)
                field = value
            }
        }

    /** Right [Icon](https://bulma.io/documentation/form/general/#with-icons) */
    var rightIcon: Icon? = rightIcon
        set(value) {
            if (value != field) {
                updateIcon("right", field, value)
                field = value
            }
        }

    private fun updateIcon(place: String, old: Icon?, new: Icon?) {
        if (new != old) {
            // removes previous if any
            old?.let { root.removeChild(it.root) }

            // sets the has-icons-left class
            root.classList.toggle("has-icons-$place", new != null)

            if (new != null) {
                // prepares the new icon
                new.root.classList.toggle("is-$place", true)
                // adds the new icon
                root.append(new.root)
            }
        }
    }

    init {
        updateIcon("left", null, leftIcon)
        updateIcon("right", null, rightIcon)
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
    var onChange: (event: UIEvent, value: String) -> Unit
}

/** [Input](https://bulma.io/documentation/form/input/) */
class Input(
    value: String = "", placeholder: String = "",
    color: ElementColor = ElementColor.None, size: Size = Size.None, columns: Int? = null,
    rounded: Boolean = false, loading: Boolean = false,
    readonly: Boolean = false, static: Boolean = false,
    override var onFocus: (Boolean) -> Unit = { },
    override var onChange: (event: UIEvent, value: String) -> Unit = { _, _ -> }
) : TextView {

    override val root = document.create.input(InputType.text, classes = "input") {
        this.value = value
        if (columns != null) this.size = "$columns"
        onInputFunction = {
            val target = it.target
            if (it is UIEvent && target is HTMLInputElement) {
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
                onChange(UIEvent("change"), value)
            }
        }

    override var placeholder by attribute(placeholder, "placeholder", root)

    /** Input type, can be `text`, `password`, 'email`, 'tel`. */
    var type
        get() = root.type
        set(value) { root.type = value }

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
    override var onChange: (event: UIEvent, value: String) -> Unit = { _, _ -> }
) : TextView {

    override val root: HTMLTextAreaElement = document.create.textArea(classes = "textarea") {
        +value
        onInputFunction = {
            val target = it.target
            if (it is UIEvent && target is HTMLTextAreaElement) {
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
                onChange(UIEvent("change"), value)
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

class Option(text: String, value: String = text) : BulmaElement {
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
    onChange: (event: Event, selected: List<Option>) -> Unit = { _, _ -> }
) : ControlElement {

    override val root: HTMLElement = document.create.div("select") {
        select {
            onInputFunction = {
                val target = it.target
                if (target is HTMLSelectElement) {
                    val selectedOptions = target.selectedOptions
                    // creates a list with all [Option] by finding them by value
                    val translated = List(selectedOptions.length) {
                        val value = (selectedOptions[it] as org.w3c.dom.Option).value
                        options.find { it.value == value }
                    }.filterNotNull()
                    onChange(it, translated)
                }
            }
        }
    }

    // Uses a timeout to set the initial select index to ensure it's set
    private val selectNode = root.querySelector("select") as HTMLSelectElement
    init { window.setTimeout({ selectNode.selectedIndex = selectedIndex  }, 0) }

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

    var disabled by booleanAttribute(false, "disabled", selectNode)

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

/** (Radio groups)[http://bulma.io/documentation/form/radio/] elements */
class Radio(
    vararg elements: String, checked: String? = null, onChange: (Radio, String) -> Unit = { _, _ -> }
): FieldElement {

    private val name = "radio-name-${Random.nextLong()}"

    override val root: HTMLElement = document.create.div("control")

    var elements by htmlList(elements.toList(), root) { element ->
        document.create.label("radio") {
            input(InputType.radio, name = name) {
                this.checked = (element == checked)
                onChangeFunction = { onChange(this@Radio, element) }
            }
            +element
        }
    }
    // TODO add support for checked property
}


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


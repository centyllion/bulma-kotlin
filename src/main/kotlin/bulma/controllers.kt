@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package bulma

import org.w3c.dom.HTMLElement
import kotlin.properties.Delegates.observable

/** Controller interface that bind a Data to a BulmaElement within a Context */
interface Controller<Data, Context, out Element : BulmaElement> : BulmaElement {

    /** Data for the controller */
    var data: Data

    /** Context for the controller */
    var context: Context

    /** Is the controller read only ? */
    var readOnly: Boolean

    /** Main element for the controller */
    val container: Element

    override val root: HTMLElement get() = container.root

    /** Refresh the controller */
    fun refresh()
}

/** A [Controller] with no context */
abstract class NoContextController<Data, out Element : BulmaElement> : Controller<Data, Unit, Element> {
    override var context: Unit = Unit
}

/** [Controller] that handle a List of [Data]. */
class MultipleController<
        /** Data type for each controller */
        Data,
        /** Context for each controller (on context for all) */
        Context,
        /** BulmaElement for the whole list */
        ParentElement : BulmaElement,
        /** BulmaElement for each item */
        ItemElement : BulmaElement,
        /** Controller for each item */
        Ctrl : Controller<Data, Context, ItemElement>
>(
    initialList: List<Data>, initialContext: Context, header: List<ItemElement>, footer: List<ItemElement>,
    override val container: ParentElement,
    var onClick: ((Data, Ctrl) -> Unit)? = null,
    val controllerBuilder: (Data, Ctrl?) -> Ctrl,
    val updateParent: (parent: ParentElement, items: List<ItemElement>) -> Unit
) : Controller<List<Data>, Context, ParentElement> {

    /** List of Data to be handled by the controller */
    override var data: List<Data> by observable(initialList) { _, old, new ->
        if (old != new) {
            refreshControllers(old.diff(new) { a, b -> a === b })
            refresh()
        }
    }

    /** Context to pass to all controllers */
    override var context: Context by observable(initialContext) { _, old, new ->
        if (old != new) {
            controllers.forEach { it.context = new }
        }
    }

    override var readOnly: Boolean by observable(false) { _, old, new ->
        if (old != new) {
            controllers.forEach { it.readOnly = new }
        }
    }

    var header: List<ItemElement> by observable(header)
    { _, _, _ -> updateAllList() }

    var footer: List<ItemElement> by observable(footer)
    { _, _, _ -> updateAllList() }

    private var controllers: List<Ctrl> = listOf()

    val dataControllers: List<Ctrl> get() = controllers

    init {
        refreshControllers(emptyList<Data>().diff(initialList))
    }

    private fun refreshControllers(diff: List<Diff<Data>>) {
        val newControllers = controllers.toMutableList()
        diff.forEach {
            when (it.action) {
                DiffAction.Added -> {
                    val newController = controllerBuilder(it.element, null)
                    onClick?.let { onClick ->
                        newController.root.onclick = { onClick(newController.data, newController) }
                    }
                    newController.readOnly = readOnly
                    newControllers.add(it.index, newController)
                }
                DiffAction.Removed -> {
                    newControllers.removeAt(it.index)
                }
                DiffAction.Replaced -> {
                    val controller = controllerBuilder(it.element, newControllers[it.index])
                    controller.data = it.element
                    newControllers[it.index] = controller
                }
            }
        }

        controllers = newControllers
        updateAllList()
    }

    private fun updateAllList() {
        updateParent(container, header + controllers.map { it.container } + footer)
    }

    override fun refresh() {
        controllers.forEach { it.refresh() }
    }

    fun indexOf(controller: Controller<Data, Context, *>) = controllers.indexOfFirst {
        it == controller || (it is WrappedController<*, *, *, *> && it.source == it)
    }
}

fun <Data, Context, Ctrl : Controller<Data, Context, Column>> columnsController(
    initialList: List<Data>, initialContext: Context,
    header: List<Column> = emptyList(), footer: List<Column> = emptyList(),
    container: Columns = Columns(multiline = true),
    onClick: ((Data, Ctrl) -> Unit)? = null,
    controllerBuilder: (data: Data, previous: Ctrl?) -> Ctrl
) = MultipleController(
    initialList, initialContext, header, footer, container, onClick, controllerBuilder
) { parent, items -> parent.columns = items }

fun <Data, Ctrl : Controller<Data, Unit, Column>> noContextColumnsController(
    initialList: List<Data>,
    container: Columns = Columns().apply { multiline = true },
    header: List<Column> = emptyList(), footer: List<Column> = emptyList(),
    onClick: ((Data, Ctrl) -> Unit)? = null,
    controllerBuilder: (data: Data, previous: Ctrl?) -> Ctrl
) = columnsController(
    initialList, Unit, header, footer, container, onClick, controllerBuilder
)

fun <Data, Context, Ctrl : Controller<Data, Context, DropdownItem>> dropdownController(
    initialList: List<Data>, initialContext: Context, container: Dropdown = Dropdown(),
    header: List<DropdownItem> = emptyList(), footer: List<DropdownItem> = emptyList(),
    onClick: ((Data, Ctrl) -> Unit)? = null,
    controllerBuilder: (data: Data, previous: Ctrl?) -> Ctrl
) = MultipleController(
    initialList, initialContext, header, footer, container, onClick, controllerBuilder
) { parent, items -> parent.items = items }

fun <Data, Ctrl : Controller<Data, Unit, DropdownItem>> noContextDropdownController(
    initialList: List<Data>, container: Dropdown = Dropdown(),
    header: List<DropdownItem> = emptyList(), footer: List<DropdownItem> = emptyList(),
    onClick: ((Data, Ctrl) -> Unit)? = null,
    controllerBuilder: (data: Data, previous: Ctrl?) -> Ctrl
) = dropdownController(initialList, Unit, container, header, footer, onClick, controllerBuilder)

fun <Data, Context, Ctrl : Controller<Data, Context, PanelItem>> panelController(
    container: Panel, initialList: List<Data>, initialContext: Context,
    header: List<PanelItem> = emptyList(), footer: List<PanelItem> = emptyList(),
    onClick: ((Data, Ctrl) -> Unit)? = null,
    controllerBuilder: (data: Data, previous: Ctrl?) -> Ctrl
) = MultipleController(
    initialList, initialContext, header, footer, container, onClick, controllerBuilder
) { parent, items -> parent.items = items }

fun <Data, Ctrl : Controller<Data, Unit, PanelItem>> noContextPanelController(
    container: Panel, initialList: List<Data>,
    header: List<PanelItem> = emptyList(), footer: List<PanelItem> = emptyList(),
    onClick: ((Data, Ctrl) -> Unit)? = null,
    controllerBuilder: (data: Data, previous: Ctrl?) -> Ctrl
) = panelController(container, initialList, Unit, header, footer, onClick, controllerBuilder)

fun <Data, Context, Ctrl : Controller<Data, Context, MenuItem>> menuController(
    container: Menu, initialList: List<Data>, initialContext: Context,
    header: List<MenuItem> = emptyList(), footer: List<MenuItem> = emptyList(),
    onClick: ((Data, Ctrl) -> Unit)? = null,
    controllerBuilder: (data: Data, previous: Ctrl?) -> Ctrl
) = MultipleController(
    initialList, initialContext, header, footer, container, onClick, controllerBuilder
) { parent, items -> parent.items = items }

fun <Data, Ctrl : Controller<Data, Unit, MenuItem>> noContextMenuController(
    container: Menu, initialList: List<Data>,
    header: List<MenuItem> = emptyList(), footer: List<MenuItem> = emptyList(),
    onClick: ((Data, Ctrl) -> Unit)? = null,
    controllerBuilder: (data: Data, previous: Ctrl?) -> Ctrl
) = menuController(container, initialList, Unit, header, footer, onClick, controllerBuilder)

class WrappedController<Data, Context, Source : BulmaElement, Target : BulmaElement>(
    val source: Controller<Data, Context, Source>, target: Target
) : Controller<Data, Context, Target> {
    override var data: Data
        get() = source.data
        set(value) {
            source.data = value
        }

    override var context: Context
        get() = source.context
        set(value) {
            source.context = value
        }

    override var readOnly: Boolean
        get() = source.readOnly
        set(value) {
            source.readOnly = value
        }

    override val container: Target = target

    override fun refresh() = source.refresh()
}

/** Wraps a controller inside a Bulma element */
fun <Data, Context, Source : BulmaElement, Ctrl : Controller<Data, Context, Source>, Target : BulmaElement>
        Ctrl.wrap(transform: ((Ctrl) -> Target)) =
    WrappedController(this, transform(this))

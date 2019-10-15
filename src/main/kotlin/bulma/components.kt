package bulma

import kotlinx.html.a
import kotlinx.html.article
import kotlinx.html.aside
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.footer
import kotlinx.html.header
import kotlinx.html.hr
import kotlinx.html.i
import kotlinx.html.img
import kotlinx.html.js.div
import kotlinx.html.js.li
import kotlinx.html.js.nav
import kotlinx.html.js.onClickFunction
import kotlinx.html.p
import kotlinx.html.role
import kotlinx.html.section
import kotlinx.html.span
import kotlinx.html.ul
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.HTMLUListElement
import kotlin.browser.document
import kotlin.collections.set
import kotlin.properties.Delegates.observable

enum class BreadcrumbSeparator(override val className: String) : HasClassName {
    Default(""), Arrow("has-arrow-separator"), Bullet("has-bullet-separator"),
    Dot("has-dot-separator"), Succeeds("has-succeeds-separator")
}

class BreadcrumbElement(text: String = "", href: String = "", icon: Icon? = null) : BulmaElement {

    override val root: HTMLElement = document.create.li {
        a(href = href) { +text }
    }

    private val aNode = root.querySelector("a") as HTMLAnchorElement

    override var text: String
        get() = aNode.innerText
        set(value) {
            aNode.innerText = value
        }

    var href: String
        get() = aNode.href
        set(value) {
            aNode.href = value
        }

    var icon by bulma(icon, aNode)
}

/** [Breadcrumb](https://bulma.io/documentation/components/breadcrumb) element */
class Breadcrumb(
    vararg body: BreadcrumbElement, separator: BreadcrumbSeparator = BreadcrumbSeparator.Default,
    size: Size = Size.None, alignment: Alignment = Alignment.Left
) : BulmaElement {

    override val root: HTMLElement = document.create.nav(classes = "breadcrumb") {
        ul()
    }

    private val ulNode = root.querySelector("ul") as HTMLUListElement

    var body by bulmaList(body.toList(), ulNode)

    var separator by className(separator, root)

    var size by className(size, root)

    var alignment by className(alignment, root)
}


interface CardItem : BulmaElement

class CardHeader(text: String = "", icon: Icon? = null) : CardItem {
    override val root = document.create.header("card-header") {
        p("card-header-title") { +text }
    }

    private val titleNode = root.querySelector("p.card-header-title") as HTMLParagraphElement

    override var text
        get() = titleNode.innerText
        set(value) {
            titleNode.innerText = value
        }

    var icon by bulma(icon, root)

}

class CardImage(image: Image) : CardItem {
    override val root = document.create.header("card-image")

    var image by bulma(image, root)
}

class CardContent(vararg body: BulmaElement) : CardItem {
    override val root = document.create.header("card-content")

    var body by bulmaList(body.toList(), root)
}

interface CardFooterItem : BulmaElement

class CardFooterLinkItem(
    text: String, href: String? = null, onClick: (CardFooterLinkItem) -> Unit = {}
) : CardFooterItem {
    override val root = document.create.a(href, null, "card-footer-item") {
        +text
        onClickFunction = { onClick(this@CardFooterLinkItem) }
    } as HTMLAnchorElement

    var href: String
        get() = root.href
        set(value) {
            root.href = value
        }
}

class CardFooterContentItem(vararg content: BulmaElement) : CardFooterItem {
    override val root = document.create.p("card-footer-item")

    var content by bulmaList(content.toList(), root)
}

class CardFooter(vararg body: CardFooterItem) : CardItem {
    override val root = document.create.header("card-footer")

    var body by bulmaList(body.toList(), root)
}

/** [Card](https://bulma.io/documentation/components/card/) element */
class Card(
    vararg content: CardItem
) : BulmaElement {
    override val root = document.create.div("card")

    var content by bulmaList(content.toList(), root)

}

interface DropdownItem : BulmaElement

class DropdownSimpleItem(
    text: String, icon: Icon? = null, disabled: Boolean = false,
    onSelect: (DropdownSimpleItem) -> Unit = {}
) : DropdownItem {
    override val root: HTMLElement = document.create.a(null, "dropdown-item") {
        span { +text }
        onClickFunction = { if (!this@DropdownSimpleItem.disabled) onSelect(this@DropdownSimpleItem) }
    }

    private val textNode = root.querySelector("span") as HTMLElement

    var disabled by className(disabled, "is-disabled", root)

    override var text: String
        get() = textNode.innerText
        set(value) {
            textNode.innerText = value
        }

    var icon by bulma(icon, root, Position.AfterBegin)
}

class DropdownContentItem(
    vararg body: BulmaElement, highlighted: Boolean = false, disabled: Boolean = false,
    onSelect: (DropdownContentItem) -> Unit = {}
) : DropdownItem {

    override val root: HTMLElement =
        (if (highlighted) document.create.a() else document.create.div()).apply {
            classList.add("dropdown-item")
            onclick = { if (!this@DropdownContentItem.disabled) onSelect(this@DropdownContentItem) }
        }

    var body by bulmaList(body.toList(), root)

    var disabled by className(disabled, "is-disabled", root)
}

class DropdownDivider : DropdownItem {
    override val root = document.create.hr("dropdown-divider")
}

/** [Dropdown](https://bulma.io/documentation/components/dropdown) element */
class Dropdown(
    vararg items: DropdownItem,
    text: String = "", icon: Icon? = null, color: ElementColor = ElementColor.None,
    rounded: Boolean = false, hoverable: Boolean = false, right: Boolean = false, up: Boolean = false,
    dropDownIcon: String = "angle-down", menuWidth: String? = null,
    var onDropdown: (Dropdown) -> Unit = {}
) : ControlElement {

    override val root: HTMLElement = document.create.div(classes = "dropdown") {
        div("dropdown-trigger") {
            button(classes = "button") {
                attributes["aria-haspopup"] = "true"
                attributes["aria-controls"] = "dropdown-menu"
                span("dropdown-title") { +text }
                span("icon is-small") {
                    i("fas fa-$dropDownIcon") {
                        attributes["aria-hidden"] = "true"
                    }
                }
            }
            onClickFunction = {
                if (!disabled) {
                    if (!active) onDropdown(this@Dropdown)
                    toggleDropdown()
                }
            }
        }
        div("dropdown-menu") {
            div("dropdown-content")
        }
    }

    private val buttonNode = root.querySelector("button.button") as HTMLButtonElement
    private val toggleNode = root.querySelector("span.icon.is-small") as HTMLSpanElement
    private val titleNode = root.querySelector(".dropdown-title") as HTMLSpanElement
    private val menuNode = root.querySelector(".dropdown-menu") as HTMLDivElement
    private val contentNode = root.querySelector(".dropdown-content") as HTMLDivElement

    init {
        if (menuWidth != null) menuNode.style.width = menuWidth
    }

    var active: Boolean
        get() = root.classList.contains("is-active")
        set(value) {
            root.classList.toggle("is-active", value)
        }

    fun toggleDropdown() {
        active = !active
    }

    override var text: String
        get() = titleNode.innerText
        set(value) {
            titleNode.innerText = value
        }

    var icon by bulma(icon, buttonNode, Position.AfterBegin)

    var color by className(color, buttonNode)

    var items by bulmaList(items.toList(), contentNode) {
        it.root.apply { if (!classList.contains("dropdown-divider")) classList.add("dropdown-item") }
    }

    var rounded by className(rounded, "is-rounded", buttonNode)

    var hoverable by className(hoverable, "is-hoverable", root)

    var right by className(right, "is-right", root)

    var up by className(up, "is-up", root)

    var menuWith: String
        get() = menuNode.style.width
        set(value) {
            menuNode.style.width = value
        }

    var disabled by observable(false) { _, old, new ->
        toggleNode.classList.toggle("is-hidden", new)
    }
}

interface MenuItem : BulmaElement

class MenuLabel(text: String) : MenuItem {
    override val root: HTMLElement = document.create.p("menu-label") { +text }
}

class MenuLink(text: String, href: String? = null) : MenuItem {
    override val root = document.create.a(href) { +text } as HTMLAnchorElement

    var href: String
        get() = root.href
        set(value) {
            root.href = value
        }
}

class MenuList(vararg items: MenuItem) : MenuItem {
    override val root: HTMLElement = document.create.ul("menu-list")
    var items by bulmaList(items.toList(), root) {
        document.create.li().apply { appendChild(it.root) }
    }
}

/** [Menu](http://bulma.io/documentation/components/menu) */
class Menu(vararg items: MenuItem) : BulmaElement {
    override val root: HTMLElement = document.create.aside("menu")
    var items by bulmaList(items.toList(), root)
}

/** [Message](http://bulma.io/documentation/components/message) element */
class Message(
    header: List<BulmaElement> = listOf(), body: List<BulmaElement> = listOf(),
    size: Size = Size.None, color: ElementColor = ElementColor.None
) : BulmaElement {

    override val root: HTMLElement = document.create.article("message")

    var header by embeddedBulmaList(header, root, Position.AfterBegin) {
        document.create.div("message-header")
    }

    var body by embeddedBulmaList(body, root) {
        document.create.div("message-body")
    }

    var size by className(size, root)

    var color by className(color, root)

}

/** [Modal](https://bulma.io/documentation/components/modal) */
class Modal(vararg content: BulmaElement) : BulmaElement {
    override val root: HTMLElement = document.create.div("modal") {
        div("modal-background") {
            onClickFunction = { active = false }
        }
        div("modal-content")
        div("modal-close is-large") {
            attributes["aria-label"] = "close"
            onClickFunction = { active = false }
        }
    }

    private val contentNode = root.querySelector(".modal-content") as HTMLDivElement

    var active by className(false, "is-active", root)

    var content by bulmaList(content.toList(), contentNode)
}

/** [Modal Card](https://bulma.io/documentation/components/modal) */
class ModalCard(
    text: String,
    body: List<BulmaElement> = listOf(),
    buttons: List<Button> = listOf(),
    onClose: (ModalCard) -> Unit = {}
) : BulmaElement {
    override val root: HTMLElement = document.create.div("modal") {
        div("modal-background") {
            onClickFunction = { active = false }
        }
        div("modal-card") {
            header("modal-card-head") {
                p("modal-card-title") { +text }
                button(classes = "delete") {
                    attributes["aria-label"] = "close"
                    onClickFunction = { active = false }
                }
            }
            section("modal-card-body")
            footer("modal-card-foot")
        }
    }

    private val titleNode = root.querySelector("p.modal-card-title") as HTMLElement
    private val bodyNode = root.querySelector("section.modal-card-body") as HTMLElement
    private val footNode = root.querySelector("footer.modal-card-foot") as HTMLElement

    var active by className(false, "is-active", root)
    { _, _, new -> if (!new) onClose(this@ModalCard) }

    override var text: String
        get() = titleNode.innerText
        set(value) {
            titleNode.innerText = value
        }

    var body by bulmaList(body, bodyNode)

    var buttons by bulmaList(buttons, footNode)
}

interface NavBarItem : BulmaElement {

    var active: Boolean
        get() = root.classList.contains("is-active")
        set(value) {
            root.classList.toggle("is-active", value)
        }

    var expanded: Boolean
        get() = root.classList.contains("is-expanded")
        set(value) {
            root.classList.toggle("is-expanded", value)
        }

    var tab: Boolean
        get() = root.classList.contains("is-tab")
        set(value) {
            root.classList.toggle("is-tab", value)
        }
}

class NavBarLinkItem(text: String, href: String? = null, id: String? = null, onClick: (NavBarLinkItem) -> Unit = {}) :
    NavBarItem {
    override val root = document.create.a(href, null, "navbar-item") {
        +text
        if (id != null) attributes["id"] = id
        onClickFunction = { onClick(this@NavBarLinkItem) }
    } as HTMLAnchorElement

    var href: String
        get() = root.href
        set(value) {
            root.href = value
        }
}

class NavBarImageItem(
    image: String, href: String? = null, width: String? = null, height: String? = null,
    onClick: (NavBarImageItem) -> Unit = {}
) : NavBarItem {
    override val root = document.create.a(href, null, "navbar-item") {
        img(null, image) {
            this@img.width = width ?: ""
            this@img.height = height ?: ""
        }
        onClickFunction = { onClick(this@NavBarImageItem) }
    }

    val imgNode = root.querySelector("img") as HTMLElement

    var href by attribute(href, "href", root)

    var image by attribute(image, "src", imgNode)
}

class NavBarIconItem(icon: Icon, href: String? = null, onClick: (NavBarIconItem) -> Unit = {}) : NavBarItem {
    override val root = document.create.a(href, null, "navbar-item") {
        onClickFunction = { onClick(this@NavBarIconItem) }
    }

    var href by attribute(href, "href", root)

    var icon by bulma(icon, root)
}

class NavBarContentItem(vararg body: BulmaElement) : NavBarItem {
    override val root = document.create.div("navbar-item")
    var body by bulmaList(body.toList(), root)
}

enum class Fixed(override val className: String) : HasClassName {
    Not(""), Top("is-fixed-top"), Bottom("is-fixed-bottom")
}

/** [NavBar](https://bulma.io/documentation/components/navbar) element. */
class NavBar(
    brand: List<BulmaElement> = emptyList(),
    start: List<BulmaElement> = emptyList(), end: List<BulmaElement> = emptyList(),
    transparent: Boolean = false, spaced: Boolean = false,
    fixed: Fixed = Fixed.Not, color: ElementColor = ElementColor.None
) : BulmaElement {

    override val root = document.create.nav("navbar") {
        role = "navigation"
        attributes["aria-label"] = "main navigation"
        div("navbar-brand") {
            a(classes = "navbar-burger") {
                role = "button"
                attributes["aria-label"] = "menu"
                attributes["aria-expanded"] = "false"
                span { attributes["aria-hidden"] = "true" }
                span { attributes["aria-hidden"] = "true" }
                span { attributes["aria-hidden"] = "true" }
                onClickFunction = {
                    this@NavBar.active = !this@NavBar.active
                }
            }
        }
        div("navbar-menu")
    }

    private val brandNode = root.querySelector("div.navbar-brand") as HTMLElement
    private val burgerNode = root.querySelector("a.navbar-burger") as HTMLElement
    private val menuNode = root.querySelector("div.navbar-menu") as HTMLElement

    var transparent by className(transparent, "is-transparent", root)

    var fixed by className(fixed, root)

    var spaced by className(spaced, "is-spaced", root)

    var color by className(color, root)

    var brand by bulmaList(brand, brandNode, { burgerNode })

    var start by embeddedBulmaList(start, menuNode, Position.AfterBegin) { document.create.div("navbar-start") }

    var end by embeddedBulmaList(end, menuNode, Position.AfterBegin) { document.create.div("navbar-end") }

    var active: Boolean
        get() = menuNode.classList.contains("is-active")
        set(value) {
            menuNode.classList.toggle("is-active", value)
            burgerNode.classList.toggle("is-active", value)
        }
}

enum class PaginationElementType(override val className: String) : HasClassName {
    Previous("pagination-previous"), Next("pagination-next")
}

interface PaginationItem : BulmaElement

class PaginationEllipsis : PaginationItem {
    override val root = document.create.span("pagination-ellipsis") { +"&hellip;" }
}

class PaginationLink(
    text: String, href: String? = null, current: Boolean = false,
    var onClick: (PaginationLink) -> Unit = {}
) : PaginationItem {

    override val root = document.create.a(href, classes = "pagination-link") {
        +text
        onClickFunction = { onClick(this@PaginationLink) }
    } as HTMLAnchorElement

    var href: String
        get() = root.href
        set(value) {
            root.href = value
        }

    var current by className(current, "is-current", root)
}

class PaginationAction(
    text: String, href: String? = null, disabled: Boolean = false,
    var onClick: (PaginationAction) -> Unit = {}
) : BulmaElement {

    override val root = document.create.a(href) {
        +text
        if (disabled) attributes["disabled"] = ""
        onClickFunction = { if (!this@PaginationAction.disabled) onClick(this@PaginationAction) }
    } as HTMLAnchorElement

    var href: String
        get() = root.href
        set(value) {
            root.href = value
        }

    var disabled: Boolean
        get() = root.hasAttribute("disabled")
        set(value) {
            if (value) root.setAttribute("disabled", "") else root.removeAttribute("disabled")
        }
}


/** [Pagination](https://bulma.io/documentation/components/pagination) */
class Pagination(
    vararg items: PaginationItem, previous: PaginationAction? = null, next: PaginationAction? = null,
    rounded: Boolean = false, size: Size = Size.None, alignment: Alignment = Alignment.Left
) : BulmaElement {

    override val root: HTMLElement = document.create.nav("pagination")

    var previous by bulma(previous, root, Position.AfterBegin) {
        it.root.classList.add("pagination-previous")
        it.root
    }

    var next by bulma(next, root, Position.AfterBegin) {
        it.root.classList.add("pagination-next")
        it.root
    }

    var items by embeddedBulmaList(
        items.toList(), root, Position.BeforeEnd,
        { item -> document.create.li().apply { appendChild(item.root) } },
        { document.create.ul("pagination-list") }
    )

    var alignment by className(alignment, root)

    var size by className(size, root)

    var rounded by className(rounded, "is-rounded", root)
}

interface PanelItem : BulmaElement

class PanelTabsItem(text: String, onClick: (PanelTabsItem) -> Unit = {}) : BulmaElement {

    override val root: HTMLElement = document.create.a {
        +text
        onClickFunction = { onClick(this@PanelTabsItem) }
    }

    var active by className(false, "is-active", root)
}

class PanelTabs(vararg items: PanelTabsItem) : PanelItem {

    override val root: HTMLElement = document.create.p("panel-tabs")

    var items by bulmaList(items.toList(), root)
}

class PanelSimpleBlock(
    text: String, icon: String, href: String? = null, var onClick: (PanelSimpleBlock) -> Unit = {}
) : PanelItem {

    override val root = document.create.a(href, classes = "panel-block") {
        span { +text }
        onClickFunction = { onClick(this@PanelSimpleBlock) }
    } as HTMLAnchorElement

    private val textNode = root.querySelector("span") as HTMLElement

    var href: String
        get() = root.href
        set(value) {
            root.href = value
        }

    override var text: String
        get() = textNode.innerText
        set(value) {
            textNode.innerText = value
        }

    var icon by html(icon, root, Position.AfterBegin) {
        document.create.span("panel-icon") {
            i("fas fa-$it") {
                attributes["aria-hidden"] = "true"
            }
        }
    }

    var active by className(false, "is-active", root)
}

class PanelContentBlock(vararg content: BulmaElement) : PanelItem {
    override val root: HTMLElement = document.create.a(classes = "panel-block")

    var content by bulmaList(content.toList(), root)

}

/** [Panel](https://bulma.io/documentation/components/panel) */
class Panel(text: String, vararg items: PanelItem) : BulmaElement {

    override val root: HTMLElement = document.create.nav("panel") {
        p("panel-heading") { +text }
    }

    var items by embeddedBulmaList(items.toList(), root) { document.create.div() }
}

class TabItem(
    text: String, icon: String? = null, var onClick: (TabItem) -> Unit = {}
) : BulmaElement {

    override val root: HTMLElement = document.create.li {
        a { span { +text } }
        onClickFunction = { onClick(this@TabItem) }
    }

    private val textNode = root.querySelector("span") as HTMLElement
    private val aNode = root.querySelector("a") as HTMLElement

    override var text: String
        get() = textNode.innerText
        set(value) {
            textNode.innerText = value
        }

    var icon by html(icon, aNode, Position.AfterBegin) {
        document.create.span("icon") {
            i("fas fa-$it") {
                attributes["aria-hidden"] = "true"
            }
        }
    }

    var active by className(false, "is-active", root)
}

/** [Tabs](http://bulma.io/documentation/components/tabs) */
class Tabs(
    vararg items: TabItem, alignment: Alignment = Alignment.Left, size: Size = Size.None,
    boxed: Boolean = false, toggle: Boolean = false, toggleRounded: Boolean = false,
    fullWidth: Boolean = false
) : BulmaElement {

    override val root: HTMLElement = document.create.div("tabs")

    var items by embeddedBulmaList(items.toList(), root, Position.AfterBegin) { document.create.ul() }

    var alignment by className(alignment, root)

    var size by className(size, root)

    var toggle by className(toggle, "is-toggle", root)

    var toggleRounded by className(toggleRounded, "is-toggle-rounded", root)

    var boxed by className(boxed, "is-boxed", root)

    var fullWidth by className(fullWidth, "is-fullwidth", root)
}

class TabPage(val title: TabItem, val body: BulmaElement)

class TabPages(
    vararg pages: TabPage, val tabs: Tabs = Tabs(), initialTabIndex: Int = 0,
    var onTabChange: (page: TabPage) -> Unit = {}
) : BulmaElement {

    override val root: HTMLElement = document.create.div()

    var pages by observable(pages.toList()) { _, _, new ->
        tabs.items = new.map { it.title }
        pagesContent = new.map { it.body }
        preparePages(new)
    }

    private var pagesContent by bulmaList(pages.map { it.body }, root)

    init {
        tabs.apply { items = pages.map { it.title } }
        preparePages(this.pages)
        pages.getOrNull(initialTabIndex)?.let { selectPage(it, false) }
        root.insertAdjacentElement(Position.AfterBegin.value, tabs.root)
    }

    private fun preparePages(pages: List<TabPage>) {
        pages.map { page ->
            page.title.onClick = { selectPage(page) }
            page.body.hidden = true
        }
    }

    var selectedPage
        get() = pages.firstOrNull { it.title.active }
        set(value) = selectPage(value)


    private fun selectPage(page: TabPage?, notify: Boolean = true) {
        pages.forEach {
            it.body.hidden = true
            it.title.active = false
        }

        page?.let {
            it.body.hidden = false
            it.title.active = true
            if (notify) onTabChange(it)
        }
    }

}

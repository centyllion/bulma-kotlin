package bulma

import kotlinx.html.CANVAS
import kotlinx.html.DIV
import kotlinx.html.article
import kotlinx.html.canvas
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.figure
import kotlinx.html.footer
import kotlinx.html.p
import kotlinx.html.section
import kotlinx.html.span
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document

class HtmlWrapper<Html : HTMLElement>(override val root: Html) : BulmaElement

fun wrap(element: HTMLElement) = HtmlWrapper(element)

fun wrap(classes: String? = null, block: DIV.() -> Unit = {}) =
    HtmlWrapper(document.create.div(classes, block) as HTMLDivElement)

fun span(text: String = "", classes: String = "") =
    HtmlWrapper(document.create.span(classes) { +text })

fun p(text: String = "", classes: String = "") =
    HtmlWrapper(document.create.p(classes) { +text })

fun canvas(classes: String? = null, block: CANVAS.() -> Unit = {}) =
    HtmlWrapper(document.create.canvas(classes, block) as HTMLCanvasElement)

class Div(vararg initial: BulmaElement, classes: String? = null): BulmaElement {
    override val root: HTMLElement = document.create.div(classes)

    var body by bulmaList(initial.toList(), root)
}

class Span(vararg initial: BulmaElement, classes: String? = null): BulmaElement {
    override val root: HTMLElement = document.create.span(classes)

    var body by bulmaList(initial.toList(), root)
}

/** [Container](https://bulma.io/documentation/layout/container) element */
class Container(vararg body: BulmaElement) : BulmaElement {
    override val root: HTMLElement = document.create.div("container")

    var body by bulmaList(body.toList(), root)
}

/** [Level](https://bulma.io/documentation/layout/level) element */
class Level(
    left: List<BulmaElement> = emptyList(), center: List<BulmaElement> = emptyList(),
    right: List<BulmaElement> = emptyList(), mobile: Boolean = false
) : BulmaElement {

    override val root: HTMLElement = document.create.div("level")

    var mobile by className(mobile, "is-mobile", root)

    var left by embeddedBulmaList(left, root, Position.AfterBegin, ::addLevelItem) {
        document.create.div("level-left")
    }

    var center by bulmaList(center, root, { root.querySelector(".level-right") }) {
        document.create.div("level-item").apply { appendChild(it.root) }
    }

    var right by embeddedBulmaList(right, root, Position.BeforeEnd, ::addLevelItem) {
        document.create.div("level-right")
    }

    private fun addLevelItem(element: BulmaElement) = element.root.apply { classList.add("level-item") }
}

/** [Media](https://bulma.io/documentation/layout/media) element */
class Media(
    left: List<BulmaElement> = emptyList(),
    center: List<BulmaElement> = emptyList(),
    right: List<BulmaElement> = emptyList()
) : BulmaElement {
    override val root: HTMLElement = document.create.article("media") {
        div("media-content")
    }

    private val contentNode = root.querySelector(".media-content") as HTMLElement

    var left by embeddedBulmaList(left, root, Position.AfterBegin) {
        document.create.figure("media-left")
    }

    var center by bulmaList(center, contentNode)

    var right by embeddedBulmaList(right, root, Position.BeforeEnd) {
        document.create.div("media-right")
    }
}

/** [Hero](https://bulma.io/documentation/layout/hero) element */
class Hero : BulmaElement {
    override val root: HTMLElement = document.create.section("hero") {
        div("hero-body")
    }

    private val bodyNode = root.querySelector(".hero-body") as HTMLElement

    var size by className(Size.None, root)

    var fullheight by className(false, "is-fullheight", root)

    var color by className(ElementColor.None, root)

    var head by embeddedBulmaList(emptyList(), root, Position.AfterBegin) {
        document.create.div("hero-head")
    }

    var body by bulmaList(emptyList(), bodyNode)

    var foot by embeddedBulmaList(emptyList(), root, Position.BeforeEnd) {
        document.create.div("hero-foot")
    }
}

/** [Section](https://bulma.io/documentation/layout/section) element */
class Section(vararg body: BulmaElement) : BulmaElement {
    override val root: HTMLElement = document.create.div("section")

    var body by bulmaList(body.toList(), root)
}

/** [Footer](https://bulma.io/documentation/layout/footer) element */
class Footer(initialBody: List<BulmaElement> = emptyList()) : BulmaElement {
    override val root: HTMLElement = document.create.footer("footer")

    var body by bulmaList(initialBody, root)
}


enum class TileSize(override val className: String) : HasClassName {
    None(""),
    S1("is-1"),
    S2("is-2"),
    S3("is-3"),
    S4("is-4"),
    S5("is-5"),
    S6("is-6"),
    S7("is-7"),
    S8("is-8"),
    S9("is-9"),
    S10("is-10"),
    S11("is-11"),
    S12("is-12")
}

/** Child [Tile](https://bulma.io/documentation/layout/tiles) element */
class TileChild(vararg body: BulmaElement, size: TileSize = TileSize.None, vertical: Boolean = false) : BulmaElement {
    override val root: HTMLElement = document.create.div("tile is-child")

    var body by bulmaList(body.toList(), root)

    var size by className(size, root)

    /** Vertical property */
    var vertical by className(vertical, "is-vertical", root)
}

/** Tile with no class or Parent */
interface TileInner : BulmaElement

/** Parent [Tile](https://bulma.io/documentation/layout/tiles) element */
class TileParent(vararg body: TileChild, size: TileSize = TileSize.None, vertical: Boolean = false) : TileInner {
    override val root: HTMLElement = document.create.div("tile is-parent")

    var body by bulmaList(body.toList(), root)

    var size by className(size, root)

    /** Vertical property */
    var vertical by className(vertical, "is-vertical", root)
}

/** No class [Tile](https://bulma.io/documentation/layout/tiles) element */
class Tile(vararg body: TileInner, size: TileSize = TileSize.None, vertical: Boolean = false) : TileInner {
    override val root: HTMLElement = document.create.div("tile")

    var body by bulmaList(body.toList(), root)

    var size by className(size, root)

    /** Vertical property */
    var vertical by className(vertical, "is-vertical", root)
}

/** Ancestor [Tile](https://bulma.io/documentation/layout/tiles) element */
class TileAncestor(vararg body: TileInner, size: TileSize = TileSize.None, vertical: Boolean = false) : BulmaElement {
    override val root: HTMLElement = document.create.div("tile is-ancestor")

    var body by bulmaList(body.toList(), root)

    var size by className(size, root)

    /** Vertical property */
    var vertical by className(vertical, "is-vertical", root)
}


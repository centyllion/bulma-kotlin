@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package bulma

import kotlinx.html.dom.create
import kotlinx.html.js.div
import org.w3c.dom.HTMLElement
import kotlin.browser.document

enum class ColumnSize(override val className: String) : HasClassName {
    None(""),
    ThreeQuarters("is-three-quarters"),
    TwoThirds("is-two-thirds"),
    Half("is-half"),
    OneThird("is-one-third"),
    OneQuarter("is-one-quarter"),
    Full("is-full"),
    FourFifths("is-four-fifths"),
    ThreeFifths("is-three-fifths"),
    TwoFifths("is-two-fifths"),
    OneFifth("is-one-fifth"),
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

enum class ColumnsGap(override val className: String) : HasClassName {
    None(""),
    S0("is-0"),
    S1("is-1"),
    S2("is-2"),
    S3("is-3"),
    S4("is-4"),
    S5("is-5"),
    S6("is-6"),
    S7("is-7"),
    S8("is-8")
}

/** [Columns](https://bulma.io/documentation/columns) element */
class Columns(
    vararg columns: Column,
    multiline: Boolean = false, mobile: Boolean = false, desktop: Boolean = false,
    gapless: Boolean = false, centered: Boolean = false, vcentered: Boolean = false,
    gap: ColumnsGap = ColumnsGap.None
) : BulmaElement {

    override val root: HTMLElement = document.create.div("columns")

    /** [Multiline](https://bulma.io/documentation/columns/options/#multiline) */
    var multiline by className(multiline, "is-multiline", root)

    var mobile by className(mobile, "is-mobile", root)

    var desktop by className(desktop, "is-desktop", root)

    /** Removes [gap](https://bulma.io/documentation/columns/gap/#gapless) between columns */
    var gapless by className(gapless, "is-gapless", root)

    /** [Centering](https://bulma.io/documentation/columns/options/#centering-columns) */
    var centered by className(centered, "is-centered", root)

    /** [VCentered](https://bulma.io/documentation/columns/options/#vertical-alignment) */
    var vcentered by className(vcentered, "is-vcentered", root)

    /** [Variable Gap](https://bulma.io/documentation/columns/gap/#variable-gap) */
    var gap by className(gap, root)

    var columns by bulmaList(columns.toList(), root)
}

/** [Column](https://bulma.io/documentation/columns/basics) element */
class Column(
    vararg body: BulmaElement,
    size: ColumnSize = ColumnSize.None,
    mobileSize: ColumnSize = ColumnSize.None,
    tabletSize: ColumnSize = ColumnSize.None,
    desktopSize: ColumnSize = ColumnSize.None,
    wideScreenSize: ColumnSize = ColumnSize.None,
    fullHdSize: ColumnSize = ColumnSize.None,
    narrow: Boolean = false
) : BulmaElement {

    override val root: HTMLElement = document.create.div("column")

    // TODO support offset

    /** [Size](https://bulma.io/documentation/columns/sizes) */
    var size by className(size, root)

    /** Mobile [Responsiveness size](http://bulma.io/documentation/columns/responsiveness/) */
    var mobileSize by className(mobileSize, root, suffix = "-mobile")

    /** Tablet [Responsiveness size](http://bulma.io/documentation/columns/responsiveness/) */
    var tabletSize by className(tabletSize, root, suffix = "-tablet")

    /** Desktop [Responsiveness size](http://bulma.io/documentation/columns/responsiveness/) */
    var desktopSize by className(desktopSize, root, suffix = "-desktop")

    /** Wide screen [Responsiveness size](http://bulma.io/documentation/columns/responsiveness/) */
    var wideScreenSize by className(wideScreenSize, root, suffix = "-widescreen")

    /** Full HD [Responsiveness size](http://bulma.io/documentation/columns/responsiveness/) */
    var fullHdSize by className(fullHdSize, root, suffix = "-fullhd")

    var narrow by className(narrow, "is-narrow", root)

    var body by bulmaList(body.toList(), root)
}

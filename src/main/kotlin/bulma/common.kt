@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package bulma

import org.w3c.dom.HTMLElement


interface BulmaElement {
    val root: HTMLElement

    var text: String
        get() = root.innerText
        set(value) { root.innerText = value }

    var hidden: Boolean
        get() = root.classList.contains("is-hidden")
        set(value) { root.classList.toggle("is-hidden", value) }

    var invisible: Boolean
        get() = root.classList.contains("is-invisible")
        set(value) { root.classList.toggle("is-invisible", value)}
}

interface HasClassName {
    val className: String
}

enum class Size(override val className: String) : HasClassName {
    None(""), Small("is-small"), Normal("is-normal"),
    Medium("is-medium"), Large("is-large");

    fun toFas() = when (this) {
        None -> FasSize.None
        Small -> FasSize.Small
        Normal -> FasSize.None
        Medium -> FasSize.Medium
        Large -> FasSize.Large
    }
}

enum class ButtonsSize(override val className: String) : HasClassName {
    None(""), Small("are-small"),
    Medium("are-medium"), Large("are-large");
}

enum class TagsSize(override val className: String) : HasClassName {
    None(""), Normal("are-normal"),
    Medium("are-medium"), Large("are-large");
}

enum class FasSize(override val className: String) : HasClassName {
    None(""), Small("fa-sm"),
    Medium("fa-2x"), Large("fa-3x")
}

enum class FaRotate(override val className: String) : HasClassName {
    None(""), R90("fa-rotate-90"),  R180("fa-rotate-180"), R270("fa-rotate-270")
}

enum class FaFlip(override val className: String) : HasClassName {
    None(""), Horizontal("fa-flip-horizontal"),  Vertical("fa-flip-vertical"), Both("fa-flip-both")
}

enum class ElementColor(override val className: String) : HasClassName {
    None(""),
    White("is-white"), Black("is-black"),
    Light("is-light"), Dark("is-dark"),
    Primary("is-primary"), Info("is-info"),
    Link("is-link"), Success("is-success"),
    Warning("is-warning"), Danger("is-danger");

    fun next() = values().let { it[(ordinal + 1) % it.count()] }
}

enum class TextSize(override val className: String) : HasClassName {
    None(""),
    S1("is-1"), S2("is-2"), S3("is-3"),
    S4("is-4"), S5("is-5"), S6("is-6");
}

enum class TextColor(override val className: String) : HasClassName {
    None(""), White("has-text-white"),
    Black("has-text-black"), Light("has-text-light"),
    Dark("has-text-dark"), Primary("has-text-primary"),
    Info("has-text-info"), Link("has-text-link"),
    Success("has-text-success"), Warning("has-text-warning"), Danger("has-text-danger"),
    BlackBis("has-text-black-bis"), BlackBer("has-text-black-ter"),
    GreyDarker("has-text-grey-darker"), GreyDark("has-text-grey-dark"),
    Grey("has-text-grey"),
    GreyLight("has-text-grey-light"), GreyLighter("has-text-grey-lighter"),
    WhiteTer("has-text-white-ter"), WhiteBis("has-text-white-bis");

    fun next() = values().let { it[(ordinal + 1) % it.count()] }
}

enum class Alignment(override val className: String) : HasClassName {
    Left(""), Centered("is-centered"), Right("is-right")
}

enum class Spacing(override val className: String) : HasClassName {
    None(""),
    M("m"), M0("m-0"), M1("m-1"), M2("m-2"), M3("m-3"), M4("m-4"), M5("m-5"), M6("m-6"),
    Mt("mt"), Mt0("mt-0"), Mt1("mt-1"), Mt2("mt-2"), Mt3("mt-3"), Mt4("mt-4"), Mt5("mt-5"), Mt6("mt-6"),
    Mr("mr"), Mr0("mr-0"), Mr1("mr-1"), Mr2("mr-2"), Mr3("mr-3"), Mr4("mr-4"), Mr5("mr-5"), Mr6("mr-6"),
    Mb("mb"), Mb0("mb-0"), Mb1("mb-1"), Mb2("mb-2"), Mb3("mb-3"), Mb4("mb-4"), Mb5("mb-5"), Mb6("mb-6"),
    Ml("ml"), Ml0("ml-0"), Ml1("ml-1"), Ml2("ml-2"), Ml3("ml-3"), Ml4("ml-4"), Ml5("ml-5"), Ml6("ml-6"),
    Mx("mx"), Mx0("mx-0"), Mx1("mx-1"), Mx2("mx-2"), Mx3("mx-3"), Mx4("mx-4"), Mx5("mx-5"), Mx6("mx-6"),
    My("my"), My0("my-0"), My1("my-1"), My2("my-2"), My3("my-3"), My4("my-4"), My5("my-5"), My6("my-6"),

    P("p"), P0("p-0"), P1("p-1"), P2("p-2"), P3("p-3"), P4("p-4"), P5("p-5"), P6("p-6"),
    Pt("pt"), Pt0("pt-0"), Pt1("pt-1"), Pt2("pt-2"), Pt3("pt-3"), Pt4("pt-4"), Pt5("pt-5"), Pt6("pt-6"),
    Pr("pr"), Pr0("pr-0"), Pr1("pr-1"), Pr2("pr-2"), Pr3("pr-3"), Pr4("pr-4"), Pr5("pr-5"), Pr6("pr-6"),
    Pb("pb"), Pb0("pb-0"), Pb1("pb-1"), Pb2("pb-2"), Pb3("pb-3"), Pb4("pb-4"), Pb5("pb-5"), Pb6("pb-6"),
    Pl("pl"), Pl0("pl-0"), Pl1("pl-1"), Pl2("pl-2"), Pl3("pl-3"), Pl4("pl-4"), Pl5("pl-5"), Pl6("pl-6"),
    Px("px"), Px0("px-0"), Px1("px-1"), Px2("px-2"), Px3("px-3"), Px4("px-4"), Px5("px-5"), Px6("px-6"),
    Py("py"), Py0("py-0"), Py1("py-1"), Py2("py-2"), Py3("py-3"), Py4("py-4"), Py5("py-5"), Py6("py-6"),
}
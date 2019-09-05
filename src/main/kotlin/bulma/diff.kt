package bulma

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

enum class DiffAction { Replaced, Added, Removed }

/** Diff description */
data class Diff<T>(val action: DiffAction, val index: Int, val element: T)

/**
 * Computes differences between [this] and [other].
 * It returns a list of [Diff] that describes the changes. The result is
 * sequential and the [Diff]s must be applied in order to be valid.
 */
internal fun <T> List<T>.diff(other: List<T>, equality: (T, T) -> Boolean = { a, b -> a == b }): List<Diff<T>> {
    var iSource = 0
    var iOther = 0

    // Important, always use iOther as DiffAction index since it's the expected result
    val result = mutableListOf<Diff<T>>()
    while (iSource < size || iOther < other.size) {
        when {
            iSource >= size -> {
                // source is empty, complete it using add
                result.add(Diff(DiffAction.Added, iOther, other[iOther]))
                iOther += 1
            }
            iOther >= other.size -> {
                // other empty, clear the source with remove
                result.add(Diff(DiffAction.Removed, iOther, this[iSource]))
                iSource += 1
            }
            equality(this[iSource], other[iOther]) -> {
                // nothing changed, next
                iSource += 1
                iOther += 1
            }
            iSource + 1 < size && iOther + 1 < other.size && this[iSource + 1] == other[iOther + 1] -> {
                // next is equals, just replace this one
                result.add(Diff(DiffAction.Replaced, iOther, other[iOther]))
                iSource += 1
                iOther += 1
            }
            else -> {
                // searches in either source or other for a current object
                val indexOfOther = other.subList(iOther, other.size).indexOfFirst { equality(it, this[iSource]) }
                val indexOfSource = subList(iSource, size).indexOfFirst { equality(it, other[iOther]) }
                when {
                    indexOfOther in 0..3 -> {
                        repeat(indexOfOther) {
                            // next other is current source
                            result.add(Diff(DiffAction.Added, iOther, other[iOther]))
                            iOther += 1
                        }
                    }
                    indexOfSource in 0..3 -> {
                        repeat(indexOfSource) {
                            // next source is current other
                            result.add(Diff(DiffAction.Removed, iOther, this[iSource]))
                            iSource += 1
                        }
                    }
                    else -> {
                        // just different
                        result.add(Diff(DiffAction.Replaced, iOther, other[iOther]))
                        iSource += 1
                        iOther += 1
                    }
                }
            }
        }
    }

    return result
}


/** From a list of [Diff] ([diff]) it computes the result one */
internal fun <T> List<T>.applyDiff(diff: List<Diff<T>>): List<T> {
    val result = this.toMutableList()
    diff.forEach {
        when (it.action) {
            DiffAction.Added -> result.add(it.index, it.element)
            DiffAction.Removed -> result.removeAt(it.index)
            DiffAction.Replaced -> result[it.index] = it.element
        }
    }
    return result
}

/** Computes differences between values and applies changes to container */
fun <T> applyChanges(
    oldValue: List<T>, value: List<T>,
    container: HTMLElement, reference: Element?, position: Position?,
    prepare: (T) -> HTMLElement
) = oldValue.diff(value).forEach {
    when (it.action) {
        DiffAction.Added -> prepare(it.element).let { new ->
            when {
                container.childElementCount == 0 && reference != null -> container.insertBefore(new, reference)
                container.childElementCount == 0 -> container.appendChild(new)
                it.index < container.childElementCount -> container.insertBefore(new, container.children.item(it.index))
                position != null -> container.insertAdjacentElement(position.value, new)
                else -> container.appendChild(new)
            }
        }
        DiffAction.Removed -> container.childNodes[it.index]?.let { container.removeChild(it) }
        DiffAction.Replaced -> container.childNodes[it.index]?.let { toReplace ->
            container.replaceChild(prepare(it.element), toReplace)
        }
    }
}

package com.centyllion.model

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class DiffTest {

    fun <T> testDiff(source: List<T>, other: List<T>, diff: List<Diff<T>>? = null) {
        val computedDiff = source.diff(other)
        if (diff != null) assertEquals(diff, computedDiff)
        assertEquals(other, source.applyDiff(computedDiff))
    }

    @Test
    fun testSwap1() = testDiff(
        listOf("1", "2"), listOf("2", "1")
    )

    @Test
    fun testAdd1() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "2", "3", "4", "5"),
        listOf(Diff(DiffAction.Added, 4, "5"))
    )

    @Test
    fun testAdd2() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "1bis", "2", "2bis", "3", "4"),
        listOf(
            Diff(DiffAction.Added, 1, "1bis"),
            Diff(DiffAction.Added, 3, "2bis")
        )
    )

    @Test
    fun testAdd3() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "2", "2bis", "2ter", "3", "4"),
        listOf(
            Diff(DiffAction.Added, 2, "2bis"),
            Diff(DiffAction.Added, 3, "2ter")
        )
    )

    @Test
    fun testAdd4() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "2", "2bis", "3", "3bis", "4", "4bis"),
        listOf(
            Diff(DiffAction.Added, 2, "2bis"),
            Diff(DiffAction.Added, 4, "3bis"),
            Diff(DiffAction.Added, 6, "4bis")
        )
    )

    @Test
    fun testAdd5() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "1bis", "1ter", "1qar", "2", "3", "4")
    )

    @Test
    fun testRemove1() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "2", "3"),
        listOf(
            Diff(DiffAction.Removed, 3, "4")
        )
    )

    @Test
    fun testRemove2() = testDiff(
        listOf("1", "2", "3", "4"), listOf("2", "3", "4"),
        listOf(
            Diff(DiffAction.Removed,0, "1")
        )
    )

    @Test
    fun testRemove3() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "3", "4"),
        listOf(
            Diff(DiffAction.Removed, 1, "2")
        )
    )

    @Test
    fun testRemove4() = testDiff(
        listOf("1", "2", "3", "4"), listOf("3", "4"),
        listOf(
            Diff(DiffAction.Removed, 0, "1"),
            Diff(DiffAction.Removed, 0, "2")
        )
    )

    @Test
    fun testRemove5() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "4"),
        listOf(
            Diff(DiffAction.Removed, 1, "2"),
            Diff(DiffAction.Removed, 1, "3")
        )
    )

    @Test
    fun testChange1() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "2bis", "3", "4"),
        listOf(
            Diff(DiffAction.Replaced, 1, "2bis")
        )
    )

    @Test
    fun testChange2() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1bis", "2bis", "3", "4"),
        listOf(
            Diff(DiffAction.Replaced, 0, "1bis"),
            Diff(DiffAction.Replaced, 1, "2bis")
        )
    )

    @Test
    fun testChange3() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "2bis", "3bis", "4"),
        listOf(
            Diff(DiffAction.Replaced, 1, "2bis"),
            Diff(DiffAction.Replaced, 2, "3bis")
        )
    )

    @Test
    fun testChange4() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1bis", "2bis", "3bis", "4bis"),
        listOf(
            Diff(DiffAction.Replaced, 0, "1bis"),
            Diff(DiffAction.Replaced, 1, "2bis"),
            Diff(DiffAction.Replaced, 2, "3bis"),
            Diff(DiffAction.Replaced, 3, "4bis")
        )
    )

    fun randomList(size: Int = 25, max: Int = 5) = ArrayList<String>(size).apply {
        (0 until size).forEach { add("${Random.nextInt(max)}") }
    }

    @Test
    fun randomTest() {
        // Tests diff apply over totally random list
        (0 until 50).forEach { testDiff(randomList(), randomList()) }
    }

    @Test
    fun testEmpty1() = testDiff(listOf(), listOf("1", "2"))

    @Test
    fun testEmpty2() = testDiff(listOf("1", "2"), listOf())

    @Test
    fun testInsertion1() = testDiff(
        listOf("1", "2", "3", "4"), listOf("1", "1.1", "1.2", "2", "2.1", "2.2", "3", "3.1", "3.2", "4", "4.1", "4.2")
    )

    @Test
    fun testSeveralTimesTheSameElement() = testDiff(
        listOf("1", "1"), listOf("1.1", "1"), listOf(Diff(DiffAction.Replaced, 0, "1.1"))
    )

}

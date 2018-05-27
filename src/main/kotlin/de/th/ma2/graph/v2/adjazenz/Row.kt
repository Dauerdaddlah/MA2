package de.th.ma2.graph.v2.adjazenz

// *****************************************************************************************************************
// utility class representing the data of 1 row of a matrix
interface Row : Iterable<Int> {
    val index: Int

    operator fun get(col: Int): Boolean

    operator fun set(col: Int, value: Boolean)

    // *************************************************************************************************************
    // utility functions for the code to look more beautiful
    fun contains(col: Int): Boolean = get(col)

    operator fun get(colName: String): Boolean = get(colName.toIndex())

    operator fun set(colName: String, value: Boolean) = set(colName.toIndex(), value)

    fun contains(colName: String): Boolean = contains(colName.toIndex())

    fun isPresent(): Boolean

    fun String.toIndex(): Int
}
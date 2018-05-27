package de.th.ma2.graph.v2.adjazenz

// *****************************************************************************************************************
// utility class representing the data of 1 column of a matrix
interface Column: Iterable<RowIndex> {
    val index: ColIndex

    operator fun get(row: RowIndex): Boolean

    operator fun set(row: RowIndex, value: Boolean)

    // *************************************************************************************************************
    // utility functions for the code to look more beautiful
    fun contains(row: RowIndex): Boolean = get(row)

    operator fun get(rowName: String): Boolean = get(rowName.toIndex())

    operator fun set(rowName: String, value: Boolean) = set(rowName.toIndex(), value)

    fun contains(rowName: String): Boolean = contains(rowName.toIndex())

    fun isPresent(): Boolean

    fun String.toIndex(): ColIndex
}
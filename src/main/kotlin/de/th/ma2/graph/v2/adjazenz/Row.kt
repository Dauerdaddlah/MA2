package de.th.ma2.graph.v2.adjazenz

// *****************************************************************************************************************
// utility class representing the data of 1 row of a matrix
interface Row : Iterable<ColIndex> {
    val index: RowIndex

    operator fun get(col: ColIndex): Boolean

    operator fun set(col: ColIndex, value: Boolean)

    // *************************************************************************************************************
    // utility functions for the code to look more beautiful
    fun contains(col: ColIndex): Boolean = get(col)

    operator fun get(colName: String): Boolean = get(colName.toIndex())

    operator fun set(colName: String, value: Boolean) = set(colName.toIndex(), value)

    fun contains(colName: String): Boolean = contains(colName.toIndex())

    fun isPresent(): Boolean

    fun String.toIndex(): ColIndex
}
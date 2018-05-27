package de.th.ma2.graph.v2.adjazenz

// interface for representing a matrix
// this interface is used to only show the relevant parts/fields/methods of the class without overloading with the
// 'complex' implementation
interface AdjazenzMatrix {
    val rowNames: List<String>
    val colNames: List<String>

    fun addRow(rowName: String)
    fun removeRow(row: Int)

    fun addCol(colName: String)
    fun removeCol(col: Int)

    fun row(row: Int): Row
    fun column(col: Int): Column

    // *****************************************************************************************************************
    // utility functions for the code to look more beautiful
    val rows: Iterable<Row>
    val columns: Iterable<Column>

    fun removeRow(rowName: String) = removeRow(rowName.toRowIndex())
    fun removeCol(colName: String) = removeCol(colName.toColIndex())

    operator fun get(row: Int): Row = row(row)
    operator fun get(rowName: String): Row = get(rowName.toRowIndex())
    fun contains(row: Int, col: Int): Boolean = this[row][col]
    fun contains(rowName: String, col: Int): Boolean = contains(rowName.toRowIndex(), col)
    fun contains(row: Int, colName: String): Boolean = contains(row, colName.toColIndex())
    fun contains(rowName: String, colName: String): Boolean = contains(rowName.toRowIndex(), colName.toColIndex())

    fun row(rowName: String): Row = row(rowName.toRowIndex())
    fun column(colName: String): Column = column(colName.toColIndex())

    fun String.toRowIndex() = rowNames.indexOf(this)
    fun String.toColIndex() = colNames.indexOf(this)
}

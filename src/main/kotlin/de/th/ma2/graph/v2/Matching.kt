package de.th.ma2.graph.v2

import de.th.ma2.graph.v2.adjazenz.AdjazenzMatrix
import de.th.ma2.graph.v2.adjazenz.AdjazenzMatrixImpl
import de.th.ma2.graph.v2.adjazenz.ColIndex
import de.th.ma2.graph.v2.adjazenz.RowIndex

class Matching(val graph: GraphBipartit) {
    val adjazenz: AdjazenzMatrix = AdjazenzMatrixImpl()

    init {
        graph.adjazenz.rowNames.forEach { adjazenz.addRow(it) }
        graph.adjazenz.colNames.forEach { adjazenz.addCol(it) }
    }

    fun containsCol(col: ColIndex): Boolean {
        return adjazenz.column(col).isPresent()
    }

    fun extend(way: ExtendingWay) {
        for(element in way) {
            // element.isColElement meanns a connection row -> col (forward)
            // element.isRowElement means a connection col -> row (backward)
            // if it is a ColElement we need to add the connection
            // otherwise we need to delete the connection
            adjazenz[element.rowIndex][element.colIndex] = element.isColElement()
        }
    }

    // *****************************************************************************************************************
    // general utility functions not important for matching

    override fun toString(): String {
        return adjazenz.toString()
    }

    // *****************************************************************************************************************
    // utility functions for the code to look more beautiful

    fun rowConnectedTo(col: ColIndex): RowIndex {
        return adjazenz.column(col)
                // as there can only one row be connected we can simple return the first match
                .first()
    }
}
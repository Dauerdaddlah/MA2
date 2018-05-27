package de.th.ma2.graph.v2

import de.th.ma2.graph.v2.adjazenz.AdjazenzMatrix
import de.th.ma2.graph.v2.adjazenz.AdjazenzMatrixImpl

class Matching(val graph: GraphBipartit) {
    val adjazenz: AdjazenzMatrix = AdjazenzMatrixImpl()

    init {
        graph.adjazenz.rowNames.forEach { adjazenz.addRow(it) }
        graph.adjazenz.colNames.forEach { adjazenz.addCol(it) }
    }

    fun containsCol(col: Int): Boolean {
        return adjazenz.column(col).isPresent()
    }

    fun extend(way: ExtendingWay) {
        var last: Int? = null
        var add = false

        for (index in way.indizes) {
            if(last != null) {
                if(add) {
                    adjazenz[last][index] = add
                } else {
                    adjazenz[index][last] = add
                }
            }

            last = index
            add = !add
        }
    }

    // *****************************************************************************************************************
    // general utility functions not important for matching

    override fun toString(): String {
        return adjazenz.toString()
    }

    // *****************************************************************************************************************
    // utility functions for the code to look more beautiful

    fun rowConnectedTo(col: Int): Int {
        return adjazenz.column(col)
                // as there can only one row be connected we can simple return the first match
                .first()
    }
}
package de.th.ma2.graph.v2

import de.th.ma2.graph.v2.adjazenz.AdjazenzMatrix
import de.th.ma2.graph.v2.adjazenz.RowIndex

class GraphBipartit(val adjazenz: AdjazenzMatrix) {
    fun findMatching(): Matching {
        val matching = initMatching()

        for (row in adjazenz.rows) {
            val way = findExtendingWay(row.index, matching)

            if (way.found()) {
                matching.extend(way!!)
            }
        }

        return matching
    }

    private inline fun initMatching(): Matching {
        return Matching(this)
    }

    private fun findExtendingWay(row: RowIndex, matching: Matching): ExtendingWay? {
        var way = findExtendingWaySimple(row, matching)
        if(!way.found()) {
            way = searchExtendedWayByTree(row, matching)
        }

        return way
    }

    private fun findExtendingWaySimple(row: RowIndex, matching: Matching): ExtendingWay? {
        for (col in adjazenz.columns) {
            if(col.contains(row)
                && !matching.containsCol(col.index)) {
                return ExtendingWay(row, col.index)
            }
        }

        return null
    }

    private fun searchExtendedWayByTree(row: RowIndex, matching: Matching): ExtendingWay? {
        val tree = ExtendingTree(row, adjazenz, matching)
        return tree.extendingWays.firstOrNull()
    }

    // *****************************************************************************************************************
    // utilities for code to look more beautiful

    inline fun ExtendingWay?.found() = this != null
}
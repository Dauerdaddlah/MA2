package de.th.ma2.graph.v2

import de.th.ma2.graph.v2.adjazenz.AdjazenzMatrix
import de.th.ma2.graph.v2.adjazenz.ColIndex
import de.th.ma2.graph.v2.adjazenz.RowIndex
import java.util.*

class ExtendingTree(rootRow: RowIndex, val adjazenz: AdjazenzMatrix, val matching: Matching) {
    val extendingWays: List<ExtendingWay>
    val root: RowNode

    init {
        root = RowNode(rootRow)
        buildTree()
        extendingWays = searchExtendingWays()
    }

    private fun buildTree() {
        val nodes = LinkedList<RowNode>()

        nodes += root

        buildTree@
        while (!nodes.isEmpty()) {
            val node = nodes.removeFirst()

            // add all connections from this rowConnectedTo present in the adjazenzmatrix
            for (connectedCol in adjazenz.row(node.index)) {
                if (!connectedCol.isColAlreadyContained()) {
                    val colNode = ColNode(connectedCol, node)
                    node.subNodes += colNode

                    if(!matching.containsCol(connectedCol)) {
                        // we found an extending way so stop further searching
                        break@buildTree
                    }

                    val connectedRow = matching.rowConnectedTo(connectedCol)

                    if(!connectedRow.isRowAlreadyContained()) {
                        val rowNode = RowNode(connectedRow, colNode)
                        colNode.row = rowNode
                        nodes.addFirst(rowNode)
                    }
                }
            }
        }
    }

    private fun searchExtendingWays(): List<ExtendingWay> {
        val list = mutableListOf<ExtendingWay>()

        for (leaf in leaves(root)) {
            // it is only an extending way if the leaf/end is also not already contained in the matching
            if (!matching.containsCol(leaf.index)) {
                list += buildExtendingWay(leaf)
            }
        }

        return list
    }

    // *****************************************************************************************************************
    // utility class for representing the tree-structure
    // root is a RowNode representing a row
    // each RowNode contains ALL columns connected to it according to the graph (as ColNode)
    // each ColNode contains the row connected to it in the matching (if connected)

    inner class RowNode(val index: RowIndex, val parent: ColNode? = null) {
        val subNodes: MutableList<ColNode> = mutableListOf()
    }

    inner class ColNode(val index: ColIndex, val parent: RowNode) {
        var row: RowNode? = null
    }

    // *****************************************************************************************************************
    // utility functions for the code to look more beautiful
    private fun leaves(rowNode: RowNode): List<ColNode> {
        val list = mutableListOf<ColNode>()

        for (colNode in rowNode.subNodes) {
            when {
                colNode.row.isPresent() ->
                    list.addAll(leaves(colNode.row!!))

                else ->
                    list += colNode
            }
        }

        return list
    }

    private fun RowIndex.isRowAlreadyContained(row: RowNode = root): Boolean {
        return when {
            row.index == this -> true
            else -> row.subNodes.find { col ->
                col.row.ifPresent { row ->
                    isRowAlreadyContained(row)
                } ?: false
            }.isPresent()
        }
    }

    private fun ColIndex.isColAlreadyContained(row: RowNode = root): Boolean {
        return row.subNodes.find { col ->
            col.index == this
                || col.row.ifPresent {
                        row -> isColAlreadyContained(row)
                    } ?: false
        }.isPresent()
    }

    private fun buildExtendingWay(col: ColNode, indizes: MutableList<Int> = mutableListOf()): ExtendingWay {
        indizes.add(0, col.index)
        indizes.add(0, col.parent.index)

        col.parent.parent.ifPresent { parentCol ->
            buildExtendingWay(parentCol, indizes)
        }

        return ExtendingWay(indizes)
    }

    private inline fun <T: Any> T?.isPresent(): Boolean =
            this != null

    private inline fun <T: Any, R> T?.ifPresent(block: ( T ) -> R) =
            this?.let(block)
}
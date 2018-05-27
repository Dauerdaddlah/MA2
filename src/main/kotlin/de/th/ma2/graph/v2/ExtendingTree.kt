package de.th.ma2.graph.v2

import de.th.ma2.graph.v2.adjazenz.AdjazenzMatrix

class ExtendingTree(rootRow: Int, val adjazenz: AdjazenzMatrix, val matching: Matching) {
    val extendingWays: List<ExtendingWay>
    val root: RowNode

    init {
        root = RowNode(rootRow)
        buildTree(root)
        extendingWays = searchExtendingWays()
    }

    private fun buildTree(rowNode: RowNode) {
        buildTreeDepthFirst(rowNode)
        //buildTreeBreadthFirst(rowNode)
    }

    private fun buildTreeDepthFirst(rowNode: RowNode) {
        // add all connections from this rowConnectedTo present in the adjazenzmatrix
        for (connectionCol in adjazenz.row(rowNode.index)) {
            // do not add the 'parent' of this col as it is already contained in the tree
            if (!connectionCol.isAlreadyConnectedTo(rowNode)) {
                val colNode = ColNode(connectionCol, rowNode)
                rowNode.subNodes += colNode

                when {
                    // the col is already in the matching so continue on its current row it is connected to
                    matching.containsCol(colNode.index)
                        && !rowAlreadyContained(matching.rowConnectedTo(colNode.index)) -> {

                        val rowNode = RowNode(matching.rowConnectedTo(colNode.index), colNode)
                        colNode.row = rowNode

                        buildTree(rowNode)
                    }
                }
            }
        }
    }

    private fun buildTreeBreadthFirst(rowNode: RowNode, level: Int = 0): Boolean {
        return when (level) {
            0 -> {
                // starting point
                var l = 1
                while(buildTreeBreadthFirst(rowNode, l)) {
                    l++
                }

                return true
            }
            1 -> {
                var temp = false
                // add all connections from this rowConnectedTo present in the adjazenzmatrix
                for (connectionCol in adjazenz.row(rowNode.index)) {
                    // do not add the 'parent' of this col as it is already contained in the tree
                    if (!connectionCol.isAlreadyConnectedTo(rowNode)) {
                        val colNode = ColNode(connectionCol, rowNode)
                        rowNode.subNodes += colNode
                        temp = true

                        when {
                            // the col is already in the matching so continue on its current row it is connected to
                            matching.containsCol(colNode.index) && !rowAlreadyContained(matching.rowConnectedTo(colNode.index)) -> {
                                val rowNode = RowNode(matching.rowConnectedTo(colNode.index), colNode)
                                colNode.row = rowNode
                            }
                        }
                    }
                }
                return temp
            }
            else -> {
                var temp = false
                for (colNode in rowNode.subNodes) {
                    if (colNode.row != null) {
                        if (buildTreeBreadthFirst(colNode.row!!, level - 1)) {
                            temp = true
                        }
                    }
                }
                return temp
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

    inner class RowNode(val index: Int, val parent: ColNode? = null) {
        val subNodes: MutableList<ColNode> = mutableListOf()
    }

    inner class ColNode(val index: Int, val parent: RowNode) {
        var row: RowNode? = null
    }

    // *****************************************************************************************************************
    // utility functions for the code to look more beautiful
    private fun leaves(rowNode: RowNode): List<ColNode> {
        val list = mutableListOf<ColNode>()

        for (colNode in rowNode.subNodes) {
            when {
                colNode.row != null ->
                    list.addAll(leaves(colNode.row!!))

                else ->
                    list += colNode
            }
        }

        return list
    }

    private fun rowAlreadyContained(row: Int, rowNode: RowNode = root): Boolean {
        return when {
            rowNode.index == row -> true
            else -> rowNode.subNodes.find { it.row != null && rowAlreadyContained(row, it.row!!) } != null
        }
    }

    private fun Int.isAlreadyConnectedTo(rowNode: ExtendingTree.RowNode): Boolean {
        return this == rowNode?.parent?.index
    }

    private fun buildExtendingWay(col: ColNode, way: ExtendingWay = ExtendingWay()): ExtendingWay {
        way.indizes.add(0, col.index)
        way.indizes.add(0, col.parent.index)
        if(col.parent.parent != null) {
            buildExtendingWay(col.parent.parent!!, way)
        }

        return way
    }
}

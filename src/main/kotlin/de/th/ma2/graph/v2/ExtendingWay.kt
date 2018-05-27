package de.th.ma2.graph.v2

import de.th.ma2.graph.v2.adjazenz.ColIndex
import de.th.ma2.graph.v2.adjazenz.RowIndex
import java.util.*

// *********************************************************************************************************************
// completely ugly but needed utility class
// it's just functional not anything more
// just pretend it's an extending way and a matching will process it correctly

class ExtendingWay(indizes: List<Int>): Iterable<ExtendingWay.PathElement> {
    constructor(vararg indizes: Int) : this(indizes.toList())

    private val indizes: List<Int> = indizes.toList()

    override fun iterator(): Iterator<PathElement> {
        return object: Iterator<PathElement> {
            var index = 1
            var row = false

            override fun hasNext(): Boolean {
                return index < indizes.size
            }

            override fun next(): PathElement {
                index++
                row = !row

                return PathElement(index - 1, !row)
            }
        }
    }

    inner class PathElement constructor(private val index: Int, private val row: Boolean) {
        fun isRowElement() = row
        fun isColElement() = !row

        val previousIndex: Int = indizes[index - 1]
        val curIndex: Int = indizes[index]

        val rowIndex: RowIndex = if(isRowElement()) curIndex else previousIndex
        val colIndex: ColIndex = if(isColElement()) curIndex else previousIndex
    }
}
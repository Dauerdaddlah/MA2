package de.th.ma2.graph.v2

import de.th.ma2.graph.v2.adjazenz.AdjazenzMatrix
import de.th.ma2.graph.v2.adjazenz.AdjazenzMatrixImpl

fun main(args: Array<String>) {
    val a: AdjazenzMatrix = AdjazenzMatrixImpl()
    for(row in 'A' .. 'H') {
        a.addRow(row.toString())
    }
    for (col in 'a' .. 'h') {
        a.addCol(col.toString())
    }

    for (p in listOf(
            "A" to "aefg",
            "B" to "bch",
            "C" to "cefg",
            "D" to "dh",
            "E" to "bcd",
            "F" to "bd",
            "G" to "abcd",
            "H" to "bcd"))
    {
        for (col in p.second.chars()) {
            a[p.first][col.toChar().toString()] = true
        }
    }

    val b = GraphBipartit(a)

    val m = b.findMatching()

    System.out.println(a)
    System.out.println(m.adjazenz)
}
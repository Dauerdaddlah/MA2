package de.th.ma2.graph.v2

// *********************************************************************************************************************
// completely ugly but needed utility class
// it's just functional not anything more
// just pretend it's an extending way and a matching will process it correctly

class ExtendingWay() {
    val indizes = mutableListOf<Int>()

    constructor(index1: Int, index2: Int) : this() {
        indizes += index1
        indizes += index2
    }
}
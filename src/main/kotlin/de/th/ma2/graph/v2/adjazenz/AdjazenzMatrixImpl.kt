package de.th.ma2.graph.v2.adjazenz


class AdjazenzMatrixImpl: AdjazenzMatrix {
    private val _rowNames = mutableListOf<String>()
    override val rowNames: List<String> = _rowNames
    private val _colNames = mutableListOf<String>()
    override val colNames: List<String> = _colNames

    override val rows: Iterable<Row>
        get() = object: Iterable<Row> {
            override fun iterator(): Iterator<Row> {
                return object: Iterator<Row> {
                    private var index = -1

                    override fun next(): Row {
                        ++index
                        return row(index)
                    }

                    override fun hasNext(): Boolean {
                        return index < rowNames.size - 1
                    }

                }
            }
        }

    override val columns: Iterable<Column>
        get() = object: Iterable<Column> {
            override fun iterator(): Iterator<Column> {
                return object: Iterator<Column> {
                    private var index = -1

                    override fun next(): Column {
                        ++index
                        return column(index)
                    }

                    override fun hasNext(): Boolean {
                        return index < colNames.size - 1
                    }

                }
            }
        }

    private val data = mutableListOf<MutableList<Boolean>>()

    override fun toString(): String {
        val s = StringBuilder()
        s.append(" ")
        colNames.forEach { s.append(" $it") }
        s.append(System.lineSeparator())

        rowNames.forEach { row ->
            s.append(row)
            colNames.forEach { col ->
                s.append(" ").append(if (this[row][col]) "1" else "0")
            }
            s.append(System.lineSeparator())
        }

        return s.toString()
    }

    override fun addRow(name: String) {
        _rowNames += name
        val newList = mutableListOf<Boolean>()
        for(i in colNames) {
            newList += false
        }
        data += newList
    }

    override fun removeRow(row: RowIndex) {
        data.removeAt(row)
        _rowNames.removeAt(row)
    }

    override fun addCol(name: String) {
        _colNames += name
        for(row in data) {
            row += false
        }
    }

    override fun removeCol(col: ColIndex) {
        for(row in data) {
            row.removeAt(col)
        }
        _colNames.removeAt(col)
    }

    override fun row(row: RowIndex) = RowImpl(row)

    override fun column(col: ColIndex) = ColumnImpl(col)

    abstract inner class MatrixElement
        protected constructor(override val index: Int): Iterable<Int>, Row, Column {

        override fun contains(col: ColIndex): Boolean = this[col]
        override fun get(colName: String): Boolean = this[colName.toIndex()]
        override fun set(colName: String, value: Boolean) = set(colName.toIndex(), value)
        override fun contains(colName: String): Boolean = this[colName]
        override fun isPresent(): Boolean = indexNames.indices.find { this[it] } != null
        override fun String.toIndex(): Int = indexNames.indexOf(this)

        protected abstract val indexNames: List<String>

        override fun iterator() = object: Iterator<Int> {
            private var nextIndex: Int

            init {
                nextIndex = calcNext()
            }

            override fun hasNext() = nextIndex < indexNames.size
            override fun next(): Int {
                val temp = nextIndex
                nextIndex = calcNext(nextIndex)
                return temp
            }

            private fun calcNext(cur: Int = -1): Int {
                var ret = cur
                while(++ret < indexNames.size && !this@MatrixElement[ret]);

                return ret
            }
        }
    }

    // class not save to structural changes (adding or removing of rows)
    inner class RowImpl(index: RowIndex): MatrixElement(index), Row, Iterable<ColIndex> {
        override operator fun get(col: ColIndex) = data[index][col]
        override fun set(col: ColIndex, value: Boolean) { data[index][col] = value }
        override val indexNames = colNames
    }

    // class not save to structural changes (adding or removing of columns)
    inner class ColumnImpl(index: ColIndex): MatrixElement(index), Column, Iterable<RowIndex> {
        override operator fun get(row: RowIndex) = data[row][index]
        override fun set(row: RowIndex, value: Boolean) { data[row][index] = value }
        override val indexNames = rowNames
    }
}
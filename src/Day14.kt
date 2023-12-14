private const val A_BILLION = 1_000_000_000

fun main() {
    fun part1(input: List<String>): Int {
        return Matrix(input)
            .rollNorth()
            .northLoad()
    }

    fun part2(input: List<String>): Int {
        val matrix = Matrix(input)
        val cycleMap: MutableMap<Int, String> = mutableMapOf(0 to matrix.toString())
        var cycleIndex = 0
        var cycleLength = 0
        for (index in 1..A_BILLION) {
            matrix.cycle()
            if (cycleMap.containsValue(matrix.toString())) {
                cycleIndex = cycleMap.filterValues { it == matrix.toString() }.keys.single()
                cycleLength = index - cycleIndex
                break
            } else {
                cycleMap[index] = matrix.toString()
            }
        }

        val pointInCycle = (A_BILLION - cycleIndex) % cycleLength
        val actualIndex = cycleIndex + pointInCycle
        return cycleMap[actualIndex]!!.split("\n")
            .let(::Matrix)
            .northLoad()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput).also(::println) == 136)
    val testInput2 = readInput("Day14_test")
    check(part2(testInput2).also(::println) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}

data class Matrix(val matrix: MutableList<CharArray>) {

    fun cycle(cycles: Int = 1): Matrix = apply {
        repeat(cycles) { rollNorth().rollWest().rollSouth().rollEast() }
    }

    fun rollNorth(): Matrix = apply {
        matrix.first().indices
            .forEach { columnIndex ->
                // Form the column
                matrix.indices
                    .joinToString("") { rowIndex ->
                        matrix[rowIndex][columnIndex].toString()
                    }.split('#')
                    .joinToString("#") {
                        it.toCharArray()
                            .sortedDescending()
                            .joinToString("")
                    }
                    // Roll the column
                    .forEachIndexed { rowIndex, c ->
                        matrix[rowIndex][columnIndex] = c
                    }
            }
    }

    fun rollSouth(): Matrix = apply {
        matrix.first().indices
            .forEach { columnIndex ->
                // Form the column
                matrix.indices
                    .joinToString("") { rowIndex ->
                        matrix[rowIndex][columnIndex].toString()
                    }.split('#')
                    .joinToString("#") {
                        it.toCharArray()
                            .sorted()
                            .joinToString("")
                    }
                    // Roll the column
                    .forEachIndexed { rowIndex, c ->
                        matrix[rowIndex][columnIndex] = c
                    }
            }
    }

    fun rollWest(): Matrix = apply {
        matrix.forEachIndexed { rowIndex, row ->
            matrix[rowIndex] = row.joinToString("")
                .split('#')
                .joinToString("#") {
                    it.toCharArray()
                        .sortedDescending()
                        .joinToString("")
                }.toCharArray()
        }
    }

    fun rollEast(): Matrix = apply {
        matrix.forEachIndexed { rowIndex, row ->
            matrix[rowIndex] = row.joinToString("")
                .split('#')
                .joinToString("#") {
                    it.toCharArray()
                        .sorted()
                        .joinToString("")
                }.toCharArray()
        }
    }

    fun northLoad(): Int = matrix.first().indices
        .sumOf { columnIndex ->
            matrix.indices
                .sumOf { rowIndex ->
                    if (matrix[rowIndex][columnIndex] == 'O') matrix.size - rowIndex else 0
                }
        }

    override fun toString(): String {
        return matrix.joinToString("\n") { line -> line.joinToString("") }
    }
}

fun Matrix(input: List<String>): Matrix = input
    .map(String::toCharArray)
    .toMutableList()
    .let(::Matrix)

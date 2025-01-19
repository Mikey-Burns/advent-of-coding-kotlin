package year2024.day04

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input.findXmas()

    fun part2(input: List<String>): Long = input.findMas()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test", "2024")
    check(part1(testInput) == 18L)
    val testInput2 = readInput("Day04_test", "2024")
    check(part2(testInput2).also(::println) == 9L)

    val input = readInput("Day04", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.findXmas(): Long {
    return this.indices.sumOf { lineNumber ->
        this[lineNumber].indices.sumOf { columnNumber ->
            if (this[lineNumber][columnNumber] == 'X') {
                listOf(
                    // Up
                    listOf(
                        lineNumber - 1 to columnNumber,
                        lineNumber - 2 to columnNumber,
                        lineNumber - 3 to columnNumber
                    ),
                    // Up Right
                    listOf(
                        lineNumber - 1 to columnNumber + 1,
                        lineNumber - 2 to columnNumber + 2,
                        lineNumber - 3 to columnNumber + 3
                    ),
                    // Right
                    listOf(
                        lineNumber to columnNumber + 1,
                        lineNumber to columnNumber + 2,
                        lineNumber to columnNumber + 3
                    ),
                    // Down Right
                    listOf(
                        lineNumber + 1 to columnNumber + 1,
                        lineNumber + 2 to columnNumber + 2,
                        lineNumber + 3 to columnNumber + 3
                    ),
                    // Down
                    listOf(
                        lineNumber + 1 to columnNumber,
                        lineNumber + 2 to columnNumber,
                        lineNumber + 3 to columnNumber
                    ),
                    // Down Left
                    listOf(
                        lineNumber + 1 to columnNumber - 1,
                        lineNumber + 2 to columnNumber - 2,
                        lineNumber + 3 to columnNumber - 3
                    ),
                    // Left
                    listOf(
                        lineNumber to columnNumber - 1,
                        lineNumber to columnNumber - 2,
                        lineNumber to columnNumber - 3
                    ),
                    // Up Left
                    listOf(
                        lineNumber - 1 to columnNumber - 1,
                        lineNumber - 2 to columnNumber - 2,
                        lineNumber - 3 to columnNumber - 3
                    )
                )
                    // Still in the grid
                    .filter { coordinates -> coordinates.all { (line, column) -> line in 0..this.lastIndex && column in 0..this.first().lastIndex } }
                    .count { coordinates ->
                        val (mLine, mColumn) = coordinates[0]
                        val (aLine, aColumn) = coordinates[1]
                        val (sLine, sColumn) = coordinates[2]
                        this[mLine][mColumn] == 'M' && this[aLine][aColumn] == 'A' && this[sLine][sColumn] == 'S'
                    }
                    .toLong()
            } else {
                0L
            }
        }
    }
}

private fun List<String>.findMas(): Long {
    return this.indices.sumOf { lineNumber ->
        this[lineNumber].indices.filter { columnNumber ->
            this[lineNumber][columnNumber] == 'A'
        }
            .count { columnNumber ->
                // M then S Locations
                listOf(
                    // Top
                    listOf(
                        lineNumber - 1 to columnNumber - 1,
                        lineNumber - 1 to columnNumber + 1,
                        lineNumber + 1 to columnNumber + 1,
                        lineNumber + 1 to columnNumber - 1,
                    ),
                    // Right
                    listOf(
                        lineNumber - 1 to columnNumber + 1,
                        lineNumber + 1 to columnNumber + 1,
                        lineNumber + 1 to columnNumber - 1,
                        lineNumber - 1 to columnNumber - 1,
                    ),
                    // Bottom
                    listOf(
                        lineNumber + 1 to columnNumber - 1,
                        lineNumber + 1 to columnNumber + 1,
                        lineNumber - 1 to columnNumber - 1,
                        lineNumber - 1 to columnNumber + 1,
                    ),
                    // Left
                    listOf(
                        lineNumber - 1 to columnNumber - 1,
                        lineNumber + 1 to columnNumber - 1,
                        lineNumber - 1 to columnNumber + 1,
                        lineNumber + 1 to columnNumber + 1,
                    )
                )
                    // Still in the grid
                    .filter { coordinates -> coordinates.all { (line, column) -> line in 0..this.lastIndex && column in 0..this.first().lastIndex } }
                    .any { coordinates ->
                        val (m1Line, m1Column) = coordinates[0]
                        val (m2Line, m2Column) = coordinates[1]
                        val (s1Line, s1Column) = coordinates[2]
                        val (s2Line, s2Column) = coordinates[3]

                        this[m1Line][m1Column] == 'M' && this[m2Line][m2Column] == 'M' &&
                                this[s1Line][s1Column] == 'S' && this[s2Line][s2Column] == 'S'
                    }

            }
            .toLong()
    }
}
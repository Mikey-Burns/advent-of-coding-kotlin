package year2025.day04

import utils.Point2D
import utils.allNeighbors
import utils.get
import utils.isInBounds
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = PaperGrid(input).accessibleRolls()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test", "2025")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day04_test", "2025")
    check(part2(testInput2) == 0L)

    val input = readInput("Day04", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private data class PaperGrid(val grid: List<List<Char>>) {
    fun accessibleRolls(): Int {
        return grid.indices.sumOf { column ->
            grid[column].indices
                .map { row -> Point2D(row, column) }
                .filter { point -> grid[point] == '@' }
                .count { point ->
                    point.allNeighbors()
                        .filter { grid.isInBounds(it) }
                        .count { grid[it] == '@' } < 4
                }
        }
    }
}

private fun PaperGrid(input: List<String>): PaperGrid = PaperGrid(input.map { line -> line.toList() })
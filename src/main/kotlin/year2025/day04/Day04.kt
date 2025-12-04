package year2025.day04

import utils.Point2D
import utils.allNeighbors
import utils.get
import utils.isInBounds
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.accessibleRolls().size

    fun part2(input: List<String>): Int = input.cleanupMaxPaper().size

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test", "2025")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day04_test", "2025")
    check(part2(testInput2) == 43)

    val input = readInput("Day04", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.accessibleRolls(cleanedPaper: List<Point2D> = emptyList()): List<Point2D> {
    val grid = this.map<String, List<Char>>(String::toList)
    return grid.indices.flatMap { column ->
        grid[column].indices
            .map { row -> Point2D(row, column) }
            .filter { point -> point !in cleanedPaper }
            .filter { point -> grid[point] == '@' }
            .filter { point ->
                point.allNeighbors()
                    .filter { grid.isInBounds(it) }
                    .filter { it !in cleanedPaper }
                    .count { grid[it] == '@' } < 4
            }
    }
}

private fun List<String>.cleanupMaxPaper(cleaned: List<Point2D> = emptyList()): List<Point2D> {
    val newCleaned = this.accessibleRolls(cleaned)
    if (newCleaned.isEmpty()) return cleaned
    return cleanupMaxPaper(newCleaned + cleaned)
}
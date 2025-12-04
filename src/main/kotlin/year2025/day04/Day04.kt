package year2025.day04

import utils.Point2D
import utils.allNeighbors
import utils.get
import utils.isInBounds
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.toGrid().accessibleRolls().size

    fun part2(input: List<String>): Int = input.toGrid().cleanupMaxPaper().size

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test", "2025")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day04_test", "2025")
    check(part2(testInput2) == 43)

    val input = readInput("Day04", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.toGrid(): List<List<Char>> = this.map(String::toList)

private fun List<List<Char>>.accessibleRolls(): List<Point2D> =
    this.indices.flatMap { column ->
        this[column].indices
            .map { row -> Point2D(row, column) }
            .filter { point -> this[point] == '@' }
            .filter { point ->
                point.allNeighbors()
                    .filter { this.isInBounds(it) }
                    .count { this[it] == '@' } < 4
            }
    }

private fun List<List<Char>>.cleanupMaxPaper(): List<Point2D> =
    this.indices.flatMap { column ->
        this[column].indices
            .map { row -> Point2D(row, column) }
            .filter { point -> this[point] == '@' }
    }
        .let { paper -> cleanupMaxPaper(this.first().indices, this.indices, paper) }


private fun cleanupMaxPaper(
    xRange: IntRange,
    yRange: IntRange,
    paper: List<Point2D>,
    accessible: List<Point2D> = emptyList()
): List<Point2D> = paper.filter { point ->
    point.allNeighbors()
        .count { it in paper } < 4
}
    .let { filtered ->
        if (filtered.isEmpty()) accessible
        else cleanupMaxPaper(xRange, yRange, paper - filtered.toSet(), accessible + filtered)
    }
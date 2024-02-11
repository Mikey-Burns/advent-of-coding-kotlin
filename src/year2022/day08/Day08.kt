package year2022.day08

import get
import println
import readInput
import utils.Point2D
import utils.plus

fun main() {
    fun part1(input: List<String>): Long {
        return input.parseInput().countVisibleTrees()
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test", "2022")
    check(part1(testInput).also(::println) == 21L)
    val testInput2 = readInput("Day08_test", "2022")
    check(part2(testInput2).also(::println) == 0L)

    val input = readInput("Day08", "2022")
    part1(input).println()
    part2(input).println()
}

private data class Forest(val grid: List<List<Int>>) {

    fun countVisibleTrees(): Long = grid.indices.sumOf { y ->
        grid.first().indices.count { x ->
            Point2D(x, y).isVisible()
        }
    }
        .toLong()

    fun Point2D.isVisible(): Boolean =
        (visibleFromTop() || visibleFromBottom() || visibleFromLeft() || visibleFromRight())

    private fun Point2D.visibleFromTop(): Boolean {
        if (y == 0) return true
        val height = grid[this]
        return (1..y)
            .map { this + Point2D(0, -it) }
            .all { grid[it] < height }
    }

    private fun Point2D.visibleFromBottom(): Boolean {
        if (y == grid.lastIndex) return true
        val height = grid[this]
        return (1..grid.lastIndex - y)
            .map { this + Point2D(0, it) }
            .all { grid[it] < height }
    }

    private fun Point2D.visibleFromLeft(): Boolean {
        if (x == 0) return true
        val height = grid[this]
        return (1..x)
            .map { this + Point2D(-it, 0) }
            .all { grid[it] < height }
    }

    private fun Point2D.visibleFromRight(): Boolean {
        if (x == grid.first().lastIndex) return true
        val height = grid[this]
        return (1..grid.first().lastIndex - x)
            .map { this + Point2D(it, 0) }
            .all { grid[it] < height }
    }
}

private fun List<String>.parseInput(): Forest = Forest(this.map { it.map(Char::digitToInt) })
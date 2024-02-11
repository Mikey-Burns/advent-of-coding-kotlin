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
        return input.parseInput().maxScenicScore()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test", "2022")
    check(part1(testInput).also(::println) == 21L)
    val testInput2 = readInput("Day08_test", "2022")
    check(part2(testInput2).also(::println) == 8L)

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

    fun maxScenicScore(): Long = grid.indices.maxOf { y ->
        grid.first().indices.maxOf { x ->
            Point2D(x, y).scenicScore()
        }
    }

    fun Point2D.isVisible(): Boolean =
        (visibleFromTop() || visibleFromBottom() || visibleFromLeft() || visibleFromRight())

    fun Point2D.scenicScore(): Long {
        val scenicUp = scenicUp()
        val scenicDown = scenicDown()
        val scenicLeft = scenicLeft()
        val scenicRight = scenicRight()
        return scenicUp * scenicDown * scenicLeft * scenicRight
    }

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

    private fun Point2D.scenicUp(): Long {
        if (y == 0) return 0L
        val height = grid[this]
        val neighbors = (1..y)
            .map { this + Point2D(0, -it) }
            .takeWhile { grid[it] < height }
        return (if (neighbors.size == y) neighbors.size else neighbors.size + 1).toLong()
    }

    private fun Point2D.scenicDown(): Long {
        if (y == grid.lastIndex) return 0L
        val height = grid[this]
        val neighbors = (1..grid.lastIndex - y)
            .map { this + Point2D(0, it) }
            .takeWhile { grid[it] < height }
        return (if (neighbors.size == grid.lastIndex - y) neighbors.size else neighbors.size + 1).toLong()
    }

    private fun Point2D.scenicLeft(): Long {
        if (x == 0) return 0L
        val height = grid[this]
        val neighbors =  (1..x)
            .map { this + Point2D(-it, 0) }
            .takeWhile { grid[it] < height }
        return (if (neighbors.size == x) neighbors.size else neighbors.size + 1).toLong()
    }

    private fun Point2D.scenicRight(): Long {
        if (x == grid.first().lastIndex) return 0L
        val height = grid[this]
        val neighbors =  (1..grid.first().lastIndex - x)
            .map { this + Point2D(it, 0) }
            .takeWhile { grid[it] < height }
        return (if (neighbors.size == grid.first().lastIndex - x) neighbors.size else neighbors.size + 1).toLong()
    }
}

private fun List<String>.parseInput(): Forest = Forest(this.map { it.map(Char::digitToInt) })
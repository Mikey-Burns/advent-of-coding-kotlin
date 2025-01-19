package year2022.day08

import utils.get
import utils.println
import utils.readInput
import utils.Point2D

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

    private fun Point2D.isVisible(): Boolean = grid[this].let { height ->
        listOf(up(), down(), left(), right())
            .any { list -> list.all { grid[it] < height } }
    }

    fun maxScenicScore(): Long = grid.indices.maxOf { y ->
        grid.first().indices.maxOf { x ->
            Point2D(x, y).scenicScore()
        }
    }

    private fun Point2D.scenicScore(): Long = grid[this].let { height ->
        listOf(up(), down(), left(), right())
            .map { list -> (list.takeWhile { grid[it] < height }.count() + 1).coerceAtMost(list.size) }
            .reduce { a, b -> a * b }
            .toLong()
    }

    private fun Point2D.up(): List<Point2D> = (y - 1 downTo 0).map { Point2D(x, it) }
    private fun Point2D.down(): List<Point2D> = (y + 1..grid.lastIndex).map { Point2D(x, it) }
    private fun Point2D.left(): List<Point2D> = (x - 1 downTo 0).map { Point2D(it, y) }
    private fun Point2D.right(): List<Point2D> = (x + 1..grid.first().lastIndex).map { Point2D(it, y) }
}

private fun List<String>.parseInput(): Forest = Forest(this.map { it.map(Char::digitToInt) })
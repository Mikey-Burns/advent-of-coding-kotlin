package year2025.day09

import utils.println
import utils.readInput
import utils.size
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = input.redTiles().areas().maxOf { it.third }

    fun part2(input: List<String>): Long = TileGrid(input.redTiles()).maximumSquareSize()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test", "2025")
    check(part1(testInput) == 50L)
    val testInput2 = readInput("Day09_test", "2025")
    check(part2(testInput2) == 24L)

    val input = readInput("Day09", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.redTiles(): List<Tile> = map {
    val (x, y) = it.split(",")
    x.toLong() to y.toLong()
}

private fun List<Tile>.areas(): List<Triple<Tile, Tile, Long>> = flatMapIndexed { index, pair ->
    this.drop(index + 1).map { other -> Triple(pair, other, pair.area(other)) }
}

private fun List<Tile>.rectangles(): List<Rectangle> = flatMapIndexed { index, pair ->
    this.drop(index + 1).map { other ->
        val xRange = (min(pair.first, other.first)..max(pair.first, other.first))
        val yRange = (min(pair.second, other.second)..max(pair.second, other.second))
        Rectangle(xRange, yRange)
    }
}

private fun Tile.area(other: Tile): Long = abs((first - other.first + 1) * (second - other.second + 1))

private data class TileGrid(val redTiles: List<Tile>) {

    fun myMaxSize(): Long {
        val edges = buildSet {
            (redTiles + redTiles.first()).zipWithNext().forEach { (start, dest) ->
                if (start.first == dest.first) {
                    (min(start.second, dest.second)..max(start.second, dest.second))
                        .forEach { y ->
                            add(start.first to y)
                        }
                } else {
                    (min(start.first, dest.first)..max(start.first, dest.first))
                        .forEach { x ->
                            add(x to start.second)
                        }
                }
            }
        }

        fun Tile.innerRectangle(other: Tile): Set<Tile> = buildSet {
            val minX = min(first, other.first) + 1
            val maxX = max(first, other.first) - 1
            val minY = min(second, other.second) + 1
            val maxY = max(second, other.second) - 1

            (minX..maxX).forEach { x ->
                add(x to minY)
                add(x to maxY)
            }
            (minY..maxY).forEach { y ->
                add(minX to y)
                add(maxX to y)
            }
        }

        return redTiles.areas()
            .sortedByDescending { it.third }
            .first { (start, dest, area) ->
                start.innerRectangle(dest).none { innerEdge -> innerEdge in edges}
            }
            .third
    }

    fun maximumSquareSize(): Long {

        val edges =
            (redTiles + redTiles.first()).zipWithNext().map { (start, dest) ->
                val xRange = (min(start.first, dest.first)..max(start.first, dest.first))
                val yRange = (min(start.second, dest.second)..max(start.second, dest.second))

                Rectangle(xRange, yRange)
            }

        val rectangles = (redTiles + redTiles.first()).rectangles()
            .sortedByDescending { it.area }

        val bigRectangle = rectangles
            .first {
                val innerRectangle = it.copy(
                    xRange = (it.xRange.first + 1)..<it.xRange.last,
                    yRange = (it.yRange.first + 1)..<it.yRange.last
                )

                edges.none { edge -> edge.overlaps(innerRectangle) }
            }
        return bigRectangle.area
    }
}


typealias Tile = Pair<Long, Long>

private data class Rectangle(val xRange: LongRange, val yRange: LongRange) {

    val area = xRange.size() * yRange.size()

    fun overlaps(other: Rectangle): Boolean {
        val xOverlaps = max(xRange.first, other.xRange.first) <= min(xRange.last, other.xRange.last)
        val yOverlaps = max(yRange.first, other.yRange.first) <= min(yRange.last, other.yRange.last)

        return xOverlaps && yOverlaps
    }
}
package year2025.day09

import utils.Direction
import utils.overlaps
import utils.println
import utils.readInput
import utils.size
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = input.redTiles().rectangles().maxOf { it.area }

    fun part2(input: List<String>): Long = input.redTiles().maximumSquareSize()

    fun part2Shell(input: List<String>): Long = input.redTiles().maximumSquareSizeShell()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test", "2025")
    check(part1(testInput) == 50L)
    val testInput2 = readInput("Day09_test", "2025")
    check(part2(testInput2) == 24L)
    check(part2Shell(testInput2) == 24L)

    val input = readInput("Day09", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
    measureTime { part2Shell(input).println() }.println()
}

/**
 * Convert the input lines into coordinate pairs.
 * Use Tile instead of Point2D because we will need Long instead of Int.
 */
private fun List<String>.redTiles(): List<Tile> = map {
    val (x, y) = it.split(",")
    x.toLong() to y.toLong()
}

/**
 * Convert the Tiles into all possible rectangles.
 */
private fun List<Tile>.rectangles(): List<Rectangle> = flatMapIndexed { index, pair ->
    this.drop(index + 1)
        .map { other -> Rectangle(pair, other) }
}

/**
 * Find the maximum square size that fits inside the outer shape.
 */
private fun List<Tile>.maximumSquareSize(): Long {
    // Find the edges of the shape and represent them as very narrow rectangles
    val edges = (this + first()).zipWithNext().map { (start, dest) -> Rectangle(start, dest) }
    // Find all possible rectangles and sort them by descending area,
    return rectangles().sortedByDescending { it.area }
        .first { rectangle ->
            // Find the inner rectangle of our candidate rectangle
            val innerRectangle = rectangle.innerRectangle()
            // If the inner rectangle never touches an edge,
            // then the outer rectangle never crosses an edge, so it is valid
            edges.none { edge -> edge.overlaps(innerRectangle) }
        }
        .area
}

private fun List<Tile>.maximumSquareSizeShell(): Long {
    // Create an external shell around the edges
    val shell = (this + take(3)).windowed(4) { (previous, start, destination, forTurning) ->
        // To match the example pictures, x and y increase as we go down right
        fun Tile.directionTo(other: Tile): Direction = when {
            first < other.first -> Direction.RIGHT
            first > other.first -> Direction.LEFT
            second > other.second -> Direction.UP
            else -> Direction.DOWN
        }

        val previousDirection = previous.directionTo(start)
        val firstDirection = start.directionTo(destination)
        val secondDirection = destination.directionTo(forTurning)

        val rectangle = when (firstDirection) {
            Direction.UP -> {
                // If we're going up, the shell is to our left
                val x = destination.first - 1
                // Check where we came from to find appropriate max y
                val endY = if (previousDirection == Direction.RIGHT) {
                    // Inner corner, so start one short
                    start.second - 1
                } else {
                    // Outer corner, start in line
                    start.second
                }
                // Check where we are going to find appropriate min y
                val startY = if (secondDirection == Direction.LEFT) {
                    // If it turns left, stop one short
                    destination.second + 1
                } else {
                    // If it turns right, go one further
                    destination.second - 1
                }
                Rectangle(x..x, startY..endY)
            }

            Direction.DOWN -> {
                // If we're going down, the shell is to our right
                val x = destination.first + 1
                // Check where we came from to find appropriate min y
                val startY = if (previousDirection == Direction.LEFT) {
                    // Inner corner, so start one further
                    start.second + 1
                } else {
                    // Outer corner, start in line
                    start.second
                }
                // Check where we are going to find appropriate max y
                val endY = if (secondDirection == Direction.LEFT) {
                    // If it turns left, go one further
                    destination.second + 1
                } else {
                    // If it turns right, stop one short
                    destination.second - 1
                }
                Rectangle(x..x, startY..endY)
            }

            Direction.LEFT -> {
                // If we're going left, the shell is below us
                val y = start.second + 1
                // Check where we came form to find appropriate max x
                val endX = if (previousDirection == Direction.UP) {
                    // Inner corner, start one short
                    start.first - 1
                } else {
                    // Outer corner, start in line
                    start.first
                }
                // Check where we are going to find appropriate min x
                val startX = if (secondDirection == Direction.UP) {
                    // If it turns up, go one further
                    destination.first - 1
                } else {
                    // If it turns down, stop one short
                    destination.first + 1
                }
                Rectangle(startX..endX, y..y)
            }

            Direction.RIGHT -> {
                // If we're going right, the shell is above us
                val y = start.second - 1
                // Check where we came from to find appropriate min x
                val startX = if (previousDirection == Direction.DOWN) {
                    // Inner corner, start one further
                    start.first + 1
                } else {
                    // Outer corner, start in line
                    start.first
                }
                // Check where we are going to find appropriate max x
                val endX = if (secondDirection == Direction.UP) {
                    // If it turns up, stop one short
                    destination.first - 1
                } else {
                    // If it turns down, go one further
                    destination.first + 1
                }
                Rectangle(startX..endX, y..y)
            }
        }

        rectangle
    }
    // Find all possible rectangles and sort them by descending area,
    val rectangles = rectangles().sortedByDescending { it.area }
    return rectangles
        .first { rectangle ->
            // Find the first rectangle that does not touch the shell
            shell.none { edge -> edge.overlaps(rectangle) }
        }
        .area
}

typealias Tile = Pair<Long, Long>

/**
 * Create a rectangle from a pair of points
 */
private fun Rectangle(start: Tile, end: Tile): Rectangle = Rectangle(
    min(start.first, end.first)..max(start.first, end.first),
    min(start.second, end.second)..max(start.second, end.second)
)

/**
 * Data class for a rectangle, consisting of the range of x and y coordinates
 * that it comprises
 */
private data class Rectangle(val xRange: LongRange, val yRange: LongRange) {

    val area = xRange.size() * yRange.size()

    /**
     * Find a smaller rectangle that is shorter by one on all sides
     */
    fun innerRectangle(): Rectangle = copy(
        xRange = (xRange.first + 1)..<xRange.last,
        yRange = (yRange.first + 1)..<yRange.last
    )

    /**
     * Two rectangles overlap if their x and y ranges both overlap.
     */
    fun overlaps(other: Rectangle): Boolean = xRange.overlaps(other.xRange) && yRange.overlaps(other.yRange)
}
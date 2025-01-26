package utils

import kotlin.math.abs

typealias Location = Pair<Int, Int>

// Orthogonal
fun Location.up(): Location = first - 1 to second
fun Location.down(): Location = first + 1 to second
fun Location.left(): Location = first to second - 1
fun Location.right(): Location = first to second + 1

// Diagonal
fun Location.upLeft(): Location = first - 1 to second - 1
fun Location.upRight(): Location = first - 1 to second + 1
fun Location.downLeft(): Location = first + 1 to second - 1
fun Location.downRight(): Location = first + 1 to second + 1

fun Location.allNeighbors(): List<Location> = listOf(
    up(), upRight(), right(), downRight(), down(), downLeft(), left(), upLeft()
)

fun Location.step(direction: Direction) = when (direction) {
    Direction.UP -> up()
    Direction.DOWN -> down()
    Direction.LEFT -> left()
    Direction.RIGHT -> right()
}

fun List<String>.findLocationOfChar(char: Char): Location = this.indexOfFirst { line -> line.contains(char) }
    .let { rowNumber -> rowNumber to this[rowNumber].indexOf(char) }

fun Location.distance(destination: Location): Int =
    abs(first - destination.first) + abs(second - destination.second)
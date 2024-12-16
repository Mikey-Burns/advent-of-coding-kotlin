package year2024.utils

import year2024.utils.Direction.*

typealias Location = Pair<Int, Int>

fun Location.up(): Location = first - 1 to second
fun Location.down(): Location = first + 1 to second
fun Location.left(): Location = first to second - 1
fun Location.right(): Location = first to second + 1

fun Location.step(direction: Direction) = when (direction) {
    UP -> up()
    DOWN -> down()
    LEFT -> left()
    RIGHT -> right()
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}
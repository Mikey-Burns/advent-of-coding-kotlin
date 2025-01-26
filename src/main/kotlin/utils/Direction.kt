package utils

import utils.Direction.*

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun Direction.turnRight(): Direction = when(this) {
    UP -> RIGHT
    RIGHT -> DOWN
    DOWN -> LEFT
    LEFT -> UP
}

fun Direction.turnLeft(): Direction = when(this) {
    UP -> LEFT
    RIGHT -> UP
    DOWN -> RIGHT
    LEFT -> DOWN
}
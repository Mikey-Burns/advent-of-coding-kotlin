package year2016.day02

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): String = input.toNumpad()

    fun part2(input: List<String>): String = input.toFancyNumpad()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test", "2016")
    check(part1(testInput) == "1985")
    val testInput2 = readInput("Day02_test", "2016")
    check(part2(testInput2) == "5DB3")

    val input = readInput("Day02", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.toNumpad(): String {
    var position = 1 to 1
    var result = ""
    this.forEach { line ->
        line.forEach { direction ->
            position = when (direction) {
                'U' -> position.copy(second = (position.second - 1).coerceAtLeast(0))
                'R' -> position.copy(first = (position.first + 1).coerceAtMost(2))
                'D' -> position.copy(second = (position.second + 1).coerceAtMost(2))
                'L' -> position.copy(first = (position.first - 1).coerceAtLeast(0))
                else -> error("Bad direction!")
            }
        }
        result += position.toNumpadButton()
    }
    return result
}

private fun Pair<Int, Int>.toNumpadButton(): Int = when (this) {
    0 to 0 -> 1
    1 to 0 -> 2
    2 to 0 -> 3
    0 to 1 -> 4
    1 to 1 -> 5
    2 to 1 -> 6
    0 to 2 -> 7
    1 to 2 -> 8
    2 to 2 -> 9
    else -> error("Off the pad!")
}

private fun List<String>.toFancyNumpad(): String {
    var position = 0 to 2
    var result = ""
    this.forEach { line ->
        line.forEach { direction ->
            position = when (direction) {
                'U' -> when (position.first) {
                    0, 4 -> position
                    1, 3 -> position.copy(second = (position.second - 1).coerceIn(1, 3))
                    2 -> position.copy(second = (position.second - 1).coerceIn(0, 4))
                    else -> error("Off the grid")
                }

                'R' -> when (position.second) {
                    0, 4 -> position
                    1, 3 -> position.copy(first = (position.first + 1).coerceIn(1, 3))
                    2 -> position.copy(first = (position.first + 1).coerceIn(0, 4))
                    else -> error("Off the grid")
                }

                'D' -> when (position.first) {
                    0, 4 -> position
                    1, 3 -> position.copy(second = (position.second + 1).coerceIn(1, 3))
                    2 -> position.copy(second = (position.second + 1).coerceIn(0, 4))
                    else -> error("Off the grid")
                }

                'L' -> when (position.second) {
                    0, 4 -> position
                    1, 3 -> position.copy(first = (position.first - 1).coerceIn(1, 3))
                    2 -> position.copy(first = (position.first - 1).coerceIn(0, 4))
                    else -> error("Off the grid")
                }

                else -> error("Bad direction!")
            }
        }
        result += position.toFancyNumpadButton()
    }
    return result
}

private fun Pair<Int, Int>.toFancyNumpadButton(): Char = when (this) {
    2 to 0 -> '1'
    1 to 1 -> '2'
    2 to 1 -> '3'
    3 to 1 -> '4'
    0 to 2 -> '5'
    1 to 2 -> '6'
    2 to 2 -> '7'
    3 to 2 -> '8'
    4 to 2 -> '9'
    1 to 3 -> 'A'
    2 to 3 -> 'B'
    3 to 3 -> 'C'
    2 to 4 -> 'D'
    else -> error("Off the pad!")
}
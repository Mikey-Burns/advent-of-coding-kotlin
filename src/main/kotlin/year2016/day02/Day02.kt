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

/**
 * Find the code for an arbitrary numpad.
 *
 * @param startingPosition Location of the 5 to start at.
 * @param getButton Mapping function from position to numpad button.
 * @param nextButton Mapping function to find the next button given a direction to move.
 */
private fun List<String>.numpadCode(
    startingPosition: Pair<Int, Int>,
    getButton: (Pair<Int, Int>) -> Char,
    nextButton: (Pair<Int, Int>, Char) -> Pair<Int, Int>
): String {
    var position = startingPosition
    var result = ""
    this.forEach { line ->
        line.forEach { direction ->
            position = nextButton(position, direction)
        }
        result += getButton(position)
    }
    return result
}

/**
 * Use the square numpad to find the code.
 */
private fun List<String>.toNumpad(): String = numpadCode(
    1 to 1,
    ::squareNumpad
) { position, direction ->
    when (direction) {
        'U' -> position.copy(second = (position.second - 1).coerceAtLeast(0))
        'R' -> position.copy(first = (position.first + 1).coerceAtMost(2))
        'D' -> position.copy(second = (position.second + 1).coerceAtMost(2))
        'L' -> position.copy(first = (position.first - 1).coerceAtLeast(0))
        else -> error("Bad direction!")
    }
}

/**
 * Map a position to the square numpad.
 * Top left is (0, 0)
 */
private fun squareNumpad(position: Pair<Int, Int>): Char = when (position) {
    0 to 0 -> '1'
    1 to 0 -> '2'
    2 to 0 -> '3'
    0 to 1 -> '4'
    1 to 1 -> '5'
    2 to 1 -> '6'
    0 to 2 -> '7'
    1 to 2 -> '8'
    2 to 2 -> '9'
    else -> error("Off the pad!")
}

/**
 * Use the fancy numpad to find the code
 */
private fun List<String>.toFancyNumpad(): String = numpadCode(
    0 to 2,
    ::fancyNumpad
) { position, direction ->
    when (direction) {
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

/**
 * Map a position to the fancy numpad.
 * Top left is (0, 0)
 */
private fun fancyNumpad(position: Pair<Int, Int>): Char = when (position) {
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
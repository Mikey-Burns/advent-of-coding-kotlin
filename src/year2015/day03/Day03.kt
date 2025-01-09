package year2015.day03

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { directions ->
        val visited = mutableSetOf(0 to 0)
        directions.fold(0 to 0) { location, direction ->
            location.step(direction).also(visited::add)
        }
        visited.size
    }

    fun part2(input: List<String>): Int = input.sumOf { directions ->
        val visited = mutableSetOf(0 to 0)
        directions.chunked(2).fold(Pair(0 to 0, 0 to 0)) { (santa, robot), steps ->
            val newSanta = santa.step(steps[0]).also(visited::add)
            val newRobot = robot.step(steps[1]).also(visited::add)

            newSanta to newRobot
        }

        visited.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test", "2015")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day03_test2", "2015")
    check(part2(testInput2) == 11)

    val input = readInput("Day03", "2015")
    part1(input).println()
    part2(input).println()
}

private fun Pair<Int, Int>.step(direction: Char): Pair<Int, Int> = when (direction) {
    '^' -> copy(second = second + 1)
    'v' -> copy(second = second - 1)
    '>' -> copy(first = first + 1)
    '<' -> copy(first = first - 1)
    else -> error("Bad direction")
}
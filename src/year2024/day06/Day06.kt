package year2024.day06

import println
import readInput
import year2024.day06.Direction.*

fun main() {
    fun part1(input: List<String>): Long = tracePath(input).size.toLong()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test", "2024")
    check(part1(testInput) == 41L)
    val testInput2 = readInput("Day06_test", "2024")
    check(part2(testInput2) == 0L)

    val input = readInput("Day06", "2024")
    part1(input).println()
    part2(input).println()
}

private fun tracePath(input: List<String>): Set<Pair<Int, Int>> {
    var lineNumber = input.indexOfFirst { it.contains('^') }
    var charNumber = input[lineNumber].indexOf('^')
    val visited = mutableSetOf(lineNumber to charNumber)
    var direction = UP

    while (true) {
        val candidateLine = when (direction) {
            UP -> lineNumber - 1
            DOWN -> lineNumber + 1
            RIGHT, LEFT -> lineNumber
        }
        val candidateChar = when (direction) {
            LEFT -> charNumber - 1
            RIGHT -> charNumber + 1
            UP, DOWN -> charNumber
        }

        if (candidateLine !in input.indices || candidateChar !in input.first().indices) return visited
        if (input[candidateLine][candidateChar] == '#') {
            direction++
        } else {
            lineNumber = candidateLine
            charNumber = candidateChar
            visited.add(lineNumber to charNumber)
        }
    }
}


private enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    operator fun inc(): Direction = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }
}
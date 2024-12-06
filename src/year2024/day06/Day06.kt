package year2024.day06

import println
import readInput
import year2024.day06.Direction.*

fun main() {
    fun part1(input: List<String>): Long = tracePath(input).size.toLong()

    fun part2(input: List<String>): Long = tryObstacles(input, tracePath(input))

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test", "2024")
    check(part1(testInput) == 41L)
    val testInput2 = readInput("Day06_test", "2024")
    check(part2(testInput2) == 6L)

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

private fun tryObstacles(input: List<String>, path: Set<Pair<Int, Int>>): Long {
    // Drop the starting point
    val startingLine = input.indexOfFirst { it.contains('^') }
    val startingChar = input[startingLine].indexOf('^')
    return path.filterNot { it == startingLine to startingChar }
        .count { (obsLine, obsChar) ->
            var lineNumber = startingLine
            var charNumber = startingChar
            var direction = UP
            val visited = mutableSetOf(Triple(lineNumber, charNumber, direction))

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

                if (candidateLine !in input.indices || candidateChar !in input.first().indices) return@count false
                if (visited.contains(Triple(candidateLine, candidateChar, direction))) return@count true
                when {
                    candidateLine == obsLine && candidateChar == obsChar -> direction++
                    input[candidateLine][candidateChar] == '#' -> direction++
                    else -> {
                        lineNumber = candidateLine
                        charNumber = candidateChar
                        visited.add(Triple(lineNumber, charNumber, direction))
                    }
                }
            }
            error("Cannot reach this")
        }
        .toLong()
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
package year2022.day02

import utils.println
import utils.readInput

private const val LOSS = 0L
private const val DRAW = 3L
private const val WIN = 6L

private const val ROCK = 1L
private const val PAPER = 2L
private const val SCISSORS = 3L

fun main() {
    fun part1(input: List<String>): Long = input.map { Game(it) }.sumOf(Game::scoreByInput)

    fun part2(input: List<String>): Long = input.map { Game(it) }.sumOf(Game::scoreByResult)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test", "2022")
    check(part1(testInput).also(::println) == 15L)
    val testInput2 = readInput("Day02_test", "2022")
    check(part2(testInput2).also(::println) == 12L)

    val input = readInput("Day02", "2022")
    part1(input).println()
    part2(input).println()
}

private data class Game(val opponent: Char, val me: Char) {

    fun scoreByInput(): Long {
        val resultScore = when (opponent) {
            'A' -> {
                when (me) {
                    'X' -> DRAW
                    'Y' -> WIN
                    'Z' -> LOSS
                    else -> 0L
                }
            }

            'B' -> {
                when (me) {
                    'X' -> LOSS
                    'Y' -> DRAW
                    'Z' -> WIN
                    else -> 0L
                }
            }

            'C' -> {
                when (me) {
                    'X' -> WIN
                    'Y' -> LOSS
                    'Z' -> DRAW
                    else -> 0L
                }
            }

            else -> 0L
        }

        val myScore = when (me) {
            'X' -> ROCK
            'Y' -> PAPER
            'Z' -> SCISSORS
            else -> 0L
        }

        return resultScore + myScore
    }

    fun scoreByResult(): Long {
        val resultScore = when (me) {
            'X' -> LOSS
            'Y' -> DRAW
            'Z' -> WIN
            else -> 0L
        }

        val myScore = when (me) {
            'X' -> when (opponent) {
                'A' -> SCISSORS
                'B' -> ROCK
                'C' -> PAPER
                else -> 0L
            }

            'Y' -> when (opponent) {
                'A' -> ROCK
                'B' -> PAPER
                'C' -> SCISSORS
                else -> 0L
            }

            'Z' -> when (opponent) {
                'A' -> PAPER
                'B' -> SCISSORS
                'C' -> ROCK
                else -> 0L
            }

            else -> 0L
        }

        return resultScore + myScore
    }
}

private fun Game(line: String): Game = line.split(" ").map(String::first).let { (opponent, me) -> Game(opponent, me) }
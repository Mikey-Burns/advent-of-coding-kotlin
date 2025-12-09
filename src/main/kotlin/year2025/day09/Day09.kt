package year2025.day09

import utils.println
import utils.readInput
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = input.redTiles().areas().max()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test", "2025")
    check(part1(testInput) == 50L)
    val testInput2 = readInput("Day09_test", "2025")
    check(part2(testInput2) == 24L)

    val input = readInput("Day09", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.redTiles(): List<Pair<Long, Long>> = map {
    val (x, y) = it.split(",")
    x.toLong() to y.toLong()
}

private fun List<Pair<Long, Long>>.areas(): List<Long> = flatMapIndexed { index, pair ->
    this.drop(index + 1).map { other -> pair.area(other) }
}

private fun Pair<Long, Long>.area(other: Pair<Long, Long>): Long = abs((first - other.first + 1) * (second - other.second + 1))
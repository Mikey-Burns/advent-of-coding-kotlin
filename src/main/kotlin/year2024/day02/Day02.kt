package year2024.day02

import utils.println
import utils.readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long = input.toLevels().count(List<Long>::isSafe).toLong()

    fun part2(input: List<String>): Long = input.toLevels().count(List<Long>::isKindaSafe).toLong()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test", "2024")
    check(part1(testInput) == 2L)
    val testInput2 = readInput("Day02_test", "2024")
    check(part2(testInput2) == 4L)

    val input = readInput("Day02", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.toLevels(): List<List<Long>> = this.map { line -> line.split(" ").map { it.toLong() } }

private fun List<Long>.isSafe(): Boolean {
    val differences = this.zipWithNext { l, r -> l - r }
    return (differences.all { it > 0 } || differences.all { it < 0 }) && differences.all { abs(it) in 1..3 }
}

private fun List<Long>.isKindaSafe(): Boolean {
    if (this.isSafe()) return true
    return indices.any { index -> this.toMutableList()
        .apply { removeAt(index) }
        .isSafe()
    }
}
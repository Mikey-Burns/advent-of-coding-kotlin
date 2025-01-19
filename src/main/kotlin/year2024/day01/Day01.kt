package year2024.day01

import utils.println
import utils.readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long = input.separateLists().distances().sum()

    fun part2(input: List<String>): Long = input.separateLists().similarityScore().sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test", "2024")
    check(part1(testInput) == 11L)
    val testInput2 = readInput("Day01_test", "2024")
    check(part2(testInput2) == 31L)

    val input = readInput("Day01", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.separateLists(): Pair<List<Long>, List<Long>> {
    val left = mutableListOf<Long>()
    val right = mutableListOf<Long>()
    this.forEach { line -> line.split("   ")
        .let {
            left.add(it[0].toLong())
            right.add(it[1].toLong())
        }
    }
    left.sort()
    right.sort()
    return left to right
}

private fun Pair<List<Long>, List<Long>>.distances(): List<Long> = this.first.zip(this.second) { l, r -> abs(l - r) }

private fun Pair<List<Long>, List<Long>>.similarityScore(): List<Long> = this.first.map { l ->
    l * this.second.count { r -> l == r }
}
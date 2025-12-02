package year2025.day02

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = input.first().separateRanges().flatMap { it.invalidIds() }.sum()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test", "2025")
    check(part1(listOf("11-22")) == 33L)
    check(part1(testInput) == 1227775554L)
    val testInput2 = readInput("Day02_test", "2025")
    check(part2(testInput2) == 0L)

    val input = readInput("Day02", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun String.separateRanges(): List<LongRange> = this.split(",")
    .map { range ->
        val (start, end) = range.split("-").map { it.toLong() }
        start..end
    }

private fun LongRange.invalidIds(): List<Long> {
    val first = this.first.toString()
    val last = this.last.toString()

    return when {
        first.length == last.length && first.length.isOdd() -> emptyList()
        first.length == last.length -> {
            (first.toLong()..last.toLong())
                .filter { it.isInvalid() }
        }

        else -> {
            (this.first..("".padEnd(first.length, '9').toLong())).invalidIds() +
                    (("1".padEnd(last.length, '0').toLong())..this.last).invalidIds()
        }
    }
}

private fun Long.isInvalid(): Boolean {
    val asString = this.toString()
    if (asString.length.isOdd()) return false
    return asString.take(asString.length / 2) == asString.takeLast(asString.length / 2)
}

private fun Int.isOdd(): Boolean = this % 2 != 0
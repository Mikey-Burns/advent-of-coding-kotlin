package year2025.day02

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long =
        input.first().separateRanges().flatMap { it.invalidIds(::twoHalfInvalidCheck) }.sum()

    fun part2(input: List<String>): Long =
        input.first().separateRanges().flatMap { it.invalidIds(::allSizesInvalidCheck) }.sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test", "2025")
    check(part1(listOf("11-22")) == 33L)
    check(part1(testInput) == 1227775554L)
    val testInput2 = readInput("Day02_test", "2025")
    check(part2(testInput2) == 4174379265L)

    val input = readInput("Day02", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun String.separateRanges(): List<LongRange> = this.split(",")
    .map { range ->
        val (start, end) = range.split("-").map { it.toLong() }
        start..end
    }

private fun LongRange.invalidIds(findInvalidIds: (LongRange) -> List<Long>): List<Long> {
    val first = this.first.toString()
    val last = this.last.toString()

    return if (first.length != last.length) {
        (this.first..("".padEnd(first.length, '9').toLong())).invalidIds(findInvalidIds) +
                (("1".padEnd(last.length, '0').toLong())..this.last).invalidIds(findInvalidIds)
    } else {
        findInvalidIds(this)
    }
}

private fun twoHalfInvalidCheck(range: LongRange): List<Long> {
    val first = range.first.toString()
    val last = range.last.toString()

    return if (first.length == last.length && first.length.isOdd()) emptyList()
    else (range.first..range.last)
        .filter { it.isInvalidHalf() }
}

private fun Long.isInvalidHalf(): Boolean {
    val asString = this.toString()
    if (asString.length.isOdd()) return false
    return asString.take(asString.length / 2) == asString.takeLast(asString.length / 2)
}

private fun allSizesInvalidCheck(range: LongRange): List<Long> {
    val maxChunkSize = range.first.toString().length / 2
    return range.filter { id ->
        (1..maxChunkSize)
            .any { chunkSize ->
                id.toString().chunked(chunkSize).toSet().size == 1
            }
    }
        .toSet()
        .toList()
}

private fun Int.isOdd(): Boolean = this % 2 != 0
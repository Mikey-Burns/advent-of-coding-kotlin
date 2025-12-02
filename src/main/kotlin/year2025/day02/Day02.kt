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

/**
 * Parse the input into a list of LongRanges.
 * Ranges are separated from each other by a comma.
 * Each range is divided by a dash.
 */
private fun String.separateRanges(): List<LongRange> = this.split(",")
    .map { range ->
        val (start, end) = range.split("-").map { it.toLong() }
        start..end
    }

/**
 * Find the invalid IDs in a range.
 * Takes the logic for processing a given range as a parameter.
 *
 * If the start and finish of a range have the same number of digits, apply the logic.
 * If they are not the same, split the range into two ranges and recurse.
 */
private fun LongRange.invalidIds(findInvalidIds: (LongRange) -> Set<Long>): Set<Long> {
    val first = this.first.toString()
    val last = this.last.toString()

    return if (first.length != last.length) {
        (this.first..("".padEnd(first.length, '9').toLong())).invalidIds(findInvalidIds) +
                (("1".padEnd(last.length, '0').toLong())..this.last).invalidIds(findInvalidIds)
    } else {
        findInvalidIds(this)
    }
}

private fun twoHalfInvalidCheck(range: LongRange): Set<Long> {
    val first = range.first.toString()
    val last = range.last.toString()

    return if (first.length == last.length && first.length.isOdd()) emptySet()
    else range.filter { it.isInvalidHalf() }.toSet()
}

private fun Long.isInvalidHalf(): Boolean {
    val asString = this.toString()
    if (asString.length.isOdd()) return false
    return asString.take(asString.length / 2) == asString.takeLast(asString.length / 2)
}

private fun allSizesInvalidCheck(range: LongRange): Set<Long> {
    val maxChunkSize = range.first.toString().length / 2
    return range.filter { id ->
        (1..maxChunkSize)
            .any { chunkSize ->
                id.toString().chunked(chunkSize).toSet().size == 1
            }
    }
        .toSet()
}

private fun Int.isOdd(): Boolean = this % 2 != 0
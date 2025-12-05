package year2016.day20

import utils.println
import utils.readInput
import utils.reduceLongRange
import kotlin.time.measureTime

private const val MAX_IP = 4294967295L

fun main() {
    fun part1(input: List<String>): Long = lowestAllowed(input.toRanges())

    fun part2(input: List<String>): Long = numberAllowed(input.toRanges())

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test", "2016")
    check(part1(testInput) == 3L)

    val input = readInput("Day20", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.toRanges(): List<LongRange> = map { line ->
    val (low, high) = line.split("-").map(String::toLong)
    low..high
}
    .sortedBy { it.first }

private fun lowestAllowed(blockedRanges: List<LongRange>): Long {
    return generateSequence(0L) { it + 1 }
        .first { ip -> blockedRanges.none { range -> ip in range } }
}

private fun numberAllowed(blockedRanges: List<LongRange>): Long {
    val combinedRanges = blockedRanges.reduceLongRange()
    val blockedIps = combinedRanges.sumOf { it.last - it.first + 1 }


    return (MAX_IP + 1) - blockedIps
}
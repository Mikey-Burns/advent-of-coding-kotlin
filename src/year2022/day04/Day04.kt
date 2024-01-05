package year2022.day04

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = input.map(::RangePair).count(RangePair::fullOverlap).toLong()

    fun part2(input: List<String>): Long = input.map(::RangePair).count(RangePair::anyOverlap).toLong()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test", "2022")
    check(part1(testInput).also(::println) == 2L)
    val testInput2 = readInput("Day04_test", "2022")
    check(part2(testInput2).also(::println) == 4L)

    val input = readInput("Day04", "2022")
    part1(input).println()
    part2(input).println()
}

private data class RangePair(val elfOne: LongRange, val elfTwo: LongRange) {

    fun fullOverlap(): Boolean = elfOne.isSubrange(elfTwo) || elfTwo.isSubrange(elfOne)
    fun anyOverlap(): Boolean = elfOne.overlaps(elfTwo) || elfTwo.overlaps(elfOne)
}

private fun LongRange.isSubrange(other: LongRange): Boolean = this.first >= other.first && this.last <= other.last
private fun LongRange.overlaps(other: LongRange): Boolean = this.first in other || this.last in other

private fun RangePair(line: String): RangePair = line.split(",")
    .map { it.substringBefore("-").toLong()..it.substringAfter("-").toLong() }
    .let { (rangeOne, rangeTwo) -> RangePair(rangeOne, rangeTwo) }
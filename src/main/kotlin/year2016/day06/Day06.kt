package year2016.day06

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): String = input.mostCommon()

    fun part2(input: List<String>): String = input.leastCommon()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test", "2016")
    check(part1(testInput) == "easter")
    val testInput2 = readInput("Day06_test", "2016")
    check(part2(testInput2) == "advent")

    val input = readInput("Day06", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.mostCommon(): String = this.first().indices.map { index ->
    this.groupingBy { line -> line[index] }
        .eachCount()
        .maxBy { (_, count) -> count }
        .key
}
    .joinToString("")

private fun List<String>.leastCommon(): String = this.first().indices.map { index ->
    this.groupingBy { line -> line[index] }
        .eachCount()
        .minBy { (_, count) -> count }
        .key
}
    .joinToString("")
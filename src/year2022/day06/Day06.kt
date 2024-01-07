package year2022.day06

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = input.first().indexAfterUniqueSetOfChar(4)

    fun part2(input: List<String>): Long = input.first().indexAfterUniqueSetOfChar(14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test", "2022")
    check(part1(testInput).also(::println) == 7L)
    val testInput2 = readInput("Day06_test", "2022")
    check(part2(testInput2).also(::println) == 19L)

    val input = readInput("Day06", "2022")
    part1(input).println()
    part2(input).println()
}

private fun String.indexAfterUniqueSetOfChar(size: Int): Long = this.windowed(size)
    .indexOfFirst { it.toCharArray().toSet().size == size } + size.toLong()
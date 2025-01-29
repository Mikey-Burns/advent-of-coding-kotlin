package year2015.day04

import utils.md5
import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input[0].findMd5WithLeadingZeros(5)

    fun part2(input: List<String>): Long = input[0].findMd5WithLeadingZeros(6)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test", "2015")
    check(part1(testInput) == 609043L)

    val input = readInput("Day04", "2015")
    part1(input).println()
    part2(input).println()
}

private fun String.findMd5WithLeadingZeros(numZeros: Int):Long {
    val prefix = "0".repeat(numZeros)
    var suffix = 0L
    while (true) {
        if ("$this$suffix".md5().startsWith(prefix)) return suffix
        suffix++
    }
}
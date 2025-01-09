package year2015.day05

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = input.count(String::isNice)

    fun part2(input: List<String>): Int = input.count(String::isStillNice)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test", "2015")
    check(part1(testInput) == 2)
    val testInput2 = readInput("Day05_test2", "2015")
    check(part2(testInput2) == 2)

    val input = readInput("Day05", "2015")
    part1(input).println()
    part2(input).println()
}

private fun String.isNice(): Boolean {
    val vowels = this.count { it in "aeiou" } >= 3
    val doubleLetter = this.zipWithNext().any { it.first == it.second }
    val forbidden = this.contains(Regex("ab|cd|pq|xy"))
    return vowels && doubleLetter && !forbidden
}

private fun String.isStillNice(): Boolean {
    val doubles = this.contains(Regex("(\\w\\w).*\\1"))
    val repeats = this.contains(Regex("(\\w)\\w\\1"))
    return doubles && repeats
}
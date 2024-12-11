package year2024.day11

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = input.first().toStones().processStones(25).size.toLong()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test", "2024")
    check(part1(testInput) == 55312L)
    val testInput2 = readInput("Day11_test", "2024")
    check(part2(testInput2) == 0L)

    val input = readInput("Day11", "2024")
    part1(input).println()
    part2(input).println()
}

private fun String.toStones(): List<Long> = this.split(" ").map(String::toLong)

private fun List<Long>.processStones(iterations: Int): List<Long> {
    var result = this
    repeat(iterations) {
        result = result.flatMap { stone ->
            val stoneString = stone.toString()
            when {
                stone == 0L -> listOf(1L)
                stoneString.length % 2 == 0 -> listOf(
                    stoneString.substring(0, stoneString.length / 2).toLong(),
                    stoneString.substring(stoneString.length / 2).toLong()
                )
                else -> listOf(stone * 2024)
            }
        }
    }
    return result
}
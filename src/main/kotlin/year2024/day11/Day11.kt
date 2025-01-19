package year2024.day11

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input.first().toStones().processStones(25)

    fun part2(input: List<String>): Long = input.first().toStones().processStones(75)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test", "2024")
    check(part1(testInput) == 55312L)
    val testInput2 = readInput("Day11_test", "2024")
    check(part2(testInput2) == 65601038650482L)

    val input = readInput("Day11", "2024")
    part1(input).println()
    part2(input).println()
}

private fun String.toStones(): List<Long> = this.split(" ").map(String::toLong)

private fun List<Long>.processStones(iterations: Int): Long {
    var stoneMap = this.groupingBy { it }.eachCount().mapValues { (_, count) -> count.toLong() }
    repeat(iterations) {
        stoneMap = stoneMap.processStones()
    }
    return stoneMap.values.sum()
}

val mergeCount: (t: Long, u: Long) -> Long = { old, new -> old + new }

private fun Map<Long, Long>.processStones(): Map<Long, Long> {
    val stoneMap = mutableMapOf<Long, Long>()
    this.forEach { (stone, count) ->
        val stoneString = stone.toString()
        when {
            stone == 0L -> stoneMap.merge(1L, count, mergeCount)
            stoneString.length % 2 == 0 -> {
                stoneMap.merge(stoneString.substring(0, stoneString.length / 2).toLong(), count, mergeCount)
                stoneMap.merge(stoneString.substring(stoneString.length / 2).toLong(), count, mergeCount)
            }

            else -> stoneMap.merge(stone * 2024, count, mergeCount)
        }
    }
    return stoneMap
}
package year2024.day19

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long {
        val towels = getTowels(input)
        val combos = getCombos(input)
        return combos.countIsPossible(towels)
    }

    fun part2(input: List<String>): Long {
        val towels = getTowels(input)
        val combos = getCombos(input)
        return combos.countAllPossibilities(towels)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test", "2024")
    check(part1(testInput) == 6L)
    val testInput2 = readInput("Day19_test", "2024")
    check(part2(testInput2) == 16L)

    val input = readInput("Day19", "2024")
    part1(input).println()
    part2(input).println()
}

private fun getTowels(input: List<String>): List<String> = input.first().split(",").map { it.trim() }

private fun getCombos(input: List<String>): List<String> = input.takeLastWhile { it.isNotBlank() }

private fun List<String>.countIsPossible(towels: List<String>): Long = this.populateCacheOfPossibility(towels)
    .values
    .count { it > 0L }
    .toLong()

private fun List<String>.countAllPossibilities(towels: List<String>): Long = this.populateCacheOfPossibility(towels)
    .values
    .sum()

private fun List<String>.populateCacheOfPossibility(towels: List<String>): Map<String, Long> {
    val cache: MutableMap<String, Long> = mutableMapOf()
    fun String.numberOfCombos(towels: List<String>): Long {
        if (this in cache) return cache.getValue(this)
        if (this.isEmpty()) return 1
        return towels.filter { towel -> this.startsWith(towel) }
            .sumOf { towel -> this.substringAfter(towel).numberOfCombos(towels) }
            .also { cache[this] = it }
    }
    this.forEach { combo -> combo.numberOfCombos(towels) }
    return cache.filterKeys { string -> string in this }
}
package year2025.day05

import utils.println
import utils.readInput
import utils.reduceLongRange
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = FoodDatabase(input).countFreshIngredients()

    fun part2(input: List<String>): Long = FoodDatabase(input).possibleFreshIngredients()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test", "2025")
    check(part1(testInput) == 3)
    val testInput2 = readInput("Day05_test", "2025")
    check(part2(testInput2) == 14L)

    val input = readInput("Day05", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private data class FoodDatabase(val freshRanges: List<LongRange>, val ingredients: List<Long>) {

    fun countFreshIngredients(): Int =
        ingredients.count { ingredient -> freshRanges.any { range -> ingredient in range } }

    fun possibleFreshIngredients(): Long {
        val combinedRanges = freshRanges.reduceLongRange()

        // This really should use it.last - it.first + 1, but instead we move the +1 into the initial longCount
        var longCount = combinedRanges.size.toLong()
        combinedRanges.forEach { longCount += it.last - it.first }
        return longCount
    }
}

private fun FoodDatabase(input: List<String>): FoodDatabase {
    val freshRanges = input.takeWhile(String::isNotEmpty)
        .map {
            val (start, end) = it.split("-").map(String::toLong)
            start..end
        }

    val ingredients = input.takeLastWhile(String::isNotEmpty)
        .map(String::toLong)

    return FoodDatabase(freshRanges, ingredients)
}
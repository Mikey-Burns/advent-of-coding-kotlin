package year2025.day03

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { batteryJoltage(it, 2).toInt() }

    fun part2(input: List<String>): Long = input.sumOf { batteryJoltage(it, 12).toLong() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test", "2025")
    check(part1(testInput) == 357)
    val testInput2 = readInput("Day03_test", "2025")
    check(part2(testInput2) == 3121910778619L)

    val input = readInput("Day03", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun batteryJoltage(bank: String, count: Int): String {
    if (count == 0) return ""
    val nextDigit = bank.dropLast(count - 1).maxBy(Char::digitToInt)
    return nextDigit + batteryJoltage(bank.substringAfter(nextDigit), count - 1)
}
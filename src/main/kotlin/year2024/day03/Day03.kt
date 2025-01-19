package year2024.day03

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input.joinToString("").multiplyLine()

    fun part2(input: List<String>): Long = input.joinToString("").multiplyWithDoAndDont()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test", "2024")
    check(part1(testInput) == 161L)
    val testInput2 = readInput("Day03_test2", "2024")
    check(part2(testInput2) == 48L)

    val input = readInput("Day03", "2024")
    part1(input).println()
    part2(input).println()
}

private fun String.dosAndDonts(): Pair<List<Int>, List<Int>> =
    (listOf(0) + Regex("do\\(\\)").findAll(this).map { it.range.last }.toList()) to
            Regex("don't\\(\\)").findAll(this).map { it.range.last }.toList()

private fun String.findMultiplies() = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)").findAll(this)

private fun String.multiplyLine(): Long = findMultiplies()
    .sumOf { it.multiplyMatchResult() }

private fun String.multiplyWithDoAndDont(): Long {
    val (dos, donts) = dosAndDonts()
    return findMultiplies()
        .filter { matchResult ->
            val first = matchResult.range.first
            val minDo = dos.filter { it <= first }.max()
            val minDont = donts.filter { it < first }.maxOrNull()
            minDont == null || minDo > minDont
        }
        .sumOf { it.multiplyMatchResult() }
}

private fun MatchResult.multiplyMatchResult() = groupValues[1].toLong() * groupValues[2].toLong()
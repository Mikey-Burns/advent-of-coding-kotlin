package year2024.day03

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = input.sumOf(::multiplyLine)

    fun part2(input: List<String>): Long = input.joinToString("").let(::multiplyWithDoAndDont)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test", "2024")
    check(part1(testInput) == 161L)
    val testInput2 = readInput("Day03_test2", "2024")
    check(part2(testInput2) == 48L)

    val input = readInput("Day03", "2024")
    part1(input).println()
    part2(input).println()
}

private fun dosAndDonts(line: String): Pair<List<Int>, List<Int>> =
    (listOf(0) + Regex("do\\(\\)").findAll(line).map { it.range.last }.toList()) to
            Regex("don't\\(\\)").findAll(line).map { it.range.last }.toList()

private fun findMultiplies(line: String) = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)").findAll(line)

private fun multiplyLine(line: String): Long = findMultiplies(line)
    .sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }

private fun multiplyWithDoAndDont(line: String): Long {
    val (dos, donts) = dosAndDonts(line)
    return findMultiplies(line)
        .filter { matchResult ->
            val first = matchResult.range.first
            val minDo = dos.filter { it <= first }.max()
            val minDont = donts.filter { it < first }.maxOrNull()
            minDont == null || minDo > minDont
        }
        .sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }
}
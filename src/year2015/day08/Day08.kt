package year2015.day08

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { it.literalMinusMemory() }

    fun part2(input: List<String>): Int = input.sumOf { it.expandedMinusLiteral() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test", "2015")
    check(part1(testInput) == 12)
    val testInput2 = readInput("Day08_test", "2015")
    check(part2(testInput2) == 19)

    val input = readInput("Day08", "2015")
    part1(input).println()
    part2(input).println()
}

private fun String.literalMinusMemory(): Int = this.length - this.memory().length

private fun String.memory(): String = this.trim('"')
    .replace("\\\"", ".")
    .replace("\\\\", "-")
    .replace(Regex("\\\\x\\w\\w"), "_")

private fun String.expandedMinusLiteral(): Int = expanded(this).length - this.length

private fun expanded(input: String): String = buildString {
    append('"')
    for (index in input.indices) {
        when (input[index]) {
            '"' -> append("\\\"")
            '\\' -> append("\\\\")
            else -> append(input[index])
        }
    }
    append('"')
}
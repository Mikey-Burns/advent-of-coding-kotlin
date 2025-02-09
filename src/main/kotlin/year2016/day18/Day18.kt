package year2016.day18

import utils.println
import utils.readInput
import kotlin.time.measureTime

private const val SAFE = '.'
private const val TRAP = '^'

fun main() {
    fun part1(input: List<String>, rows: Int = 40): Int = makeMaze(input[0], rows).safeSpaces()

    fun part2(input: List<String>): Int = makeMaze(input[0], 400000).safeSpaces()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test", "2016")
    check(part1(testInput, 10) == 38)

    val input = readInput("Day18", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun makeMaze(startingLine: String, rows: Int): List<String> = buildList {
    add(startingLine)
    while (size < rows) add(last().nextLine())
}

private fun String.nextLine(): String = indices.map { index ->
    val left = if (index > 0) this[index - 1] else SAFE
    val right = if (index < lastIndex) this[index + 1] else SAFE

    // Simplify the rules by combining 1 with 3, and 2 with 4
    // Then realize that it is a trap if left != right
    if (left != right) TRAP else SAFE
}.joinToString("")

private fun List<String>.safeSpaces(): Int = this.sumOf { line -> line.count { it == SAFE } }
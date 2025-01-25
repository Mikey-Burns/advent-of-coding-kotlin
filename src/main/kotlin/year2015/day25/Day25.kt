package year2015.day25

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = input.coordinates()
        .let { (row, column) -> findCode(row, column) }

    // test if implementation meets criteria from the description, like:
    check(findCode(1, 1) == 20151125L)
    check(findCode(2, 1) == 31916031L)
    check(findCode(1, 2) == 18749137L)
    check(findCode(3, 3) == 1601130L)

    val input = readInput("Day25", "2015")
    measureTime { part1(input).println() }.println()
}

private fun List<String>.coordinates(): Pair<Int, Int> = this.joinToString("")
    .split(" ", ",", ".")
    .mapNotNull { it.toIntOrNull() }
    .let { it.first() to it.last() }

private fun findCode(row: Int, column: Int): Long {
    val diagonal = row + (column - 1)
    val codeNumber = (1..<diagonal).fold(0) { acc, i -> acc + i } + column
    var code = 20151125L
    repeat(codeNumber - 1) {
        code = code * 252533 % 33554393
    }
    return code
}
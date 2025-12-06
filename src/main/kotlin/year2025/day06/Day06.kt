package year2025.day06

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = input.cephalopodMath()

    fun part2(input: List<String>): Long = input.columnMath()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test", "2025")
    check(part1(testInput) == 4277556L)
    val testInput2 = readInput("Day06_test", "2025")
    check(part2(testInput2) == 3263827L)

    val input = readInput("Day06", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.cephalopodMath(): Long {
    val numbers = dropLast(1).map { line -> line.split(" ").filter { it.isNotBlank() }.map(String::toLong) }
    val operators = takeLast(1).flatMap { line -> line.split(" ").filter { it.isNotBlank() } }
    return operators
        .foldIndexed(0L) { index, acc, symbol ->
            acc + if (symbol == "+") {
                numbers.fold(0L) { acc, line -> acc + line[index] }
            } else {
                numbers.fold(1L) { acc, line -> acc * line[index] }
            }
        }
}

private fun List<String>.columnMath(): Long {
    var sum = 0L
    val lastIndex = this.maxOf(String::lastIndex)
    val numbers = mutableListOf<Long>()
    val numberRows = this.dropLast(1).map { line -> line.padEnd(lastIndex + 1, ' ') }
    val operatorRow = last().padEnd(this.maxOf(String::length), ' ')
    (lastIndex downTo 0).forEach { index ->
        numberRows.fold("") { acc, line -> acc + line[index] }
            .trim()
            .toLongOrNull()
            ?.also(numbers::add)

        val operator = operatorRow[index]
        if (operator != ' ') {
            sum += if (operator == '+') {
                numbers.reduce(Long::plus)
            } else {
                numbers.reduce(Long::times)
            }
            numbers.clear()
        }
    }

    return sum
}
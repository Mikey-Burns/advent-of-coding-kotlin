package year2015.day10

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line -> line.iterativeLookAndSay(40).length }

    fun part2(input: List<String>): Int = input.sumOf { line -> line.iterativeLookAndSay(50).length }

    // test if implementation meets criteria from the description, like:
    check("1".iterativeLookAndSay(5).length == 6)

    val input = readInput("Day10", "2015")
    part1(input).println()
    part2(input).println()
}

fun String.iterativeLookAndSay(iterations: Int): String {
    var result = this
    repeat(iterations) {
        result = result.lookAndSay()
    }
    return result
}

fun String.lookAndSay(): String {
    val result = StringBuilder()

    var currentDigit = this.first()
    var count = 0
    for (index in indices) {
        if (this[index] == currentDigit) {
            count++
        } else {
            result.append(count.toString())
            result.append(currentDigit)
            currentDigit = this[index]
            count = 1
        }
    }
    result.append(count.toString())
    result.append(currentDigit)
    return result.toString()
}
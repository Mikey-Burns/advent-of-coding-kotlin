package year2015.day11

import println
import readInput

fun main() {
    fun part1(input: String): String {
        var newPassword = input.increment()
        while (!newPassword.isValidPassword()) {
            newPassword = newPassword.increment()
        }
        return newPassword
    }

    fun part2(input: String): String = part1(part1(input))

    // test if implementation meets criteria from the description, like:
    check(part1("abcdefgh") == "abcdffaa")
    check(part1("ghijklmn") == "ghjaabcc")

    val input = readInput("Day11", "2015").joinToString("")
    part1(input).println()
    part2(input).println()
}

private fun String.isValidPassword(): Boolean {
    val threeInARow = this.windowed(3) { it.toList() }
        .any { (first, second, third) -> first + 1 == second && first + 2 == third }
    val forbidden = this.contains("[iol]".toRegex())
    val pairs = this.zipWithNext()
        .filter { it.first == it.second }
        .toSet()
        .let { it.size >= 2 }
    return threeInARow && !forbidden && pairs
}

private fun String.increment(): String {
    return if (last() != 'z') {
        // No roll over, so increment the last character
        this.dropLast(1) + last().inc()
    } else {
        // Roll over the last character to a, but run the whole formula on the preceding letters
        this.dropLast(1).increment() + 'a'
    }
}
package year2016.day05

import utils.md5
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): String = input.first().md5WithLeadingZeros(5, 8)

    fun part2(input: List<String>): String = input.first().md5WithLeadingZerosUnordered(5, 8)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test", "2016")
    check(part1(testInput) == "18f47a30")
    val testInput2 = readInput("Day05_test", "2016")
    check(part2(testInput2) == "05ace8e3")

    val input = readInput("Day05", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun String.md5WithLeadingZeros(zeros: Int, hashes: Int): String {
    var counter = 0
    var result = ""
    val prefix = "0".repeat(zeros)
    while (result.length < hashes) {
        val md5 = (this + counter).md5()
        if (md5.startsWith(prefix)) {
            result += md5[zeros]
        }
        counter++
    }
    return result
}

private fun String.md5WithLeadingZerosUnordered(zeros: Int, hashes: Int): String {
    var counter = 0
    val result = CharArray(hashes) { ' ' }
    val prefix = "0".repeat(zeros)
    while (result.any { it == ' ' }) {
        val md5 = (this + counter).md5()
        if (md5.startsWith(prefix)) {
            val position = md5[zeros].digitToIntOrNull() ?: -1
            if (position in 0..<hashes && result[position] == ' ') {
                result[position] = md5[zeros + 1]
            }
        }
        counter++
    }
    return result.joinToString("")
}
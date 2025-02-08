package year2016.day16

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>, length: Int = 272): String = checksum(input[0], length)

    fun part2(input: List<String>): String = checksum(input[0], 35651584)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test", "2016")
    check(part1(testInput, 20) == "01100")

    val input = readInput("Day16", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun checksum(initialState: String, length: Int): String {
    var a = initialState

    while (a.length <= length) {
        val b = a.reversed()
            .replace("0", "2")
            .replace("1", "0")
            .replace("2", "1")
        a = a + "0" + b
    }

    fun String.checksum(): String = this.take(length)
        .chunked(2)
        .joinToString("") { pair -> if (pair[0] == pair[1]) "1" else "0" }

    var checksum = a.checksum()
    while (checksum.length % 2 == 0) {
        checksum = checksum.checksum()
    }
    return checksum
}
package year2016.day09

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.map { it.decompress() }.sumOf { it.length }

    fun part2(input: List<String>): Long = input.sumOf { it.decompressLength() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test", "2016")
    check(part1(testInput) == 6 + 7 + 9 + 11 + 6 + 18)
    val testInput2 = readInput("Day09_test2", "2016")
    check(part2(testInput2) == 9L + 20L + 241920L + 445L)

    val input = readInput("Day09", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun String.decompress(): String {
    var index = 0
    var result = ""
    while (index <= lastIndex) {
        if (this[index] == '(') {
            val marker = this.substring(index + 1).takeWhile { it.isLetterOrDigit() }
            val (length, repeats) = marker
                .split("x")
                .map(String::toInt)
            index += marker.length + 2
            val repeatable = this.substring(index, index + length)
            repeat(repeats) {
                result += repeatable
            }
            index += length
        } else {
            result += this[index]
            index++
        }
    }
    return result
}

private fun String.decompressLength(): Long {
    var index = 0
    var result = 0L
    while (index <= lastIndex) {
        if (this[index] == '(') {
            val marker = this.substring(index + 1).takeWhile { it.isLetterOrDigit() }
            val (length, repeats) = marker
                .split("x")
                .map(String::toInt)
            index += marker.length + 2
            val repeatable = this.substring(index, index + length)
            val repeatLength = repeatable.decompressLength()
            result += repeats * repeatLength
            index += length
        } else {
            result++
            index++
        }
    }
    return result
}
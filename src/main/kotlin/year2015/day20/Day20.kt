package year2015.day20

import utils.println
import utils.readInput


fun main() {
    fun part1(input: List<String>): Int = findElfHouse(input[0].toLong())

    fun part2(input: List<String>): Int = findElfHouseLimited(input[0].toLong())

    // test if implementation meets criteria from the description, like:
    check(findElfHouse(100) == 6)
    check(findElfHouse(300) == 16)

    val input = readInput("Day20", "2015")
    part1(input).println()
    part2(input).println()
}

private fun findElfHouse(presentThreshold: Long): Int = generateSequence(0) { it + 1}
    .map { house ->
        ((1..(house / 2)).filter { house % it == 0 }.sum() + house) * 10
    }
    .indexOfFirst { it >= presentThreshold }

private fun findElfHouseLimited(presentThreshold: Long): Int = generateSequence(0) { it + 1}
    .map { house ->
        ((1..(house / 2)).filter { house % it == 0 && house <= it * 50 }.sum() + house) * 11
    }
    .indexOfFirst { it >= presentThreshold }
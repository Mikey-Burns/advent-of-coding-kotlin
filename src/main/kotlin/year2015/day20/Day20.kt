package year2015.day20

import utils.println
import utils.readInput
import kotlin.time.measureTime


fun main() {
    fun part1(input: List<String>): Int = findElfHouse(input[0].toLong())

    fun part2(input: List<String>): Int = findElfHouseLimited(input[0].toLong())

    // test if implementation meets criteria from the description, like:
    check(findElfHouse(100) == 6)
    check(findElfHouse(300) == 16)

    val input = readInput("Day20", "2015")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun findElfHouse(presentThreshold: Long): Int {
    val maxHouse = (presentThreshold / 10).toInt()
    val houses = MutableList(maxHouse) { 0 }
    for (elf in 1..<maxHouse) {
        for (house in elf..<maxHouse step elf) {
            houses[house] += elf * 10
        }
    }
    return houses.indexOfFirst { it >= presentThreshold }
}

private fun findElfHouseLimited(presentThreshold: Long): Int {
    val maxHouse = (presentThreshold / 11).toInt()
    val houses = MutableList(maxHouse) { 0 }
    for (elf in 1..<maxHouse) {
        for (house in elf..maxHouse.coerceAtMost(elf * 50) step elf) {
            houses[house] += elf * 11
        }
    }
    return houses.indexOfFirst { it >= presentThreshold }
}
package year2016.day13

import utils.Location
import utils.LocationAlgorithms.dijkstra
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>, destination: Location = 31 to 39): Int = shortestPath(input[0].toInt(), destination)

    fun part2(input: List<String>): Int = dijkstra(
        start = 1 to 1,
        range = 0..50,
        isValid = { location -> location.isOpen(input[0].toInt()) }
    )
        .count { (_, value) -> value <= 50 }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test", "2016")
    check(part1(testInput, 7 to 4) == 11)

    val input = readInput("Day13", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

fun Location.isOpen(magicNumber: Int): Boolean =
    ((first * first) + (3 * first) + (2 * first * second) + (second) + (second * second) + magicNumber)
        .toString(2)
        .count { it == '1' }
        .let { ones -> ones % 2 == 0 }

private fun shortestPath(magicNumber: Int, destination: Location): Int {
    val distances = dijkstra(
        start = 1 to 1,
        range = 0..50,
        isValid = { location -> location.isOpen(magicNumber) }
    )
    return distances.getValue(destination)
}

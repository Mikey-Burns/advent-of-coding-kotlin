package year2016.day13

import utils.Location
import utils.cardinalNeighbors
import utils.println
import utils.readInput
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>, destination: Location = 31 to 39): Int = shortestPath(input[0].toInt(), destination)

    fun part2(input: List<String>): Int = dijsktra(input[0].toInt()).count { (_, value) -> value <= 50 }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test", "2016")
    check(part1(testInput, 7 to 4) == 11)

    val input = readInput("Day13", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun shortestPath(magicNumber: Int, destination: Location): Int {
    val distances = dijsktra(magicNumber)
    return distances.getValue(destination)
}

private fun dijsktra(magicNumber: Int): Map<Pair<Int, Int>, Int> {
    fun Location.isOpen(): Boolean =
        ((first * first) + (3 * first) + (2 * first * second) + (second) + (second * second) + magicNumber)
            .toString(2)
            .count { it == '1' }
            .let { ones -> ones % 2 == 0 }

    val arbitraryMax = 50
    val unvisited = buildSet {
        for (x in 0..arbitraryMax) {
            for (y in 0..arbitraryMax) {
                val location = x to y
                if (location.isOpen()) add(location)
            }
        }
    }.toMutableSet()
    val distances = unvisited.associateWith { Int.MAX_VALUE }.toMutableMap()
        .apply { this[1 to 1] = 0 }

    while (unvisited.isNotEmpty()) {
        val currentLocation = unvisited.minBy { distances.getValue(it) }
        unvisited.remove(currentLocation)
        val currentDistance = distances.getValue(currentLocation)
        if (currentDistance == Int.MAX_VALUE) break
        currentLocation.cardinalNeighbors()
            .filter { (x, y) -> x >= 0 && y >= 0 }
            .filter { neighbor -> neighbor.isOpen() }
            .forEach { neighbor ->
                distances[neighbor] = min(currentDistance + 1, distances.getValue(neighbor))
            }
    }
    return distances
}

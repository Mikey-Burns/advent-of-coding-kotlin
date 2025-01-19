package year2024.day18

import utils.println
import utils.readInput
import year2024.utils.*
import kotlin.math.min

fun main() {
    fun part1(input: List<String>, gridSize: Int = 70, numObstacles: Int = 1024): Long =
        pathWithDijkstra(input.placeObstacles(numObstacles), gridSize)
            .getValue(gridSize to gridSize).toLong()

    fun part2(input: List<String>, gridSize: Int = 70): String = findFirstBlocker(input, gridSize)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test", "2024")
    check(part1(testInput, 6, 12) == 22L)
    val testInput2 = readInput("Day18_test", "2024")
    check(part2(testInput2, 6) == "6,1")

    val input = readInput("Day18", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.placeObstacles(numberOfObstacles: Int): List<Location> = this.take(numberOfObstacles)
    .map { line -> line.split(",").map { it.toInt() } }
    .map { (x, y) -> Location(x, y) }

private fun pathWithDijkstra(obstacles: List<Location>, gridSize: Int): Map<Pair<Int, Int>, Int> {
    val range = 0..gridSize
    val unvisited = buildSet {
        for (x in 0..gridSize) {
            for (y in 0..gridSize) {
                val location = x to y
                if (location !in obstacles) add(location)
            }
        }
    }.toMutableSet()
    val distances = unvisited.associateWith { Int.MAX_VALUE }.toMutableMap()
        .apply { this[0 to 0] = 0 }
    while (unvisited.isNotEmpty()) {
        val currentNode = unvisited.minBy { distances.getValue(it) }
        unvisited.remove(currentNode)
        val currentDistance = distances.getValue(currentNode)
        if (currentDistance == Int.MAX_VALUE) break
        with(currentNode) {
            listOf(left(), right(), up(), down())
                .filter { neighbor -> neighbor.first in range && neighbor.second in range }
                .filter { neighbor -> neighbor !in obstacles }
                .forEach { neighbor ->
                    distances[neighbor] = min(distances.getValue(currentNode) + 1, distances.getValue(neighbor))
                }
        }
    }

    return distances
}

private fun findFirstBlocker(input: List<String>, gridSize: Int): String {
    var success = 0
    var failure = input.lastIndex

    while (success + 1 < failure) {
        val candidate = (success + failure) / 2
        val obstacles = input.placeObstacles(candidate)
        val distances = pathWithDijkstra(obstacles, gridSize)
        if (distances.getValue(gridSize to gridSize) != Int.MAX_VALUE) {
            success = candidate
        } else {
            failure = candidate
        }
    }
    // Off by 1 because failure is the number of obstacles, and we need the 0-based index of that line
    return input[failure - 1]
}
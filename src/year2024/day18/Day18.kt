package year2024.day18

import println
import readInput
import year2024.utils.*
import kotlin.math.min

fun main() {
    fun part1(input: List<String>, gridSize: Int = 70, numObstacles: Int = 1024): Long =
        pathWithDijkstra(input.placeObstacles(numObstacles), gridSize)

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test", "2024")
    check(part1(testInput, 6, 12) == 22L)
    val testInput2 = readInput("Day18_test", "2024")
    check(part2(testInput2) == 0L)

    val input = readInput("Day18", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.placeObstacles(numberOfObstacles: Int): List<Location> = this.take(numberOfObstacles)
    .map { line -> line.split(",").map { it.toInt() } }
    .map { (x, y) -> Location(x, y) }

private fun pathWithDijkstra(obstacles: List<Location>, gridSize: Int): Long {
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
        with(currentNode) {
            listOf(left(), right(), up(), down())
                .filter { neighbor -> neighbor.first in range && neighbor.second in range }
                .filter { neighbor -> neighbor !in obstacles }
                .forEach { neighbor ->
                    distances[neighbor] = min(distances.getValue(currentNode) + 1, distances.getValue(neighbor))
                }
        }
        unvisited.remove(currentNode)
    }

    return distances.getValue(gridSize to gridSize).toLong()
}
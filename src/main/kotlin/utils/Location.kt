package utils

import kotlin.math.abs
import kotlin.math.min

typealias Location = Pair<Int, Int>

// Orthogonal
fun Location.up(): Location = first - 1 to second
fun Location.down(): Location = first + 1 to second
fun Location.left(): Location = first to second - 1
fun Location.right(): Location = first to second + 1

// Diagonal
fun Location.upLeft(): Location = first - 1 to second - 1
fun Location.upRight(): Location = first - 1 to second + 1
fun Location.downLeft(): Location = first + 1 to second - 1
fun Location.downRight(): Location = first + 1 to second + 1

fun Location.cardinalNeighbors(): List<Location> = listOf(
    up(), right(), down(), left()
)

fun Location.allNeighbors(): List<Location> = listOf(
    up(), upRight(), right(), downRight(), down(), downLeft(), left(), upLeft()
)

fun Location.step(direction: Direction) = when (direction) {
    Direction.UP -> up()
    Direction.DOWN -> down()
    Direction.LEFT -> left()
    Direction.RIGHT -> right()
}

fun List<String>.findLocationOfChar(char: Char): Location = this.indexOfFirst { line -> line.contains(char) }
    .let { rowNumber -> rowNumber to this[rowNumber].indexOf(char) }

fun Location.distance(destination: Location): Int =
    abs(first - destination.first) + abs(second - destination.second)

object LocationAlgorithms {

    fun dijkstra(
        start: Location,
        range: IntRange,
        isValid: (Location) -> Boolean,
    ): Map<Location, Int> = dijkstra(
        start = start,
        xRange = range,
        yRange = range,
        isValid = isValid,
        cost = { _, _ -> 1 }
    )

    fun dijkstra(
        start: Location,
        xRange: IntRange,
        yRange: IntRange,
        isValid: (Location) -> Boolean,
        cost: (Location, Location) -> Int = { _, _ -> 1 }
    ): Map<Location, Int> {
        val unvisited = buildSet {
            for (x in xRange) {
                for (y in yRange) {
                    val location = x to y
                    if (isValid(location)) add(location)
                }
            }
        }.toMutableSet()
        val distances = unvisited.associateWith { Int.MAX_VALUE }.toMutableMap()
            .apply { this[start] = 0 }

        while (unvisited.isNotEmpty()) {
            val currentLocation = unvisited.minBy { distances.getValue(it) }
            unvisited.remove(currentLocation)
            val currentDistance = distances.getValue(currentLocation)
            if (currentDistance == Int.MAX_VALUE) break
            currentLocation.cardinalNeighbors()
                .filter { (x, y) -> x in xRange && y in yRange }
                .filter { neighbor -> isValid(neighbor) }
                .forEach { neighbor ->
                    val distance = cost(currentLocation, neighbor)
                    distances[neighbor] = min(currentDistance + distance, distances.getValue(neighbor))
                }
        }
        return distances
    }
}
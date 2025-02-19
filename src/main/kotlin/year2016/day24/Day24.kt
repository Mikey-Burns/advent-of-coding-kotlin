package year2016.day24

import utils.Location
import utils.LocationAlgorithms
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = shortestRoute(input)

    fun part2(input: List<String>): Int = shortestRoute(input, true)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test", "2016")
    check(part1(testInput) == 14)
    val testInput2 = readInput("Day24_test", "2016")
    check(part2(testInput2) == 20)

    val input = readInput("Day24", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun shortestRoute(input: List<String>, returnHome: Boolean = false): Int {
    val locations = mutableSetOf<Location>()
    val pointOfInterestMap = mutableMapOf<Int, Location>()
    input.indices.forEach { rowNumber ->
        input[0].indices.forEach { columnNumber ->
            val space = input[rowNumber][columnNumber]
            val location = rowNumber to columnNumber
            when {
                space.isDigit() -> {
                    pointOfInterestMap[space.digitToInt()] = location
                    locations.add(location)
                }

                space == '.' -> locations.add(location)
            }
        }
    }
    val pointsOfInterest = pointOfInterestMap.keys.sorted()

    val dijkstras = buildMap {
        pointOfInterestMap.forEach { (poi, location) ->
            put(
                poi,
                LocationAlgorithms.dijkstra(
                    start = location,
                    xRange = input.indices,
                    yRange = input[0].indices,
                    isValid = { it in locations }
                )
            )
        }
    }

    fun findShortest(currentLocation: Int, remainingPoints: List<Int>): Int {
        if (remainingPoints.isEmpty()) {
            return if (returnHome) {
                val currentDijkstra = dijkstras.getValue(currentLocation)
                val homeBase = pointOfInterestMap.getValue(0)
                currentDijkstra.getValue(homeBase)
            }
            else 0
        }
        return remainingPoints
            .minOf { nextPoint ->
                val currentDijkstra = dijkstras.getValue(currentLocation)
                val nextLocation = pointOfInterestMap.getValue(nextPoint)
                val distanceToNextPoint = currentDijkstra.getValue(nextLocation)
                distanceToNextPoint + findShortest(nextPoint, remainingPoints - nextPoint)
            }
    }

    return findShortest(0, pointsOfInterest - 0)
}
package year2024.day20

import utils.*
import kotlin.math.min

fun main() {
    fun part1(input: List<String>, timeToSave: Int): Int = Racetrack(input)
        .findAllCheats()
        .count { (_, timeSaved) -> timeSaved >= timeToSave }

    fun part2(input: List<String>, timeToSave: Int): Int = Racetrack(input)
        .countTwentySecondCheats(timeToSave)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test", "2024")
    check(part1(testInput, 18) == 5)
    check(part1(testInput, 19) == 5)
    check(part1(testInput, 20) == 5)
    check(part1(testInput, 21) == 4)
    check(part1(testInput, 22) == 4)
    check(part1(testInput, 30) == 4)
    check(part1(testInput, 60) == 1)
    check(part1(testInput, 100) == 0)
    val testInput2 = readInput("Day20_test", "2024")
    check(part2(testInput2, 70) == 41)
    check(part2(testInput2, 71) == 29)
    check(part2(testInput2, 72) == 29)
    check(part2(testInput2, 73) == 7)
    check(part2(testInput2, 76) == 3)
    check(part2(testInput2, 77) == 0)
    check(part2(testInput2, 100) == 0)

    val input = readInput("Day20", "2024")
    part1(input, 100).println()
    part2(input, 100).println()
}

private data class Racetrack(val input: List<String>) {
    private val start: Location = input.findLocationOfChar('S')
    private val rowRange = input.indices
    private val charRange = input[0].indices

    private val obstacles: List<Location> = buildList {
        for (rowNumber in input.indices) {
            for (charNumber in input[0].indices) {
                if (input[rowNumber][charNumber] == '#') add(rowNumber to charNumber)
            }
        }
    }


    private fun dijkstra(): Map<Location, Int> {
        val unvisited = buildSet {
            for (rowNumber in rowRange) {
                for (charNumber in charRange) {
                    val location = rowNumber to charNumber
                    if (location !in obstacles) add(location)
                }
            }
        }.toMutableSet()
        val distances = unvisited.associateWith { Int.MAX_VALUE }.toMutableMap()
            .apply { this[start] = 0 }

        while (unvisited.isNotEmpty()) {
            val currentNode = unvisited.minBy { distances.getValue(it) }
            unvisited.remove(currentNode)
            val currentDistance = distances.getValue(currentNode)
            if (currentDistance == Int.MAX_VALUE) break
            with(currentNode) {
                listOf(left(), right(), up(), down())
                    .filter { (neighborRow, neighborChar) -> neighborRow in rowRange && neighborChar in charRange }
                    .filter { neighbor -> neighbor !in obstacles }
                    .forEach { neighbor ->
                        distances[neighbor] = min(distances.getValue(currentNode) + 1, distances.getValue(neighbor))
                    }
            }
        }

        return distances
    }

    fun findAllCheats(): Map<Pair<Location, Location>, Int> {
        val distances = dijkstra()
        val path = distances.filterValues { it != Int.MAX_VALUE }.keys
        val toMap = path.flatMap { pathLocation ->
            with(pathLocation) {
                listOf(
                    left() to left().left(),
                    right() to right().right(),
                    up() to up().up(),
                    down() to down().down()
                )
                    .filter { (neighbor, destination) -> neighbor in obstacles && destination in path }
                    .map { (_, destination) ->
                        val cheatPath = pathLocation to destination
                        // The time we save is the difference between the two points, minus 2
                        // We subtract 2 from time saved for the steps we take into and out of the cheat space
                        val timeSaved = (distances.getValue(destination) - distances.getValue(pathLocation)) - 2
                        cheatPath to timeSaved
                    }
            }
        }
            .filter { (_, timeSaved) -> timeSaved > 0 }
            .toMap()
        return toMap
    }

    fun countTwentySecondCheats(timeToSave: Int): Int {
        val distances = dijkstra()
        val path = distances.filterValues { it != Int.MAX_VALUE }.keys

        return path.sumOf { startCheat ->
            path.count { endCheat ->
                val distance = startCheat.distance(endCheat)
                val timeSaved = (distances.getValue(endCheat) - distances.getValue(startCheat)) - distance

                distance <= 20 && timeSaved >= timeToSave
            }
        }
    }
}
package year2015.day09

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int = LocationMap.of(input).findShortestTrip()

    fun part2(input: List<String>): Int = LocationMap.of(input).findLongestTrip()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test", "2015")
    check(part1(testInput) == 605)
    val testInput2 = readInput("Day09_test", "2015")
    check(part2(testInput2) == 982)

    val input = readInput("Day09", "2015")
    part1(input).println()
    part2(input).println()
}

private data class LocationMap(
    val possibleTrips: Map<String, List<String>>,
    val tripCosts: Map<Pair<String, String>, Int>
) {
    private val locations = possibleTrips.keys.toSet()

    private fun findAllTrips(): MutableList<List<String>> {
        val fullTrips = mutableListOf<List<String>>()

        fun dfsForTrip(steps: List<String>) {
            if (steps.size == locations.size) {
                fullTrips.add(steps)
                return
            }
            for (nextStep in possibleTrips.getValue(steps.last())) {
                if (nextStep !in steps) dfsForTrip(steps + nextStep)
            }
        }

        locations.forEach { step -> dfsForTrip(listOf(step)) }

        return fullTrips
    }

    fun findShortestTrip(): Int = findAllTrips()
        .minOf { trip -> trip.zipWithNext().sumOf { leg -> tripCosts.getValue(leg) } }

    fun findLongestTrip(): Int = findAllTrips()
        .maxOf { trip -> trip.zipWithNext().sumOf { leg -> tripCosts.getValue(leg) } }

    companion object {
        fun of(input: List<String>): LocationMap {
            val possibleTrips = mutableMapOf<String, List<String>>()
            val tripCosts = mutableMapOf<Pair<String, String>, Int>()
            input.forEach { line ->
                val (start, _, end, _, cost) = line.split(" ")
                possibleTrips[start] = (possibleTrips[start] ?: emptyList()) + end
                possibleTrips[end] = (possibleTrips[end] ?: emptyList()) + start
                tripCosts[start to end] = cost.toInt()
                tripCosts[end to start] = cost.toInt()
            }
            return LocationMap(possibleTrips, tripCosts)
        }
    }
}
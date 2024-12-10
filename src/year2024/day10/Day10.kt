package year2024.day10

import println
import readInput

private typealias Location = Pair<Int, Int>

fun main() {
    fun part1(input: List<String>): Long = input.trailheadsScore()

    fun part2(input: List<String>): Long = input.trailheadsRating()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test", "2024")
    check(part1(testInput) == 36L)
    val testInput2 = readInput("Day10_test", "2024")
    check(part2(testInput2) == 81L)

    val input = readInput("Day10", "2024")
    part1(input).println()
    part2(input).println()
}


private fun List<String>.trailheadsScore() = mapTrailheads().first

private fun List<String>.trailheadsRating() = mapTrailheads().second

private fun List<String>.mapTrailheads(): Pair<Long, Long> {
    val locationToPeakInformation = mutableMapOf<Location, PeakInformation>()

    val valueToLocations = mapOf<Int, MutableSet<Location>>(
        0 to mutableSetOf(),
        1 to mutableSetOf(),
        2 to mutableSetOf(),
        3 to mutableSetOf(),
        4 to mutableSetOf(),
        5 to mutableSetOf(),
        6 to mutableSetOf(),
        7 to mutableSetOf(),
        8 to mutableSetOf(),
        9 to mutableSetOf()
    )
    // Populate our maps
    forEachIndexed { rowNumber, row ->
        row.forEachIndexed { charNumber, c ->
            val value = c.digitToInt()
            val location = rowNumber to charNumber

            locationToPeakInformation[location] = if (value == 9) PeakInformation(location) else PeakInformation()
            valueToLocations.getValue(value).add(location)
        }
    }

    // Calculate scores
    for (value in 8 downTo 0) {
        valueToLocations.getValue(value)
            .forEach { location ->
                location.neighborsInBounds(this)
                    .filter { neighbor -> neighbor in valueToLocations.getValue(value + 1) }
                    // If the neighbor is unreachable from a peak, ignore it
                    .filter { neighbor -> locationToPeakInformation.getValue(neighbor).validPeaks.isNotEmpty() }
                    .forEach { neighbor ->
                        locationToPeakInformation[location] =
                            locationToPeakInformation.getValue(location) + locationToPeakInformation.getValue(neighbor)
                    }

            }
    }

    return valueToLocations.getValue(0)
        .fold(0L to 0L) { (score, rating), trailhead ->
            val peakInformation = locationToPeakInformation.getValue(trailhead)
            score + peakInformation.validPeaks.size to rating + peakInformation.uniquePaths
        }
}

private data class PeakInformation(var uniquePaths: Int, val validPeaks: MutableSet<Location>) {

    operator fun plus(other: PeakInformation): PeakInformation =
        PeakInformation(uniquePaths + other.uniquePaths, validPeaks.apply { addAll(other.validPeaks) })
}

private fun PeakInformation(location: Location): PeakInformation = PeakInformation(1, mutableSetOf(location))
private fun PeakInformation(): PeakInformation = PeakInformation(0, mutableSetOf())

private fun Location.neighborsInBounds(input: List<String>): List<Location> = listOf(
    first - 1 to second,
    first + 1 to second,
    first to second - 1,
    first to second + 1
)
    .filter { it.first in input.indices && it.second in input[0].indices }
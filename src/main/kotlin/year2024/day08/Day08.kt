package year2024.day08

import utils.println
import utils.readInput

private typealias Location = Pair<Int, Int>

fun main() {
    fun part1(input: List<String>): Long = input.findMatchingAntenna()
        .values
        .asSequence()
        .flatMap(List<Location>::makePairs)
        .map(Pair<Location, Location>::travelThrough)
        .filter { location -> location.inBounds(input) }
        .toSet()
        .count()
        .toLong()


    fun part2(input: List<String>): Long =
        // Include any antenna that is not alone
        input.findMatchingAntenna().values
            .asSequence()
            .flatMap(List<Location>::makePairs)
            .flatMap { locationPair -> locationPair.travelThroughRepeated(input) }
            .filter { location -> location.inBounds(input) }
            .toSet()
            .count()
            .toLong()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test", "2024")
    check(part1(testInput) == 14L)
    val testInput2 = readInput("Day08_test", "2024")
    check(part2(testInput2) == 34L)

    val input = readInput("Day08", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.findMatchingAntenna(): Map<Char, List<Location>> {
    val antenna: MutableMap<Char, List<Location>> = mutableMapOf()
    forEachIndexed { rowNumber, line ->
        line.forEachIndexed { charNumber, c ->
            if (c != '.') {
                val list = antenna[c] ?: emptyList()
                antenna[c] = list + listOf(rowNumber to charNumber)
            }
        }
    }
    return antenna
}

private fun List<Location>.makePairs(): List<Pair<Location, Location>> =
    this.flatMap { start ->
        this.filter { start != it }
            .map { start to it }
    }

private fun Pair<Location, Location>.travelThrough(): Location =
    second.first + (second.first - first.first) to second.second + (second.second - first.second)

private fun Pair<Location, Location>.travelThroughRepeated(input: List<String>): List<Location> {
    val deltaFirst = (second.first - first.first)
    val deltaSecond = (second.second - first.second)
    val destinations = mutableListOf(second)
    var destination = (second.first + deltaFirst) to (second.second + deltaSecond)
    while (destination.inBounds(input)) {
        destinations.add(destination)
        destination = (destination.first + deltaFirst) to (destination.second + deltaSecond)
    }
    return destinations
}

private fun Location.inBounds(input: List<String>): Boolean = first in input.indices && second in input.first().indices
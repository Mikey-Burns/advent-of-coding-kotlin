package year2024.day12

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = input.groupByLetter().findPlots(input).sumOf { it.cost() }

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val smallest = """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent().lines()
    check(part1(smallest) == 140L)
    val nested = """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
    """.trimIndent().lines()
    check(part1(nested) == 772L)
    val testInput = readInput("Day12_test", "2024")
    check(part1(testInput) == 1930L)
    val testInput2 = readInput("Day12_test", "2024")
    check(part2(testInput2) == 0L)

    val input = readInput("Day12", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.groupByLetter(): Map<Char, List<Location>> = flatMapIndexed { rowNumber, row ->
    row.indices.map { charNumber -> Location(rowNumber, charNumber) }
}
    .groupBy { this[it.rowNumber][it.charNumber] }

private fun Map<Char, List<Location>>.findPlots(input: List<String>): List<Plot> =
    this.flatMap { (c, locations) ->
        val groups = mutableListOf(mutableListOf<Location>())
        locations.forEach { location ->
            val neighbors = location.validNeighbors(input)
            val groupsToJoin = groups.filter { group -> group.any { neighbor -> neighbor in neighbors } }
            when {
                groupsToJoin.isEmpty() -> groups.add(mutableListOf(location))
                groupsToJoin.size == 1 -> groupsToJoin[0].add(location)
                else -> {
                    groups.removeAll(groupsToJoin)
                    groups.add((groupsToJoin.flatten() + location).toMutableList())
                }
            }
        }
        groups
    }
        .map { Plot(it, input) }

data class Location(val rowNumber: Int, val charNumber: Int) {

    fun validNeighbors(input: List<String>): List<Location> = listOf(
        Location(rowNumber - 1, charNumber),
        Location(rowNumber + 1, charNumber),
        Location(rowNumber, charNumber - 1),
        Location(rowNumber, charNumber + 1)
    )
        .filter { it.rowNumber in input.indices && it.charNumber in input[0].indices }
}

data class Plot(val locations: List<Location>, val input: List<String>) {
    private val area = locations.size

    fun cost(): Long = area * perimeter()

    private fun perimeter(): Long =
        locations.sumOf { location ->
            4 - location.validNeighbors(input)
                .count { neighbor -> neighbor in locations }
        }
            .toLong()

    override fun toString(): String = locations.toString()
}
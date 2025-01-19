package year2024.day12

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input.groupByLetter().findPlots(input).sumOf { it.cost() }

    fun part2(input: List<String>): Long = input.groupByLetter().findPlots(input).sumOf {
        it.bulkCost()
    }

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
    // Part 2
    check(part2(smallest) == 80L)
    check(part2(nested) == 436L)
    val eShape = """
        EEEEE
        EXXXX
        EEEEE
        EXXXX
        EEEEE
    """.trimIndent().lines()
    check(part2(eShape) == 236L)
    val testInput2 = readInput("Day12_test", "2024")
    check(part2(testInput2) == 1206L)

    val input = readInput("Day12", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.groupByLetter(): Map<Char, List<Location>> = flatMapIndexed { rowNumber, row ->
    row.indices.map { charNumber -> Location(rowNumber, charNumber) }
}
    .groupBy { this[it.rowNumber][it.charNumber] }

private fun Map<Char, List<Location>>.findPlots(input: List<String>): List<Plot> =
    this.values.flatMap { locations ->
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
        .filter { it.isNotEmpty() }
        .map { Plot(it, input) }

data class Location(val rowNumber: Int, val charNumber: Int) {

    fun validNeighbors(input: List<String>): List<Location> = listOf(
        up(), down(), left(), right()
    )
        .filter { it.rowNumber in input.indices && it.charNumber in input[0].indices }

    fun up() = Location(rowNumber - 1, charNumber)
    fun down() = Location(rowNumber + 1, charNumber)
    fun right() = Location(rowNumber, charNumber + 1)
    fun left() = Location(rowNumber, charNumber - 1)
    fun upLeft() = Location(rowNumber - 1, charNumber - 1)
    fun upRight() = Location(rowNumber - 1, charNumber + 1)
    fun downLeft() = Location(rowNumber + 1, charNumber - 1)
    fun downRight() = Location(rowNumber + 1, charNumber + 1)
}

data class Plot(val locations: List<Location>, val input: List<String>) {
    private val area = locations.size

    fun cost(): Long = area * perimeter()

    fun bulkCost(): Long = area * sides()

    private fun perimeter(): Long =
        locations.sumOf { location ->
            4 - location.validNeighbors(input)
                .count { neighbor -> neighbor in locations }
        }
            .toLong()

    private fun sides(): Long {
        val sideToCorner = locations.associateWith { location ->
            val neighbors = location.validNeighbors(input)
                .filter { neighbor -> neighbor in locations }

            val up = location.up()
            val down = location.down()
            val left = location.left()
            val right = location.right()
            val upLeft = location.upLeft()
            val upRight = location.upRight()
            val downLeft = location.downLeft()
            val downRight = location.downRight()

            when (neighbors.size) {
                4 -> listOf(upLeft, upRight, downLeft, downRight).count { corner -> corner !in locations }.toLong()
                3 -> {
                    val maybeNeighbors = when {
                        left !in neighbors -> listOf(upRight, downRight)
                        right !in neighbors -> listOf(upLeft, downLeft)
                        up !in neighbors -> listOf(downLeft, downRight)
                        down !in neighbors -> listOf(upLeft, upRight)
                        else -> error("Impossible T")
                    }
                    val corners = 2L - locations.count { it in maybeNeighbors }
                    corners
                }

                2 -> {
                    if (neighbors.containsAll(listOf(up, down)) || neighbors.containsAll(listOf(left, right))) {
                        // Tunnel
                        0L
                    } else {
                        val corner = when {
                            neighbors.containsAll(listOf(left, up)) -> upLeft
                            neighbors.containsAll(listOf(left, down)) -> downLeft
                            neighbors.containsAll(listOf(right, up)) -> upRight
                            neighbors.containsAll(listOf(right, down)) -> downRight
                            else -> error("Impossible corner")
                        }
                        2L - locations.count { it == corner }
                    }
                }

                1 -> 2L
                // No neighbors
                else -> 4L
            }
        }
        return sideToCorner.values.sum()
    }


    override fun toString(): String = locations.toString()
}
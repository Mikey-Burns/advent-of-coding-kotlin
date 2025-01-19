package year2015.day18

import utils.println
import utils.readInput
import year2024.utils.Location
import year2024.utils.allNeighbors

fun main() {
    fun part1(input: List<String>, steps: Int = 100): Int = input.toLightGrid().animate(steps).litCount

    fun part2(input: List<String>, steps: Int = 100): Int = input.toLightGridWithLitCorners().animate(steps).litCount

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test", "2015")
    check(part1(testInput, 4) == 4)
    val testInput2 = readInput("Day18_test", "2015")
    check(part2(testInput2, 5) == 17)

    val input = readInput("Day18", "2015")
    part1(input).println()
    part2(input).println()
}

private data class LightGrid(
    var lightsOn: Set<Location>,
    val range: IntRange,
    val alwaysOn: Set<Location> = emptySet()
) {
    val litCount: Int
        get() = lightsOn.size

    fun animate(steps: Int): LightGrid = apply {
        repeat(steps) {
            val nextLights = alwaysOn.toMutableSet()
            range.forEach { rowIndex ->
                range.forEach { columnIndex ->
                    val location = Location(rowIndex, columnIndex)
                    val litNeighbors = location.allNeighbors()
                        .filter { (row, column) -> row in range && column in range }
                        .count { neighbor -> neighbor in lightsOn }
                    // Stay lit if 2 or 3 neighbors are on
                    if (location in lightsOn && (litNeighbors == 2 || litNeighbors == 3)) nextLights.add(location)
                    // Turn on if 3 neighbors are on
                    if (location !in lightsOn && litNeighbors == 3) nextLights.add(location)
                }
            }
            lightsOn = nextLights
        }
    }
}


private fun List<String>.toLightGrid(alwaysOn: Set<Location> = emptySet()): LightGrid {
    val lightsOn = alwaysOn.toMutableSet()
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, c ->
            if (c == '#') lightsOn.add(rowIndex to columnIndex)
        }
    }
    return LightGrid(lightsOn, this.indices, alwaysOn)
}

private fun List<String>.toLightGridWithLitCorners(): LightGrid {
    val corners = setOf(
        0 to 0,
        0 to this.lastIndex,
        this.lastIndex to 0,
        this.lastIndex to this.lastIndex
    )
    return toLightGrid(corners)
}
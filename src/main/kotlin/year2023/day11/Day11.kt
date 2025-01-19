package year2023.day11

import utils.println
import utils.readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        return Universe(input).totalDistance()
    }

    fun part2(input: List<String>, expansionFactor: Int): Long {
        val universe = Universe(input)
        return universe.totalDistance(expansionFactor)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    val testInput2 = readInput("Day11_test")
    check(part2(testInput2, 1).also(::println) == 374L)
    check(part2(testInput2, 9).also(::println) == 1030L)
    check(part2(testInput2, 99).also(::println) == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input, 999_999).println()
}

private const val EMPTY = '.'
private const val GALAXY = '#'

data class Universe(val rows: List<String>) {
    private val emptyRows = rows.indices.filter { rowIndex ->
        rows[rowIndex].all { it.isEmptySpace() }
    }
        .reversed()

    // Columns with nothing
    private val emptyColumns = rows[0].indices.filter { columnIndex ->
        rows.all { it[columnIndex].isEmptySpace() }
    }
        .reversed()

    private val galaxies = rows.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { columnIndex, c ->
            if (c.isGalaxy()) rowIndex to columnIndex else null
        }
    }

    private fun distances(expansionFactor: Int): Long {
        return galaxies.flatMapIndexed { index, galaxy ->
            if (index == galaxies.lastIndex) emptyList()
            else {
                galaxies.subList(index + 1, galaxies.size)
                    .map { destination -> galaxy to destination }
            }
        }
            .sumOf { (start, destination) ->
                start.distance(destination, emptyRows, emptyColumns, expansionFactor)
            }
    }

    fun totalDistance(expansionFactor: Int = 1): Long {
        val zero = distances(0)
        val one = distances(1)
        val rate = one - zero
        return zero + (rate * expansionFactor)
    }
}

fun Pair<Int, Int>.distance(destination: Pair<Int, Int>): Int =
    abs(first - destination.first) + abs(second - destination.second)

fun Pair<Int, Int>.distance(
    destination: Pair<Int, Int>,
    emptyRows: List<Int>,
    emptyColumns: List<Int>,
    expansionFactor: Int = 1
): Long {
    val emptyRowsPassed = emptyRows
        .count { it in min(first, destination.first)..max(first, destination.first) }
        .toLong()
    val emptyColumnsPassed = emptyColumns
        .count { it in min(second, destination.second)..max(second, destination.second) }
        .toLong()
    val totalExpansions = emptyRowsPassed + emptyColumnsPassed
    val rawDistance = distance(destination).toLong()
    val totalDistance = rawDistance + (totalExpansions * expansionFactor)
    return totalDistance
}

fun Char.isEmptySpace(): Boolean = this == EMPTY
fun Char.isGalaxy(): Boolean = this == GALAXY
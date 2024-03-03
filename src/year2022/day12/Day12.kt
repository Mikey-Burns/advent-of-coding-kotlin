package year2022.day12

import get
import println
import readInput
import utils.Point2D
import utils.isInBounds
import utils.orthogonalNeighbors
import java.util.LinkedList

fun main() {
    fun part1(input: List<String>): Long =
        input.findPath('S', 'E') { source, destination ->
            when {
                source == 'S' -> destination in 'a'..'b'
                destination == 'E' -> source in 'y'..'z'
                else -> destination in 'a'..(source + 1)
            }
        }

    fun part2(input: List<String>): Long =
        input.findPath('E', 'a') { source, destination ->
            when {
                source == 'E' -> destination in 'y'..'z'
                else -> destination in (source - 1)..'z'
            }
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test", "2022")
    check(part1(testInput).also(::println) == 31L)
    val testInput2 = readInput("Day12_test", "2022")
    check(part2(testInput2).also(::println) == 29L)

    val input = readInput("Day12", "2022")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.findPath(startChar: Char, endChar: Char, validStep: (Char, Char) -> Boolean): Long {
    val x = this.indexOfFirst { line -> line.contains(startChar) }
    val y = this[x].indexOf(startChar)
    val start = Point2D(y, x)
    val points = this.map(String::toList)

    val visited = mutableSetOf<Point2D>()
    val toVisit = LinkedList<Pair<Point2D, Long>>()
        .apply { add(start to 0) }

    while (toVisit.isNotEmpty()) {
        val (location, steps) = toVisit.pop()
        val currentChar = points[location]
        if (currentChar == endChar) {
            return steps
        }
        visited += location
        location.orthogonalNeighbors()
            .filter { points.isInBounds(it) }
            .filterNot { it in visited }
            .filterNot { toVisit.map(Pair<Point2D, Long>::first).contains(it) }
            .filter { validStep(currentChar, points[it]) }
            .forEach { toVisit.add(it to steps + 1) }
    }
    return -1L
}
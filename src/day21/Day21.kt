package day21

import Point2D
import get
import isInBounds
import orthogonalNeighbors
import println
import readInput

fun main() {
    fun part1(input: List<String>, steps: Int): Int {
        return Garden(input).possibleEndsAfterSteps(steps)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput, 6).also(::println) == 16)
    val testInput2 = readInput("Day21_test")
    check(part2(testInput2).also(::println) == 0)

    val input = readInput("Day21")
    part1(input, 64).println()
    part2(input).println()
}

private data class Garden(val grid: List<List<Char>>) {
    val start: Point2D = grid.indexOfFirst { list -> list.contains('S') }
        .let { y -> Point2D(grid[y].indexOf('S'), y) }

    fun possibleEndsAfterSteps(maxSteps: Int): Int {
        val minimumStepsTo: MutableMap<Point2D, Int> = mutableMapOf()
        fun takeNextStep(currentPoint: Point2D, currentSteps: Int, maxSteps: Int) {
            minimumStepsTo[currentPoint] = currentSteps
            currentPoint.orthogonalNeighbors()
                .filter { grid.isInBounds(it) }
                .filterNot { grid[it] == '#' }
                .filter { (currentSteps + 2) < (minimumStepsTo[it] ?: (maxSteps + 2)) }
                .forEach { takeNextStep(it, currentSteps + 1, maxSteps) }
        }

        takeNextStep(start, 0, maxSteps)
        return minimumStepsTo.filterValues { it % 2 == 0 }.count()
    }
}

private fun Garden(input: List<String>): Garden = input.map(String::toList).let(::Garden)
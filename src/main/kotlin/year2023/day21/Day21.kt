package year2023.day21

import utils.Point2D
import utils.orthogonalNeighbors
import utils.println
import utils.readInput
import kotlin.collections.set

private const val BIG_STEPPER = 26501365

fun main() {
    fun part1(input: List<String>, steps: Int): Int {
        return Garden(input).possibleEndsAfterSteps(steps)
    }

    fun part2(input: List<String>, steps: Int): Long {
        val garden = Garden(input)
        // Input is a square
        val cycleSize = input.size
        val fullCycles = (steps / cycleSize).toLong()
        val cycleOffset = steps % cycleSize
        val stepMap = garden.minimumStepsMap(cycleOffset + (cycleSize * 2) + 1)

        val (zero, one, two) = (0..2)
            .map { index ->
                stepMap.values
                    .count {
                        val maxSteps = cycleOffset + (index * cycleSize)
                        it <= (cycleOffset + (index * cycleSize)) && it % 2 == maxSteps % 2
                    }
                    .toLong()
            }

        // region Mathematics for quadratic equation
        // zero == zero
        // a + b + zero == one
        // 4a + 2b + zero == two

        // a + b == one - zero
        // b == one - zero - a
        // a == one - zero - b

        // 4a + 2b == two - zero
        // 4a = two - zero - 2b
        // 4a == two - zero - 2(one - zero - a)
        // 4a == two - zero - 2 * one + 2 * zero + 2a
        // 2a == two + zero - (2 * one)
        // a == (two + zero - (2 * one)) / 2

        // 2b == two - zero - 4a
        // 2b == two - zero - 4(one - zero - b)
        // 2b == two - zero - (4 * one) + (4 * zero) + 4b
        // -2b == two + (3 * zero) - (4 * one)
        // b == (two + (3 * zero) - (4 * one)) / -2
        // endregion
        val a = (two + zero - (2 * one)) / 2
        val b = (two + (3 * zero) - (4 * one)) / -2
        val c = zero

        return a * (fullCycles * fullCycles) + b * fullCycles + c
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput, 6).also(::println) == 16)

    val input = readInput("Day21")
    part1(input, 64).println()
    part2(input, BIG_STEPPER).println()
}

private data class Garden(val grid: List<List<Char>>) {
    val start: Point2D = grid.indexOfFirst { list -> list.contains('S') }
        .let { y -> Point2D(grid[y].indexOf('S'), y) }

    fun possibleEndsAfterSteps(maxSteps: Int): Int {
        val minimumStepsTo: MutableMap<Point2D, Int> = minimumStepsMap(maxSteps)
        return minimumStepsTo.values.count { it % 2 == maxSteps % 2 }
    }

    fun minimumStepsMap(maxSteps: Int): MutableMap<Point2D, Int> {
        val minimumStepsTo: MutableMap<Point2D, Int> = mutableMapOf()
        fun takeNextStep(currentPoint: Point2D, currentSteps: Int, maxSteps: Int) {
            minimumStepsTo[currentPoint] = currentSteps
            currentPoint.orthogonalNeighbors()
//                .filter { grid.utils.isInBounds(it) }
                .filterNot { grid[it] == '#' }
                .filter { (currentSteps + 1) < (minimumStepsTo[it] ?: (maxSteps + 1)) }
                .forEach { takeNextStep(it, currentSteps + 1, maxSteps) }
        }

        takeNextStep(start, 0, maxSteps)
        return minimumStepsTo
    }

    // New get that can handle infinite scrolling
    operator fun List<List<Char>>.get(point: Point2D): Char {
        var y = point.y
        while (y < 0) y += this.size
        var x = point.x
        while (x < 0) x += this.first().size
        return this[y % this.size][x % this.first().size]
    }
}

private fun Garden(input: List<String>): Garden = input.map(String::toList).let(::Garden)
package year2023.day17

import utils.Compass
import utils.Compass.*
import utils.Point2D
import utils.isInBounds
import utils.println
import utils.readInput
import java.util.*
import utils.get

fun main() {
    fun part1(input: List<String>): Int {
        return HeatGrid(input).calculateHeatLoss { state, direction ->
            state.steps < 3 || state.direction != direction
        }
    }

    fun part2(input: List<String>): Int {
        return HeatGrid(input).calculateHeatLoss { state, direction ->
            (state.direction == direction && state.steps <= 10)
                    || (state.direction != direction && state.steps in 4..10)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput).also(::println) == 102)
    val testInput2 = readInput("Day17_test")
    check(part2(testInput2).also(::println) == 94)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}

private val directionMap = mapOf(
    NORTH to setOf(NORTH, EAST, WEST),
    SOUTH to setOf(SOUTH, EAST, WEST),
    EAST to setOf(NORTH, SOUTH, EAST),
    WEST to setOf(NORTH, SOUTH, WEST),
)

data class State(val location: Point2D, val direction: Compass, val steps: Int) {
    fun next(nextStep: Compass) = State(
        location.step(nextStep),
        nextStep,
        if (direction == nextStep) steps + 1 else 1
    )
}

data class Work(val state: State, val heatLoss: Int) : Comparable<Work> {
    override fun compareTo(other: Work): Int = heatLoss - other.heatLoss

}

data class HeatGrid(val grid: List<List<Int>>) {
    fun calculateHeatLoss(isValidNextMove: (State, Compass) -> Boolean): Int {
        val end = Point2D(grid.first().lastIndex, grid.lastIndex)
        val visited = mutableSetOf<State>()
        val queue = PriorityQueue<Work>()

        val start = Point2D(0, 0)
        val startingState = State(start, EAST, 0)

        queue.add(Work(startingState, 0))
        visited.add(startingState)

        while (queue.isNotEmpty()) {
            val (current, heatLoss) = queue.poll()
            if (current.location == end) return heatLoss

            directionMap
                .getValue(current.direction)
                .filter { direction -> grid.isInBounds(current.location.step(direction)) }
                .filter { direction -> isValidNextMove(current, direction) }
                .map { direction -> current.next(direction) }
                .filter { state -> state !in visited }
                .forEach { state ->
                    queue.add(Work(state, heatLoss + grid[state.location]))
                    visited.add(state)
                }
        }
        throw IllegalStateException("Should have found a path")
    }
}

fun HeatGrid(input: List<String>): HeatGrid = input.map { it.map(Char::digitToInt) }.let(::HeatGrid)
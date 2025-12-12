package year2025.day10

import utils.println
import utils.readInput
import kotlin.math.pow
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.map { Machine(it) }
        .sumOf { it.minimumPressesForIndicators(it.indicatorLights) }

    fun part2(input: List<String>): Int = input.map { Machine(it) }
        .sumOf { it.minimumPressesForJoltage(it.joltages) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test", "2025")
    check(part1(testInput) == 7)
    val testInput2 = readInput("Day10_test", "2025")
    check(part2(testInput2) == 33)

    val input = readInput("Day10", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private data class Machine(
    val indicatorLights: List<Boolean>,
    val buttons: List<Button>,
    val joltages: List<Int>
) {

    /**
     * Calculate all combinations of button presses where each button is pressed up to one time.
     * Use a binary representation of each button being unpressed or pressed.
     * This is mapped to how many buttons were pressed for convenience.
     *
     * Not pressing a button is a valid option!
     */
    fun combinationsButtonLists(): Map<List<Int>, Int> {
        val buttonsToCost = mutableMapOf<List<Int>, Int>()
        (0..<(2.0.pow(buttons.size).toInt()))
            .map { counter -> counter.toString(2).padStart(buttons.size, '0') }
            .forEach { binary ->
                val binaryList = binary.toList().map { ch -> if (ch == '1') 1 else 0 }
                buttonsToCost.putIfAbsent(binaryList, binaryList.count { it == 1 })
            }

        return buttonsToCost
    }

    /**
     * Helper to convert from a list of button indices
     * to a list of times each button was pressed.
     */
    private fun List<Int>.buttonIndicesToCounts(): List<Int> = MutableList(joltages.size) { 0 }
        .apply {
            this@buttonIndicesToCounts.mapIndexedNotNull { index, binary -> if (binary == 1) buttons[index] else null }
                .forEach { button -> button.lights.forEach { light -> this[light]++ } }
        }

    /**
     * Find the minimum presses to match the boolean flags
     */
    fun minimumPressesForIndicators(onOff: List<Boolean> = indicatorLights): Int {
        return combinationsButtonLists()
            .filter { (buttonIndices, _) -> buttonIndices.buttonIndicesToCounts().map { it % 2 == 1 } == onOff }
            .minOf { (_, cost) -> cost }
    }

    /**
     * Find the minimum number of button presses to get a desired joltage outcome.
     * At each step, identify which button combinations would produce a new joltage
     * goal where the values are all even. Divide those joltages by 2 to simplify,
     * then solve the new problem.  Sum up the minimum solutions for each subproblem,
     * remembering to multiply by 2 wherever we did our simplification.
     * Cache subproblems as we go for efficiency.
     * If we ever have an impossible subproblem, cache Int.MAX_VALUE so it never
     * gets selected as a minimum path.
     */
    fun minimumPressesForJoltage(
        goal: List<Int>,
        cache: MutableMap<List<Int>, Int> = mutableMapOf()
    ): Int {
        if (goal.all { it == 0 }) return 0
        val booleanGoal = goal.map { it % 2 == 1 }

        return cache.getOrPut(goal) {
            combinationsButtonLists()
                .minOf { (binaryList, cost) ->
                    val counts = binaryList.buttonIndicesToCounts()
                    if (counts.map { it % 2 == 1 } != booleanGoal) return@minOf Int.MAX_VALUE

                    // Apply the combination to the goal
                    val undivided = goal.toMutableList().mapIndexed { index, button -> (button - counts[index]) }
                    val newGoal = undivided.map { it / 2 }
                    // If any resulting value is below 0, don't go down this path
                    if (newGoal.any { it < 0 }) Int.MAX_VALUE
                    else {
                        val minimumPresses = minimumPressesForJoltage(newGoal, cache)
                        if (minimumPresses == Int.MAX_VALUE) {
                            Int.MAX_VALUE
                        } else {
                            cost + 2 * minimumPresses
                        }
                    }
                }
        }
    }
}

private fun Machine(line: String): Machine {
    val indicatorLights = line.drop(1).substringBefore("]").map { it == '#' }
    val buttons = line.substringAfter("] ").substringBefore(") {")
        .split(")")
        .map { buttonGroup ->
            buttonGroup.substringAfter('(')
                .split(",")
                .map { it.toInt() }
        }
        .map { Button(it) }
    val joltages = line.substringAfter("{").substringBefore("}")
        .split(",")
        .map { it.toInt() }

    return Machine(indicatorLights, buttons, joltages)
}

private data class Button(val lights: List<Int>)
package year2025.day10

import utils.println
import utils.readInput
import kotlin.math.pow
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.map { Machine(it) }
        .sumOf { it.minimumPresses(it.indicatorLights) }

    fun part2(input: List<String>): Long = input.map { Machine(it) }
        .map { it.combinations() }
        .let { 0L }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test", "2025")
    check(part1(testInput) == 7)
    val testInput2 = readInput("Day10_test", "2025")
    check(part2(testInput2) == 0L)

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
     * Use a binary representation of each button being unpressed or pressed,
     * then calculate how much each counter should be increased based on those button presses.
     */
    fun combinations(): Map<List<Int>, Int> {
        val counts = List(joltages.size) { 0 }
        val costs = mutableMapOf<List<Int>, Int>()
        (1..<(2.0.pow(buttons.size).toInt()))
            .map { counter -> counter.toString(2).padStart(buttons.size, '0') }
            .map { binary ->
                binary.toList()
                    .mapIndexedNotNull { index, ch -> if (ch == '1') buttons[index] else null }
            }
            .forEach { buttonList ->
                val myCounts = counts.toMutableList()
                buttonList.forEach { button -> button.lights.forEach { light -> myCounts[light]++ } }
                costs.putIfAbsent(myCounts.toList(), buttonList.size)
            }

        return costs
    }

    /**
     * Find the minimum presses to match the boolean flags
     */
    fun minimumPresses(onOff: List<Boolean> = indicatorLights): Int {
        return combinations()
            .filter { (buttonCounts, cost) -> buttonCounts.map { it % 2 == 1 } == onOff }
            .minOf { (buttonCounts, cost) -> cost }
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
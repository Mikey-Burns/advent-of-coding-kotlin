package year2025.day10

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.map { Machine(it) }
        .sumOf { it.minimumButtonPresses() }

    fun part2(input: List<String>): Long = 0L

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

    fun minimumButtonPresses(): Int {
        return (1..buttons.size).first { numberOfButtons ->
            findAllCombinations(numberOfButtons)
                .any {
                    buttonsMatch(it)
                }
        }
    }

    private fun findAllCombinations(numberOfButtons: Int): Set<Set<Button>> {
        fun findCombinations(selected: Set<Button>, remaining: Set<Button>, desiredSize: Int): Set<Set<Button>> {
            return if (selected.size == desiredSize) setOf(selected)
            else {
                remaining.flatMap { button ->
                    findCombinations(selected + button, remaining - button, desiredSize)
                }
                    .toSet()
            }
        }

        return findCombinations(emptySet(), buttons.toSet(), numberOfButtons)
    }

    private fun buttonsMatch(buttonsToPress: Set<Button>): Boolean {
        val indexPresses = buttonsToPress.flatMap { button -> button.lights }
            .groupBy { it }
        return indicatorLights.withIndex().all { (index, light) ->
            val presses = indexPresses[index]?.size ?: 0
            val isOn = presses % 2 == 1
            light == isOn
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
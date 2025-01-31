package year2016.day08

import utils.Location
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = Screen().executeInstructions(input).size

    fun part2(input: List<String>): Unit = Screen().print(input)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test", "2016")
    check(part1(testInput) == 6)

    val input = readInput("Day08", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input) }.println()
}

private data class Screen(val width: Int = 50, val height: Int = 6) {

    fun executeInstructions(input: List<String>): Set<Location> {
        val lights = mutableSetOf<Location>()

        input.forEach { instruction ->
            when {
                instruction.startsWith("rect") -> {
                    val (wide, tall) = instruction.substringAfter(" ")
                        .split("x")
                        .map(String::toInt)
                    for (x in 0..<wide) {
                        for (y in 0..<tall) {
                            lights.add(x to y)
                        }
                    }
                }

                instruction.startsWith("rotate row") -> {
                    val (y, offset) = instruction.substringAfter("y=")
                        .split(" ")
                        .mapNotNull(String::toIntOrNull)
                    lights.filter { it.second == y }
                        .also { lights.removeAll(it) }
                        .map { it.copy(first = (it.first + offset) % width) }
                        .also { lights.addAll(it) }
                }

                instruction.startsWith("rotate column") -> {
                    val (x, offset) = instruction.substringAfter("x=")
                        .split(" ")
                        .mapNotNull(String::toIntOrNull)
                    lights.filter { it.first == x }
                        .also { lights.removeAll(it) }
                        .map { it.copy(second = (it.second + offset) % height) }
                        .also { lights.addAll(it) }
                }
            }
        }

        return lights
    }

    fun print(input: List<String>) {
        val lights = executeInstructions(input)

        for (row in 0..<height) {
            buildString {
                for (column in 0..<width) {
                    append(if ((column to row) in lights) '#' else ' ')
                }
            }.println()
        }
    }
}
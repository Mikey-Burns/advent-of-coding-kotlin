package year2015.day06

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int = performInstructions(input.map { Instruction.of(it) })

    fun part2(input: List<String>): Int = performBrightnessInstructions(input.map { Instruction.of(it) })

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test", "2015")
    check(part1(testInput) == 1_000_000 - 1000 - 4)
    val testInput2 = readInput("Day06_test2", "2015")
    check(part2(testInput2) == 1 + 2000000 - 2)

    val input = readInput("Day06", "2015")
    part1(input).println()
    part2(input).println()
}

private enum class Operation {
    ON, OFF, TOGGLE
}

private data class Instruction(val operation: Operation, val xRange: IntRange, val yRange: IntRange) {
    companion object {
        fun of(line: String): Instruction {
            val operation = when {
                line.contains("turn on") -> Operation.ON
                line.contains("turn off") -> Operation.OFF
                else -> Operation.TOGGLE
            }
            val trimmed = line.dropWhile { !it.isDigit() }
            val (start, _, end) = trimmed.split(" ")
            val (startX, startY) = start.split(",")
            val (endX, endY) = end.split(",")
            val xRange = startX.toInt()..endX.toInt()
            val yRange = startY.toInt()..endY.toInt()
            return Instruction(operation, xRange, yRange)
        }
    }
}

private fun performInstructions(instructions: List<Instruction>): Int {
    val lightsOn = mutableSetOf<Pair<Int, Int>>()
    instructions.forEach { instruction ->
        when (instruction.operation) {
            Operation.ON -> {
                for (x in instruction.xRange) {
                    for (y in instruction.yRange) {
                        lightsOn.add(x to y)
                    }
                }
            }

            Operation.OFF -> {
                for (x in instruction.xRange) {
                    for (y in instruction.yRange) {
                        lightsOn.remove(x to y)
                    }
                }
            }

            Operation.TOGGLE -> {
                for (x in instruction.xRange) {
                    for (y in instruction.yRange) {
                        if (!lightsOn.remove(x to y)) lightsOn.add(x to y)
                    }
                }
            }
        }
    }

    return lightsOn.size
}

private fun performBrightnessInstructions(instructions: List<Instruction>): Int {
    val lights = mutableMapOf<Pair<Int, Int>, Int>()
    instructions.forEach { instruction ->
        when (instruction.operation) {
            Operation.ON -> {
                for (x in instruction.xRange) {
                    for (y in instruction.yRange) {
                        lights[x to y] = (lights[x to y] ?: 0) + 1
                    }
                }
            }

            Operation.OFF -> {
                for (x in instruction.xRange) {
                    for (y in instruction.yRange) {
                        lights[x to y] = ((lights[x to y] ?: 0) - 1).coerceAtLeast(0)
                    }
                }
            }

            Operation.TOGGLE -> {
                for (x in instruction.xRange) {
                    for (y in instruction.yRange) {
                        lights[x to y] = (lights[x to y] ?: 0) + 2
                    }
                }
            }
        }
    }

    return lights.values.sum()
}
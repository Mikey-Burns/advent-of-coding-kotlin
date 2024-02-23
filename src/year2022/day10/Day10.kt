package year2022.day10

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        return input.signalStrength()
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test", "2022")
    check(part1(testInput).also(::println) == 13140L)
    val testInput2 = readInput("Day10_test", "2022")
    check(part2(testInput2).also(::println) == 0L)

    val input = readInput("Day10", "2022")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.signalStrength() : Long {
    val signals = mutableListOf(1)
    this.forEach { instruction ->
        if (instruction.startsWith("addx")) {
            val v = instruction.substringAfter(" ").toInt()
            signals.add(signals.last())
            signals.add(signals.last() + v)
        } else {
            signals.add(signals.last())
        }
    }
    return listOf(20, 60, 100, 140, 180, 220)
        .sumOf { cycle -> (signals[cycle - 1] * cycle).toLong() }
}
package year2022.day10

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input.signalStrength()

    fun part2(input: List<String>): String = input.toSignals().draw()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test", "2022")
    check(part1(testInput).also(::println) == 13140L)
    val testInput2 = readInput("Day10_test", "2022")
    check(
        part2(testInput2).also(::println) == """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()
    )

    val input = readInput("Day10", "2022")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.toSignals(): List<Int> = mutableListOf(1)
    .apply {
        this@toSignals.forEach { instruction ->
            if (instruction.startsWith("addx")) {
                val v = instruction.substringAfter(" ").toInt()
                this.add(this.last())
                this.add(this.last() + v)
            } else {
                this.add(this.last())
            }
        }
    }

private fun List<String>.signalStrength(): Long = this.toSignals()
    .let { signals ->
        listOf(20, 60, 100, 140, 180, 220)
            .sumOf { cycle -> (signals[cycle - 1] * cycle).toLong() }
    }

private fun List<Int>.draw(): String = this.take(240)
    .chunked(40) { signals ->
        signals.mapIndexed { index, signal ->
            if (signal in index - 1..index + 1) '#' else '.'
        }
    }
    .joinToString("\n") { it.joinToString("") }
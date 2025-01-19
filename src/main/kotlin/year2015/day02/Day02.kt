package year2015.day02

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input.calculate { l, w, h ->
        (2 * l * w) + (2 * w * h) + (2 * h * l) + minOf(l * w, w * h, h * l)
    }

    fun part2(input: List<String>): Long = input.calculate { l, w, h ->
        // Smallest perimeter = double of (sum of all 3 sides - length of max side)
        (l + w + h - maxOf(l, w, h)) * 2 + (l * w * h)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test", "2015")
    check(part1(testInput) == 58L + 43L)
    val testInput2 = readInput("Day02_test", "2015")
    check(part2(testInput2) == 34L + 14L)

    val input = readInput("Day02", "2015")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.calculate(calculation: ((Long, Long, Long) -> Long)): Long =
    this.sumOf { line ->
        val (l, w, h) = line.split("x").map { it.toLong() }
        calculation.invoke(l, w, h)
    }
package year2016.day15

import utils.println
import utils.readInput
import java.util.function.Predicate
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = findDrop(input)

    fun part2(input: List<String>): Long = findDrop(input + "Disc #7 has 11 positions; at time=0, it is at position 0.")

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test", "2016")
    check(part1(testInput) == 5L)

    val input = readInput("Day15", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun findDrop(input: List<String>): Long {
    val fullPredicate = input.mapIndexed { index, line ->
        val split = line.split(" ")
        val positions = split[3].toInt()
        val startingPosition = split.last().dropLast(1).toInt()

        Predicate { n: Long -> ((n + index + 1) + startingPosition) % positions == 0L }
    }
        .reduce { combinedPredicate, singlePredicate -> combinedPredicate.and(singlePredicate) }

    return generateSequence(0L) { it + 1 }
        .first { fullPredicate.test(it) }
}
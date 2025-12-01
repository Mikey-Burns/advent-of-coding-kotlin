package year2025.day01

import utils.println
import utils.readInput
import java.lang.Math.floorMod
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.processLock().countEndOnZero()

    fun part2(input: List<String>): Int = input.processLock().countCrossingZero()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test", "2025")
    check(part1(testInput) == 3)
    val testInput2 = readInput("Day01_test", "2025")
    check(part2(testInput2) == 6)

    val input = readInput("Day01", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.processLock(): List<Int> = this.runningFold(50) { position, line ->
    val rotations = line.drop(1).toInt()
    (if (line.first() == 'R') position + rotations else position - rotations)
}
//    .also(::println)

private fun List<Int>.countEndOnZero() = this.count { it % 100 == 0}

private fun List<Int>.countCrossingZero(): Int = this.windowed(2) { (start, end) ->
    (min(start, end)..max(start, end))
        .count { it != start && floorMod(it, 100) == 0 }
//        .also { println("Start: $start, End: $end, Count: $it")}
}
    .sum()
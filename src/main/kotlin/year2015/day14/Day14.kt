package year2015.day14

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>, time: Int = 2503): Int = input.map { Reindeer.of(it) }
        .maxOf { reindeer -> reindeer.locationAtTime(time) }

    fun part2(input: List<String>, time: Int = 2503): Int = input.map { Reindeer.of(it) }
        .let { reindeer ->
            val points = reindeer.associate { it.name to 0 }.toMutableMap()
            for (currentTime in 1..time) {
                val currentPositions = reindeer.associateWith { it.locationAtTime(currentTime) }
                currentPositions.filterValues { position -> position == currentPositions.values.max() }
                    .keys
                    .map { it.name }
                    .forEach { winningReindeer -> points[winningReindeer] = points.getValue(winningReindeer) + 1 }
            }
            points.values.max()
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test", "2015")
    check(part1(testInput, 1000) == 1120)
    val testInput2 = readInput("Day14_test", "2015")
    check(part2(testInput2, 1000) == 689)

    val input = readInput("Day14", "2015")
    part1(input).println()
    part2(input).println()
}

private data class Reindeer(val name: String, val speed: Int, val duration: Int, val rest: Int) {
    private val distanceInLoop = speed * duration
    private val fullLoop = duration + rest

    fun locationAtTime(time: Int): Int =
        distanceInLoop * (time / fullLoop) + speed * (time % fullLoop).coerceAtMost(duration)

    companion object {
        fun of(line: String): Reindeer {
            val name = line.substringBefore(" ")
            val (speed, duration, rest) = line.split(" ").mapNotNull { it.toIntOrNull() }
            return Reindeer(name, speed, duration, rest)
        }
    }
}
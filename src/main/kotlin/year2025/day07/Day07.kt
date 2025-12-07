package year2025.day07

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.tachyon()

    fun part2(input: List<String>): Long = input.quantumTachyon()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test", "2025")
    check(part1(testInput) == 21)
    val testInput2 = readInput("Day07_test", "2025")
    check(part2(testInput2) == 40L)

    val input = readInput("Day07", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.tachyon(): Int {
    val start = first().indexOf('S')
    var splits = 0
    this.drop(1)
        .fold(setOf(start)) { beams, line ->
            beams.flatMap { index ->
                if (line[index] == '^') {
                    splits++
                    setOf(index - 1, index + 1)
                } else setOf(index)
            }
                .toSet()
        }
    return splits
}

private fun List<String>.quantumTachyon(): Long {
    val start = first().indexOf('S')
    return drop(1)
        .fold(mapOf(start to 1L)) { beamMap, line ->
            val nextLevel = mutableMapOf<Int, Long>()
            beamMap.forEach { (index, count) ->
                if (line[index] == '^') {
                    nextLevel[index - 1] = nextLevel.getOrDefault(index - 1, 0L) + count
                    nextLevel[index + 1] = nextLevel.getOrDefault(index + 1, 0L) + count
                } else {
                    nextLevel[index] = nextLevel.getOrDefault(index, 0L) + count
                }
            }
            nextLevel
        }
        .toList()
        .sumOf { it.second }
}
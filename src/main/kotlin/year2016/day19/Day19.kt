package year2016.day19

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = winningElf(input[0].toInt())

    fun part2(input: List<String>): Int = smarterElf(input[0].toInt())

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test", "2016")
    check(part1(testInput) == 3)
    val testInput2 = readInput("Day19_test", "2016")
    check(part2(testInput2) == 2)

    val input = readInput("Day19", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun winningElf(numberOfElves: Int): Int {
    val initialElves = List(numberOfElves) { it + 1 }

    tailrec fun splitElves(elves: List<Int>): Int {
        if (elves.size == 1) return elves.single()
        val remainingElves = elves.filterIndexed { index, elf -> index % 2 == 0 }
        return if (elves.size % 2 == 0) splitElves(remainingElves) else splitElves(
            listOf(remainingElves.last()) +
                    remainingElves.dropLast(1)
        )
    }

    return splitElves(initialElves)
}

/**
 * Use induction to find the winner.
 * Knowing the index M of the winner at size N, we find that elf for size N+1.
 * We add 1 to M because at size N+1 we would have to eliminate one elf before we are size N.
 * Check if 1 + M moves us past the elf we eliminated.  If so, add 1 again.
 * Use modulo to ensure we wrap around the circle instead of going too far.
 */
private fun smarterElf(numberOfElves: Int): Int = (2..numberOfElves)
    .fold(0) { previousWinner, elf ->
        // Add one because we eliminate an elf to get back to size N
        val candidate = 1 + previousWinner
        // Check if we need an offset because we included the eliminated elf
        if (candidate >= elf / 2) (candidate + 1) % elf else candidate
    } + 1
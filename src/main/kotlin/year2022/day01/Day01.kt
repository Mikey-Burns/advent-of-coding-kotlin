package year2022.day01

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long {
        return input.elves().maxOf(List<Long>::sum)
    }

    fun part2(input: List<String>): Long {
        return input.elves().map(List<Long>::sum).sortedDescending().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test", "2022")
    check(part1(testInput).also(::println) == 24000L)
    val testInput2 = readInput("Day01_test", "2022")
    check(part2(testInput2).also(::println) == 45000L)

    val input = readInput("Day01", "2022")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.elves(): List<List<Long>> {
    val lists = mutableListOf(mutableListOf<Long>())
    this.forEach { line ->
        if (line.isBlank()) {
            lists.add(mutableListOf())
        } else {
            lists.last().add(line.toLong())
        }
    }
    return lists
}

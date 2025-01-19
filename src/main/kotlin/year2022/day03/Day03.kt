package year2022.day03

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input.map(::Rucksack).sumOf(Rucksack::priority)

    fun part2(input: List<String>): Long = input.map(::Rucksack)
        .chunked(3)
        .sumOf(List<Rucksack>::badgePriority)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test", "2022")
    check(part1(testInput).also(::println) == 157L)
    val testInput2 = readInput("Day03_test", "2022")
    check(part2(testInput2).also(::println) == 70L)

    val input = readInput("Day03", "2022")
    part1(input).println()
    part2(input).println()
}

private data class Rucksack(val line: String) {
    private val split = line.lastIndex / 2 + 1
    val left = line.substring(0, split)
    val right = line.substring(split)

    fun priority(): Long = left.first { it in right }.toPriority()
}

private fun List<Rucksack>.badgePriority() = first()
    .line
    .first { it in this[1].line && it in this[2].line }
    .toPriority()

val priorityMap: Map<Char, Long> = buildMap {
    ('a'..'z').forEachIndexed { index, c -> put(c, index + 1L) }
    ('A'..'Z').forEachIndexed { index, c -> put(c, index + 27L) }
}

private fun Char.toPriority(): Long = priorityMap.getValue(this)
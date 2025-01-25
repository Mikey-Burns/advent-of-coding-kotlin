package year2015.day24

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = input.map { it.toLong() }.findQuantumEntanglement()

    fun part2(input: List<String>): Int = 0

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test", "2015")
    check(part1(testInput) == 99L)
    val testInput2 = readInput("Day24_test", "2015")
    check(part2(testInput2) == 0)

    val input = readInput("Day24", "2015")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<Long>.findQuantumEntanglement(): Long {
    val total = this.sum()
    val groupTarget = total / 3
    val maxGroupSize = this.runningFold(0L) { sum, value -> sum + value}.indexOfFirst { it > groupTarget } + 1
    for (groupSize in 1..maxGroupSize) {
        val candidates = mutableListOf<List<Long>>()
        // Find candidates
        fun getCombinations(list: List<Long>, size: Int, partialList: List<Long> = emptyList<Long>()): List<List<Long>> {
            return if (partialList.size == size) {
                if (partialList.sum() == groupTarget) {
                    listOf(partialList)
                } else {
                    emptyList()
                }
            } else {
                list.flatMapIndexed { index, element ->
                    getCombinations(list.drop(index + 1), size, partialList + element)
                }
            }
        }
        val firstGroups = getCombinations(this, groupSize)
        firstGroups.forEach { groupOne ->
            for (groupTwoSize in 1..(this.size - groupSize)) {
                val otherGroups = getCombinations(this - groupOne, groupTwoSize)
                if (otherGroups.isNotEmpty()) {
                    // If there is a way to group the remaining numbers, our first group is a candidate
                    // so we can stop looking at this first group
                    candidates.add(groupOne)
                    break
                }
            }
        }

        if (candidates.isNotEmpty()) {
            return candidates.minOf { it.fold(1L) { left, right -> left * right } }
        }
    }
    error("No valid groups")
}
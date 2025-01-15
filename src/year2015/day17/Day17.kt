package year2015.day17

import println
import readInput

fun main() {
    fun part1(input: List<String>, liters: Int = 150): Int = input.map { Container(it.toInt()) }
        .countCombinations(liters)

    fun part2(input: List<String>, liters: Int = 150): Int = input.map { Container(it.toInt()) }
        .countMinCombinations(liters)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test", "2015")
    check(part1(testInput, 25) == 4)
    val testInput2 = readInput("Day17_test", "2015")
    check(part2(testInput2, 25) == 3)

    val input = readInput("Day17", "2015")
    part1(input).println()
    part2(input).println()
}

private data class Container(val capacity: Int)

private fun List<Container>.countCombinations(liters: Int): Int = findAllCombinations(liters).size

private fun List<Container>.countMinCombinations(liters: Int): Int = findAllCombinations(liters)
    .groupBy { it.size }
    .minBy { (listSize, _) -> listSize }
    .value
    .size

private fun List<Container>.findAllCombinations(liters: Int): List<List<Container>> {
    val combinations = mutableListOf<List<Container>>()

    fun findCombinations(partialCombination: List<Container>, remainingContainers: List<Container>) {
        val runningTotal = partialCombination.sumOf { it.capacity }
        if (runningTotal == liters) {
            combinations.add(partialCombination)
        } else {
            for (remainingIndex in remainingContainers.indices) {
                if (runningTotal + remainingContainers[remainingIndex].capacity <= liters) {
                    findCombinations(
                        partialCombination + remainingContainers[remainingIndex],
                        remainingContainers.drop(remainingIndex + 1)
                    )
                }
            }
        }
    }

    for (containerIndex in indices) {
        findCombinations(listOf(this[containerIndex]), this.drop(containerIndex + 1))
    }

    return combinations
}
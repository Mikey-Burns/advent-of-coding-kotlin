package year2024.day22

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = input
        .map { it.toLong() }.sumOf { it.evolveTimes(2000) }

    fun part2(input: List<String>): Long = input.bestSequence()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test", "2024")
    check(part1(testInput) == 37327623L)
    val testInput2 = readInput("Day22_test2", "2024")
    check(part2(testInput2) == 23L)

    val input = readInput("Day22", "2024")
    part1(input).println()
    part2(input).println()
}


private fun Long.evolveTimes(count: Int): Long {
    var result = this
    repeat(count) {
        result = result.evolve()

    }
    return result
}

private fun Long.evolve(): Long {
    val firstShift = this shl 6
    var result = this xor firstShift
    result %= 16777216
    val secondShift = result shr 5
    result = result xor secondShift
    result %= 16777216
    val thirdShift = result shl 11
    result = result xor thirdShift
    result %= 16777216
    return result
}

private fun List<String>.bestSequence(): Long = this.fold(mutableMapOf<List<Long>, Long>()) { bananas, line ->
    var secret = line.toLong()
    val sequences = mutableSetOf<List<Long>>()
    buildList {
        add(secret)
        repeat(2000) {
            secret = secret.evolve()
            add(secret)
        }
    }
        .map { it % 10 }
        .windowed(5)
        .forEach { secrets ->
            val sequence = secrets.zipWithNext { a, b -> a - b }
            if (!sequences.contains(sequence)) {
                bananas[sequence] = secrets.last() + (bananas[sequence] ?: 0)
                sequences.add(sequence)
            }
        }

    bananas
}
    .values
    .max()
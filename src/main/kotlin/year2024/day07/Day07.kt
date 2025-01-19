package year2024.day07

import utils.println
import utils.readInput
import year2024.day07.Operation.*

fun main() {
    fun part1(input: List<String>): Long = input.map { MathLine(it) }
        .filter { it.isValid(setOf(ADD, MULTIPLY)) }
        .sumOf(MathLine::target)

    fun part2(input: List<String>): Long = input.map { MathLine(it) }
        .filter { it.isValid(setOf(ADD, MULTIPLY, COMBINE)) }
        .sumOf(MathLine::target)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test", "2024")
    check(part1(testInput) == 3749L)
    val testInput2 = readInput("Day07_test", "2024")
    check(part2(testInput2) == 11387L)

    val input = readInput("Day07", "2024")
    part1(input).println()
    part2(input).println()
}

private data class MathLine(val target: Long, val numbers: List<Long>) {

    fun isValid(operations: Set<Operation>): Boolean =
        doMath(operations, numbers.first(), numbers.drop(1)).contains(target)

    private fun doMath(operations: Set<Operation>, total: Long, remaining: List<Long>): List<Long> {
        return if (remaining.isEmpty()) listOf(total)
        else operations.flatMap { operation ->
            doMath(
                operations,
                operation.perform(total, remaining.first()),
                remaining.drop(1)
            )
        }
    }
}

private fun MathLine(line: String): MathLine {
    val target = line.substringBefore(":").toLong()
    val numbers = line.substringAfter(": ").split(" ").map(String::toLong)
    return MathLine(target, numbers)
}

private enum class Operation {
    ADD, MULTIPLY, COMBINE;

    fun perform(first: Long, second: Long): Long {
        return when (this) {
            ADD -> first + second
            MULTIPLY -> first * second
            COMBINE -> (first.toString() + second.toString()).toLong()
        }
    }
}
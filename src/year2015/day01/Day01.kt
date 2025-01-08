package year2015.day01

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = input.joinToString("")
        .let { it.count { c -> c == '(' } - it.count { c -> c == ')' } }

    fun part2(input: List<String>): Int = input.joinToString("")
        .foldIndexed(0) { index, count, c ->
            val newCount = if (c == '(') count + 1 else count - 1
            if (newCount < 0) return index + 1
            newCount
        }

    // test if implementation meets criteria from the description, like:
    check(part1(listOf("(())")) == 0)
    check(part1(listOf("()()")) == 0)
    check(part1(listOf("(((")) == 3)
    check(part1(listOf("(()(()(")) == 3)
    check(part1(listOf("))(((((")) == 3)
    check(part1(listOf("())")) == -1)
    check(part1(listOf("))(")) == -1)
    check(part1(listOf(")))")) == -3)
    check(part1(listOf(")())())")) == -3)

    check(part2(listOf(")")) == 1)
    check(part2(listOf("()())")) == 5)

    val input = readInput("Day01", "2015")
    part1(input).println()
    part2(input).println()
}
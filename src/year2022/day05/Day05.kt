package year2022.day05

import println
import readInput

fun main() {
    fun part1(input: List<String>): String = processInput(input) { it.reversed() }

    fun part2(input: List<String>): String = processInput(input)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test", "2022")
    check(part1(testInput).also(::println) == "CMZ")
    val testInput2 = readInput("Day05_test", "2022")
    check(part2(testInput2).also(::println) == "MCD")

    val input = readInput("Day05", "2022")
    part1(input).println()
    part2(input).println()
}

fun processInput(
    input: List<String>,
    transform: (List<Char>) -> List<Char> = { it }
): String {
    val gapIndex = input.indexOfFirst(String::isEmpty)
    val numStacks = input[gapIndex - 1].split(" ").mapNotNull { it.toIntOrNull() }.max()
    val stacks = List(numStacks + 1) { mutableListOf<Char>() }
    ((gapIndex - 2) downTo 0).map { input[it] }
        .forEach { line ->
            line.forEachIndexed { index, c ->
                if (c in 'A'..'Z')
                    stacks[(index - 1) / 4 + 1].add(c)
            }
        }
    val instructions = input.drop(gapIndex + 1).map(::Instruction)
    instructions.forEach { instruction ->
        val toMove = stacks[instruction.from].takeLast(instruction.boxes)
        repeat(instruction.boxes) { stacks[instruction.from].removeLast() }
        stacks[instruction.to].addAll(transform(toMove))
    }
    return stacks.drop(1).map { it.last() }.joinToString("")
}

private data class Instruction(val boxes: Int, val from: Int, val to: Int)

private fun Instruction(line: String): Instruction = line.split(" ")
    .mapNotNull { it.toIntOrNull() }
    .let { (boxes, from, to) -> Instruction(boxes, from, to) }
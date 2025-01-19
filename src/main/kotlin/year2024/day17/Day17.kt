package year2024.day17

import utils.println
import utils.readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): String = input.parseInput()
        .let { (computer, instructions) -> computer.executeInstructions(instructions) }
        .joinToString(",")

    fun part2(input: List<String>): Long = input.parseInput().second
        .let { instructions ->
            val toSearchFor: ArrayDeque<Pair<Int, Long>> = ArrayDeque<Pair<Int, Long>>()
                .apply { add(instructions.lastIndex to 0L) }
            val potentialSolutions = mutableSetOf<Long>()
            while (toSearchFor.isNotEmpty()) {
                val (index, targetValue) = toSearchFor.removeFirst()
                for (lastThreeBits in 0L..7L) {
                    val a = (targetValue shl 3) or lastThreeBits
                    val computer = Computer(a)
                    val output = computer.executeInstructions(instructions, true)

                    if (output.first() == instructions[index] && computer.a == targetValue) {
                        if (index == 0) {
                            potentialSolutions.add(a)
                        } else {
                            toSearchFor.add(index - 1 to a)
                        }
                    }
                }
            }
            potentialSolutions.minBy { solution ->
                Computer(solution).executeInstructions(instructions) == instructions
            }
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test", "2024")
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
    val testInput2 = readInput("Day17_test2", "2024")
    check(part2(testInput2) == 117440L)

    val input = readInput("Day17", "2024")
    part1(input).println()
    part2(input).println()
}

private data class Computer(var a: Long, var b: Long = 0L, var c: Long = 0L) {

    fun executeInstructions(instructions: List<Int>, shortCircuit: Boolean = false): List<Int> {
        var counter = 0
        val output = mutableListOf<Int>()
//        println("a: $a, b: $b, c: $c, counter: $counter")
        while (counter in instructions.indices) {
            if (counter + 1 !in instructions.indices) error("Argument is beyond the end of the instructions!")
            val literal = instructions[counter + 1]
            fun combo(): Long = when (instructions[counter + 1]) {
                0, 1, 2, 3 -> literal.toLong()
                4 -> a
                5 -> b
                6 -> c
                7 -> error("Reserved!")
                else -> error("Invalid operand: ${instructions[counter]}")
            }
            when (instructions[counter]) {
                !in 0..7 -> error("Invalid instruction: ${instructions[counter]}")
                0 -> {
                    a /= 2.0.pow(combo().toInt()).toInt()
                }

                1 -> b = b.xor(literal.toLong())
                2 -> b = combo() % 8
                3 -> {
                    if (a == 0L || shortCircuit) Unit else {
                        // Minus 2 to counteract our automatic increment
                        counter = literal - 2
                    }
                }

                4 -> b = b.xor(c)
                5 -> output.add((combo() % 8L).toInt())
                6 -> b = a / 2.0.pow(combo().toInt()).toInt()
                7 -> c = a / 2.0.pow(combo().toInt()).toInt()
            }
            // Almost everything wants us to increment by 2
            counter += 2

//            println("a: $a, b: $b, c: $c, counter: $counter")
        }
        return output
    }


}

private fun List<String>.parseInput(): Pair<Computer, List<Int>> {
    val a = this[0].substringAfter(": ").toLong()
    val b = this[1].substringAfter(": ").toLong()
    val c = this[2].substringAfter(": ").toLong()

    val instructions = this.last().substringAfter(": ").split(",").map { it.toInt() }

    return Computer(a, b, c) to instructions
}
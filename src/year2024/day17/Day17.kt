package year2024.day17

import println
import readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): String = input.parseInput()
        .let { (computer, instructions) -> computer.executeInstructions(instructions) }
        .joinToString(",")

    fun part2(input: List<String>): String = ""

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test", "2024")
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
    val testInput2 = readInput("Day17_test", "2024")
    check(part2(testInput2) == "")

    val input = readInput("Day17", "2024")
    part1(input).println()
    part2(input).println()
}

private data class Computer(var a: Int, var b: Int, var c: Int) {

    fun executeInstructions(instructions: List<Int>): List<Int> {
        var counter = 0
        val output = mutableListOf<Int>()
//        println("a: $a, b: $b, c: $c, counter: $counter")
        while (counter in instructions.indices) {
            if (counter + 1 !in instructions.indices) error("Argument is beyond the end of the instructions!")
            val literal = instructions[counter + 1]
            fun combo() = when (instructions[counter + 1]) {
                0, 1, 2, 3 -> literal
                4 -> a
                5 -> b
                6 -> c
                7 -> error("Reserved!")
                else -> error("Invalid operand: ${instructions[counter]}")
            }
            when (instructions[counter]) {
                !in 0..7 -> error("Invalid instruction: ${instructions[counter]}")
                0 -> {
                    a /= 2.0.pow(combo()).toInt()
                }

                1 -> b = b.xor(literal)
                2 -> b = combo() % 8
                3 -> {
                    if (a == 0) Unit else {
                        // Minus 2 to counteract our automatic increment
                        counter = literal - 2
                    }
                }

                4 -> b = b.xor(c)
                5 -> output.add(combo() % 8)
                6 -> b = a / 2.0.pow(combo()).toInt()
                7 -> c = a / 2.0.pow(combo()).toInt()
            }
            // Almost everything wants us to increment by 2
            counter += 2

//            println("a: $a, b: $b, c: $c, counter: $counter")
        }
        return output
    }


}

private fun List<String>.parseInput(): Pair<Computer, List<Int>> {
    val a = this[0].substringAfter(": ").toInt()
    val b = this[1].substringAfter(": ").toInt()
    val c = this[2].substringAfter(": ").toInt()

    val instructions = this.last().substringAfter(": ").split(",").map { it.toInt() }

    return Computer(a, b, c) to instructions
}
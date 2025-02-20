package year2016.day25

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = findLowestA(input)

    val input = readInput("Day25", "2016")
    measureTime { part1(input).println() }.println()
}

private fun findLowestA(input: List<String>): Int {

    fun compute(initialA: Int, instructions: List<String>): List<Int> {
        val output = mutableListOf<Int>()
        val registers = mutableMapOf(
            "a" to initialA,
            "b" to 0,
            "c" to 0,
            "d" to 0,
        )

        var counter = 0
        var offset: Int
        while (counter in instructions.indices && output.size < 20) {
            offset = 1
            val split = instructions[counter].split(" ")
            when (split[0]) {
                "cpy" -> registers[split[2]] = split[1].toIntOrNull() ?: registers.getValue(split[1])
                "inc" -> registers[split[1]] = registers.getValue(split[1]) + 1
                "dec" -> registers[split[1]] = registers.getValue(split[1]) - 1
                "jnz" -> if ((registers[split[1]] ?: split[1].toInt()) != 0) offset =
                    (registers[split[2]] ?: split[2].toInt())
                "out" -> output.add(registers[split[1]] ?: split[1].toInt())
            }

            counter += offset
        }
        return output
    }

    // We know it has to be even, or it loops forever
    return generateSequence(2) { it + 2 }
        .first { compute(it, input).withIndex().all { (index, value) -> index % 2 == value } }
}
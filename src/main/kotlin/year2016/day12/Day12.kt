package year2016.day12

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = compute(input)

    fun part2(input: List<String>): Int = compute(input, 1)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test", "2016")
    check(part1(testInput) == 42)
    val testInput2 = readInput("Day12_test", "2016")
    check(part2(testInput2) == 42)

    val input = readInput("Day12", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun compute(instructions: List<String>, initialC: Int = 0): Int {
    val registers = mutableMapOf(
        "a" to 0,
        "b" to 0,
        "c" to initialC,
        "d" to 0,
    )

    var counter = 0
    var offset = 1
    while (counter in instructions.indices) {
        offset = 1
        val split = instructions[counter].split(" ")
        when (split[0]) {
            "cpy" -> registers[split[2]] = split[1].toIntOrNull() ?: registers.getValue(split[1])
            "inc" -> registers[split[1]] = registers.getValue(split[1]) + 1
            "dec" -> registers[split[1]] = registers.getValue(split[1]) - 1
            "jnz" -> if ((registers[split[1]] ?: split[1].toInt()) != 0) offset = split[2].toInt()
        }
        counter += offset
    }
    return registers.getValue("a")
}
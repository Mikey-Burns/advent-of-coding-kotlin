package year2016.day23

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = compute(input.toMutableList())

    fun part2(input: List<String>): Long = reverseEngineer(input)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test", "2016")
    check(part1(testInput) == 3L)

    val input = readInput("Day23", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun compute(instructions: MutableList<String>, initialA: Long = 7L): Long {
    val registers = mutableMapOf(
        "a" to initialA,
        "b" to 0L,
        "c" to 0L,
        "d" to 0L,
    )

    var counter = 0
    var offset: Int
    while (counter in instructions.indices) {
        offset = 1
        val split = instructions[counter].split(" ")
        when (split[0]) {
            "cpy" -> registers[split[2]] = split[1].toLongOrNull() ?: registers.getValue(split[1])
            "inc" -> registers[split[1]] = registers.getValue(split[1]) + 1
            "dec" -> registers[split[1]] = registers.getValue(split[1]) - 1
            "jnz" -> if ((registers[split[1]] ?: split[1].toLong()) != 0L) offset =
                (registers[split[2]] ?: split[2].toLong()).toInt()

            "tgl" -> {
                val tglOffset = registers.getValue(split[1])
                val tglIndex = counter + tglOffset
                if (tglIndex in instructions.indices) {
                    var instruction = instructions[tglIndex.toInt()]
                    val iSplit = instruction.split(" ")
                    instruction = when {
                        iSplit.size == 2 && iSplit[0] == "inc" -> instruction.replace("inc", "dec")
                        iSplit.size == 2 -> instruction.replace(iSplit[0], "inc")
                        iSplit[0] == "jnz" -> instruction.replace("jnz", "cpy")
                        else -> instruction.replace(iSplit[0], "jnz")
                    }

                    instructions[tglIndex.toInt()] = instruction
                }
            }
        }
        counter += offset
    }
    return registers.getValue("a")
}

/**
 * The result of reverse engineering the instructions.
 * There are two high level loops, each that have nesting.
 * The first main loop is from lines 3 to 19, which results
 * initialA multiplied by all integers down to 1,
 * e.g. 12 * 11 * 10...
 * The second main loop is lines 21 to 26, bearing in mind that
 * the odd numbered lines (even 0 based index) will have been toggled.
 * This loop adds the product of the constants for C and D.
 * In my input, 96*95.
 */
private fun reverseEngineer(input: List<String>): Long {
    val a = 12L
    val c = input[19].split(" ")[1].toLong()
    val d = input[20].split(" ")[1].toLong()
    return (a downTo 2).reduce { acc, lng -> acc * lng } + (c * d)
}
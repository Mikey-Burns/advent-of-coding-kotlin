package year2016.day10

import utils.println
import utils.readInput
import java.util.function.BiConsumer
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>, leftChip: Int = 61, rightChip: Int = 17): Int = RobotSystem(input)
        .let { (_, combinations) -> combinations.getValue(min(leftChip, rightChip) to max(leftChip, rightChip)) }

    fun part2(input: List<String>): Int = RobotSystem(input)
        .let { (outputs, _) -> outputs.getValue(0) * outputs.getValue(1) * outputs.getValue(2) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test", "2016")
    check(part1(testInput, 5, 2) == 2)
    val testInput2 = readInput("Day10_test", "2016")
    check(part2(testInput2) == 5 * 2 * 3)

    val input = readInput("Day10", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun RobotSystem(instructions: List<String>): RobotSystem {
    val robots: MutableMap<Int, MutableList<Int>> = mutableMapOf()
    val functions: MutableMap<Int, BiConsumer<Int, Int>> = mutableMapOf()
    val outputs: MutableMap<Int, Int> = mutableMapOf()
    val combinations: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    instructions.forEach { instruction ->
        if (instruction.startsWith("value")) {
            val (value, bot) = instruction.split(" ").mapNotNull { it.toIntOrNull() }
            robots[bot] = (robots[bot] ?: mutableListOf<Int>())
                .apply { add(value) }
        } else {
            val split = instruction.split(" ")
            val bot = split[1].toInt()
            val isLowOutput = split[5] == "output"
            val lowNumber = split[6].toInt()
            val isHighOutput = split[10] == "output"
            val highNumber = split[11].toInt()
            functions[bot] = BiConsumer { a, b ->
                val min = min(a, b)
                val max = max(a, b)

                if (isLowOutput) {
                    outputs[lowNumber] = min
                } else {
                    robots[lowNumber] = (robots[lowNumber] ?: mutableListOf<Int>())
                        .apply { add(min) }
                }

                if (isHighOutput) {
                    outputs[highNumber] = max
                } else {
                    robots[highNumber] = (robots[highNumber] ?: mutableListOf<Int>())
                        .apply { add(max) }
                }

                combinations[min to max] = bot
            }
        }
    }

    while (robots.values.any { it.size == 2 }) {
        val toUpdate = robots.filter { (_, chips) -> chips.size == 2 }
        toUpdate.forEach { (robot, chips) ->
            if (chips.size == 2) {
                robots[robot] = mutableListOf()
                functions[robot]?.accept(chips[0], chips[1])
            }
        }
    }

    return RobotSystem(outputs, combinations)
}

private data class RobotSystem(val outputs: Map<Int, Int>, val combinations: Map<Pair<Int, Int>, Int>)
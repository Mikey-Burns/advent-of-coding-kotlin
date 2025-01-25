package year2015.day23

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = compute(input).second

    fun part2(input: List<String>): Int = compute(input, 1).second

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test", "2015")
    check(compute(testInput) == 2 to 0)

    val input = readInput("Day23", "2015")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun compute(instructions: List<String>, initialA: Int = 0): Pair<Int, Int> {
    var a = initialA
    var b = 0
    var counter = 0
    var offset = 1
    while (counter in instructions.indices) {
        offset = 1
        val split = instructions[counter].split(" ")
        when (split[0]) {
            "hlf" -> a /= 2
            "tpl" -> a *= 3
            "inc" -> {
                if (split[1] == "a") {
                    a++
                } else {
                    b++
                }
            }

            "jmp" -> offset = split[1].toInt()
            "jie" -> if (a % 2 == 0) offset = split[2].toInt()
            "jio" -> if (a == 1) offset = split[2].toInt()
        }
        counter += offset
    }
    return a to b
}
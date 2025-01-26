package year2016.day01

import utils.Direction.*
import utils.println
import utils.readInput
import utils.turnLeft
import utils.turnRight
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.first().walk()

    fun part2(input: List<String>): Int = input.first().visitTwice()

    // test if implementation meets criteria from the description, like:
    check("R2, L3".walk() == 5)
    check("R2, R2, R2".walk() == 2)
    check("R5, L5, R5, R3".walk() == 12)
    check("R8, R4, R4, R8".visitTwice() == 4)
    val input = readInput("Day01", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun String.walk(): Int {
    var direction = UP
    var vertical = 0
    var horizontal = 0
    this.split(", ")
        .forEach { instruction ->
            direction = if (instruction[0] == 'R') direction.turnRight() else direction.turnLeft()
            val steps = instruction.drop(1).toInt()
            when (direction) {
                UP -> vertical += steps
                DOWN -> vertical -= steps
                LEFT -> horizontal -= steps
                RIGHT -> horizontal += steps
            }
        }
    return abs(vertical) + abs(horizontal)
}

private fun String.visitTwice(): Int {
    var direction = UP
    var vertical = 0
    var horizontal = 0
    val locations = mutableSetOf<Pair<Int, Int>>()
    this.split(", ")
        .forEach { instruction ->
            direction = if (instruction[0] == 'R') direction.turnRight() else direction.turnLeft()
            val steps = instruction.drop(1).toInt()
            repeat(steps) {
                when (direction) {
                    UP -> vertical++
                    DOWN -> vertical--
                    LEFT -> horizontal--
                    RIGHT -> horizontal++
                }
                val location = horizontal to vertical
                if (location in locations) return abs(vertical) + abs(horizontal)
                locations.add(location)
            }

        }
    error("No repeats!")
}
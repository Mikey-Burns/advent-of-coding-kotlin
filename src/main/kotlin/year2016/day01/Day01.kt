package year2016.day01

import utils.*
import utils.Direction.*
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.first().distanceToDestination()

    fun part2(input: List<String>): Int = input.first().distanceToRepeatedLocation()

    // test if implementation meets criteria from the description, like:
    check("R2, L3".distanceToDestination() == 5)
    check("R2, R2, R2".distanceToDestination() == 2)
    check("R5, L5, R5, R3".distanceToDestination() == 12)
    check("R8, R4, R4, R8".distanceToRepeatedLocation() == 4)
    val input = readInput("Day01", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun String.fullWalkingPath(): List<Location> {
    var direction = UP
    var vertical = 0
    var horizontal = 0
    return this.split(", ")
        .flatMap { instruction ->
            direction = if (instruction[0] == 'R') direction.turnRight() else direction.turnLeft()
            val steps = instruction.drop(1).toInt()
            buildList {
                repeat(steps) {
                    when (direction) {
                        UP -> vertical++
                        DOWN -> vertical--
                        LEFT -> horizontal--
                        RIGHT -> horizontal++
                    }
                    add(horizontal to vertical)
                }
            }
        }
}

private fun String.distanceToDestination(): Int =
    fullWalkingPath().last().let { (horizontal, vertical) -> abs(horizontal) + abs(vertical) }

private fun String.distanceToRepeatedLocation(): Int {
    val fullPath = fullWalkingPath()
    val (horizontal, vertical) = fullPath.first { location -> location in (fullPath - location) }
    return abs(horizontal) + abs(vertical)
}

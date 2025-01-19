package year2024.day14

import utils.println
import utils.readInput
import year2024.day14.Robot.Quadrant.*

fun main() {
    fun part1(input: List<String>, gridX: Int = 101, gridY: Int = 103): Long = input.toRobots(gridX, gridY)
        .map { robot -> robot.patrol(100) }
        .safetyFactor()

    fun part2(input: List<String>, gridX: Int = 101, gridY: Int = 103): Long {
        val candidates = mutableListOf<Int>()
        var iteration = 0
        val robots = input.toRobots(gridX, gridY)
        while (candidates.size < 1) {
            val patrolled = robots.map { it.patrol(iteration) }

            if (patrolled.toStrings().contains("XXXXXXXXXXXX")) {
                candidates.add(iteration)
                println("\n\nIteration: $iteration")
                patrolled.toStrings().println()
            }
            iteration++
        }


        println(candidates)

        return 0L
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test", "2024")
    check(part1(testInput, 11, 7) == 12L)

    val input = readInput("Day14", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<Robot>.toStrings(): String {
    val map: Map<Pair<Int, Int>, Robot> = this.associateBy { it.x to it.y }
    return buildList {
        for (y in 0..103) {
            add(buildString {
                for (x in 0..101) {
                    if (map.containsKey(x to y)) append("X") else append(" ")
                }
            })
        }
    }
        .joinToString("\n")
}

private fun List<String>.toRobots(gridX: Int, gridY: Int): List<Robot> = this.map { Robot(it, gridX, gridY) }

private fun List<Robot>.safetyFactor(): Long = toQuadrants()
    .filterKeys { quadrant -> quadrant != Robot.Quadrant.AXIS }
    .values
    .reduce { a, b -> a * b }
    .toLong()

private fun List<Robot>.toQuadrants(): Map<Robot.Quadrant, Int> = groupingBy { it.getQuadrant() }.eachCount()

private data class Robot(val x: Int, val y: Int, val dx: Int, val dy: Int, val gridX: Int, val gridY: Int) {

    private fun moveTo(newX: Int, newY: Int): Robot = Robot(newX, newY, dx, dy, gridX, gridY)

    fun patrol(seconds: Int): Robot {
        val newX = (x + gridX * seconds + dx * seconds) % gridX
        val newY = (y + gridY * seconds + dy * seconds) % gridY
        return moveTo(newX, newY)
    }

    fun getQuadrant(): Quadrant {
        val middleX = (gridX - 1) / 2
        val middleY = (gridY - 1) / 2

        return when {
            x < middleX && y < middleY -> TOP_LEFT
            x > middleX && y < middleY -> TOP_RIGHT
            x < middleX && y > middleY -> BOTTOM_LEFT
            x > middleX && y > middleY -> BOTTOM_RIGHT
            else -> AXIS
        }
    }

    enum class Quadrant {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, AXIS
    }
}

private fun Robot(line: String, gridX: Int, gridY: Int): Robot {
    val (x, y) = line.substringAfter("p=").substringBefore(" ").split(",").map { it.toInt() }
    val (dx, dy) = line.substringAfter("v=").split(",").map { it.toInt() }
    return Robot(x, y, dx, dy, gridX, gridY)
}
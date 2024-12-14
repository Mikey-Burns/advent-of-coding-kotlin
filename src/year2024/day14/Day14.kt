package year2024.day14

import println
import readInput

fun main() {
    fun part1(input: List<String>, gridX: Int = 101, gridY: Int = 103): Long = input.toRobots(gridX, gridY)
        .map { robot -> robot.patrol(100) }
        .safetyFactor()

    fun part2(input: List<String>, gridX: Int = 101, gridY: Int = 103): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test", "2024")
    check(part1(testInput, 11, 7) == 12L)
    val testInput2 = readInput("Day14_test", "2024")
    check(part2(testInput2) == 0L)

    val input = readInput("Day14", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.toRobots(gridX: Int, gridY: Int): List<Robot> = this.map { Robot(it, gridX, gridY) }

private fun List<Robot>.safetyFactor(): Long = groupingBy { it.getQuadrant() }
    .eachCount()
    .filterKeys { quadrant -> quadrant != Robot.Quadrant.AXIS }
    .values
    .reduce { a, b -> a * b }
    .toLong()

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
            x < middleX && y < middleY -> Quadrant.TOP_LEFT
            x > middleX && y < middleY -> Quadrant.TOP_RIGHT
            x < middleX && y > middleY -> Quadrant.BOTTOM_LEFT
            x > middleX && y > middleY -> Quadrant.BOTTOM_RIGHT
            else -> Quadrant.AXIS
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
package year2022.day09

import utils.println
import utils.readInput
import utils.Directions
import utils.Point2D
import utils.plus

fun main() {
    fun part1(input: List<String>): Long {
        return input.toPoints().follow(2).size.toLong()
    }

    fun part2(input: List<String>): Long {
        return input.toPoints().follow(10).size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test", "2022")
    check(part1(testInput).also(::println) == 13L)
    val testInput2 = readInput("Day09_test", "2022")
    check(part2(testInput2).also(::println) == 1L)

    val input = readInput("Day09", "2022")
    part1(input).println()
    part2(input).println()
}

private fun Point2D.follow(other: Point2D): Point2D = when (other) {
    // Straight Up
    north().north() -> north()
    // Up Right
    north().north().east(), north().east().east(), north().north().east().east() -> north().east()
    // Straight Right
    east().east() -> east()
    // Down Right
    east().east().south(), east().south().south(), east().east().south().south() -> east().south()
    // Straight Down
    south().south() -> south()
    // Down Left
    south().south().west(), south().west().west(), south().south().west().west() -> south().west()
    // Straight Left
    west().west() -> west()
    // Up Left
    west().west().north(), west().north().north(), west().west().north().north() -> west().north()
    else -> this
}

private fun List<String>.toPoints(): List<Point2D> = this.flatMap { line ->
    List(line.drop(2).toInt()) {
        when (line.first()) {
            'U' -> Directions.NORTH
            'R' -> Directions.EAST
            'D' -> Directions.SOUTH
            'L' -> Directions.WEST
            else -> error("Invalid direction")
        }
    }
}

private fun List<Point2D>.follow(ropeLength: Int): Set<Point2D> =
    buildSet {
        this@follow.fold(List(ropeLength) { Point2D(0, 0) }) { rope, direction ->
            rope.drop(1).runningFold(rope.first() + direction) { head, tail -> tail.follow(head) }
                .also { add(it.last()) }
        }
    }


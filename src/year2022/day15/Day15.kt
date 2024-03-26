package year2022.day15

import println
import readInput
import utils.Point2D
import utils.manhattanDistance
import utils.plus
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>, row: Int): Long = input.toPointsMap().invalidPositionsInRow(row)

    fun part2(input: List<String>): Long {
        return 0L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test", "2022")
    check(part1(testInput, 10).also(::println) == 26L)
    val testInput2 = readInput("Day15_test", "2022")
    check(part2(testInput2).also(::println) == 0L)

    val input = readInput("Day15", "2022")
    part1(input, 2000000).println()
    part2(input).println()
}

private fun List<String>.toPointsMap(): Map<Point2D, Point2D> = this.associate { line ->
    val sensorX = line.substringAfter("Sensor at x=").substringBefore(",").toInt()
    val sensorY = line.substringAfter(", y=").substringBefore(":").toInt()
    val beaconX = line.substringAfter("is at x=").substringBefore(", ").toInt()
    val beaconY = line.substringAfter("beacon").substringAfter("y=").toInt()
    Point2D(sensorX, sensorY) to Point2D(beaconX, beaconY)
}

private fun Map<Point2D, Point2D>.invalidPositionsInRow(row: Int): Long {
    val beaconX = values.filter { it.y == row }.map { it.x }.toSet()
    val invalidX = entries.flatMap { (sensor, beacon) ->
        val manhattan = sensor.manhattanDistance(beacon)
        val toRow = abs(sensor.y - row)
        val offset = manhattan - toRow

        if (offset >= 0) (sensor.x - offset)..(sensor.x + offset) else emptySet()
    }
        .toSet()
    return (invalidX - beaconX).count().toLong()
}
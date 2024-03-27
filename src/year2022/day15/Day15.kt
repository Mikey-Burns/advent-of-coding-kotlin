package year2022.day15

import println
import readInput
import reduce
import utils.Point2D
import utils.manhattanDistance
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>, row: Int): Long = input.toPointsMap().invalidPositionsInRow(row).count().toLong()

    fun part2(input: List<String>, maxRow: Int): Long =
        input.toPointsMap().findBeacon(maxRow).let { (x, y) -> x * 4000000L + y }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test", "2022")
    check(part1(testInput, 10).also(::println) == 26L)
    val testInput2 = readInput("Day15_test", "2022")
    check(part2(testInput2, 20).also(::println) == 56000011L)

    val input = readInput("Day15", "2022")
    measureTime { part1(input, 2000000).println() }
        .also { println("Part 1 ran in: ${it.inWholeSeconds} seconds")}
    measureTime { part2(input, 4000000).println() }.also(::println)
        .also { println("Part 2 ran in: ${it.inWholeSeconds} seconds")}
}

private fun List<String>.toPointsMap(): Map<Point2D, Point2D> = this.associate { line ->
    val sensorX = line.substringAfter("Sensor at x=").substringBefore(",").toInt()
    val sensorY = line.substringAfter(", y=").substringBefore(":").toInt()
    val beaconX = line.substringAfter("is at x=").substringBefore(", ").toInt()
    val beaconY = line.substringAfter("beacon").substringAfter("y=").toInt()
    Point2D(sensorX, sensorY) to Point2D(beaconX, beaconY)
}

private fun Map<Point2D, Point2D>.invalidPositionsInRow(row: Int): Set<Int> {
    val beaconX = values.filter { it.y == row }.map { it.x }.toSet()
    val invalidX = entries.flatMap { (sensor, beacon) ->
        val manhattan = sensor.manhattanDistance(beacon)
        val toRow = abs(sensor.y - row)
        val offset = manhattan - toRow

        if (offset >= 0) (sensor.x - offset)..(sensor.x + offset) else emptySet()
    }
        .toSet()
    return (invalidX - beaconX)
}

private fun Point2D.excludedRanges(beacon: Point2D, max: Int): Map<Int, IntRange> {
    val manhattan = this.manhattanDistance(beacon)
    return (0..manhattan)
        .flatMap { yOffset ->
            val farLeft = (x - (manhattan - yOffset)).coerceAtLeast(0)
            val farRight = (x + (manhattan - yOffset)).coerceAtMost(max)
            val range = farLeft..farRight
            buildList {
                if (y - yOffset >= 0) {
                    add(y - yOffset to range)
                }
                if (y + yOffset <= max) {
                    add(y + yOffset to range)
                }
            }
        }
        .toMap()
}

private fun Map<Point2D, Point2D>.findBeacon(maxRow: Int): Point2D {
    val listOfMaps = entries.map { (sensor, beacon) -> sensor.excludedRanges(beacon, maxRow) }
    val (row, exclusions) = (0..maxRow)
        .associateWith { row ->
            listOfMaps.filter { it.containsKey(row) }
                .mapNotNull { it[row] }
                .reduce()
        }
        .maxBy { (_, excludedValues) -> excludedValues.size }
    val x = exclusions.first().last + 1
    return Point2D(x, row)
}
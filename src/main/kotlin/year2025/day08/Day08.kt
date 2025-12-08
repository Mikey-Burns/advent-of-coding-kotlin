package year2025.day08

import utils.Point3D
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>, numberOfConnections: Int = 1000): Long = input.toPoints()
        .calculateDistances()
        .take(numberOfConnections)
        .findCircuits()
        .take(3)
        .fold(1L) { acc, circuit -> acc * circuit.size }

    fun part2(input: List<String>): Long = input.toPoints()
        .calculateDistances()
        .findLastConnection(input.size)
        .let { (start, end) -> start.x.toLong() * end.x.toLong() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test", "2025")
    check(part1(testInput, 10) == 40L)
    val testInput2 = readInput("Day08_test", "2025")
    check(part2(testInput2) == 25272L)

    val input = readInput("Day08", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun List<String>.toPoints(): List<Point3D> = this.map {
    val (x, y, z) = it.split(",")
    Point3D(x.toInt(), y.toInt(), z.toInt())
}

private fun List<Point3D>.calculateDistances(): List<DistancePair> {
    return this.flatMapIndexed { index, point ->
        this.drop(index + 1).map { destination ->
            DistancePair(point, destination, point.distance(destination).toLong())
        }
    }
        .sortedBy { it.third }
}

private fun List<DistancePair>.findCircuits(): List<Circuit> {
    val circuits = mutableListOf<MutableSet<Point3D>>()
    this.forEach { distancePair -> circuits.updateCircuits(distancePair) }
    return circuits.sortedByDescending { it.size }
}

private fun List<DistancePair>.findLastConnection(numberOfPoints: Int): DistancePair {
    val circuits = mutableListOf<MutableSet<Point3D>>()

    this.forEach { distancePair ->
        circuits.updateCircuits(distancePair)
        if (circuits.any { circuit -> circuit.size == numberOfPoints }) return distancePair
    }

    throw IllegalStateException("Never made a full circuit!")
}

private fun MutableList<MutableSet<Point3D>>.updateCircuits(distancePair: DistancePair) = apply {
    val start = distancePair.first
    val end = distancePair.second
    val startCircuits = this.singleOrNull { circuit -> circuit.contains(start) }
    val endCircuits = this.singleOrNull { circuit -> circuit.contains(end) }

    when {
        startCircuits == null && endCircuits == null -> this.add(mutableSetOf(start, end))
        startCircuits == null && endCircuits != null -> endCircuits.add(start)
        startCircuits != null && endCircuits == null -> startCircuits.add(end)
        startCircuits != null && endCircuits != null -> {
            this.remove(startCircuits)
            this.remove(endCircuits)
            this.add((startCircuits + endCircuits).toMutableSet())
        }
    }
}

private typealias DistancePair = Triple<Point3D, Point3D, Long>
private typealias Circuit = Set<Point3D>
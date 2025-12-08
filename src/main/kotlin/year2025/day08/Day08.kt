package year2025.day08

import utils.Point3D
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>, numberOfConnections: Int = 1000): Long = input.toPoints()
//        .also(::println)
        .calculateDistances()
//        .also(::println)
        .take(numberOfConnections)
        .findCircuits()
//        .also(::println)
        .take(3)
        .fold(1L) { acc, circuit -> acc * circuit.size }

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test", "2025")
    check(part1(testInput, 10) == 40L)
    val testInput2 = readInput("Day08_test", "2025")
    check(part2(testInput2) == 0L)

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

    this.forEach { (start, end) ->
        val startCircuits = circuits.singleOrNull { circuit -> circuit.contains(start) }
        val endCircuits = circuits.singleOrNull { circuit -> circuit.contains(end) }

        when {
            startCircuits == null && endCircuits == null -> circuits.add(mutableSetOf(start, end))
            startCircuits == null && endCircuits != null -> endCircuits.add(start)
            startCircuits != null && endCircuits == null -> startCircuits.add(end)
            startCircuits != null && endCircuits != null -> {
                circuits.remove(startCircuits)
                circuits.remove(endCircuits)
                circuits.add((startCircuits + endCircuits).toMutableSet())
            }
        }
    }

    return circuits.sortedByDescending { it.size }
}

private typealias DistancePair = Triple<Point3D, Point3D, Long>
private typealias Circuit = Set<Point3D>
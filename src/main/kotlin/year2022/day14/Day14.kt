package year2022.day14

import utils.println
import utils.readInput
import utils.Point2D
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long = dropSand(input.formRocks())

    fun part2(input: List<String>): Long {
        val rocks = input.formRocks()
        val floor = rocks.maxOf { it.y } + 2
        return dropSand(rocks + (-2000..2500).map { x -> Point2D(x, floor) })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test", "2022")
    check(part1(testInput).also(::println) == 24L)
    val testInput2 = readInput("Day14_test", "2022")
    check(part2(testInput2).also(::println) == 93L)

    val input = readInput("Day14", "2022")
    part1(input).println()
    part2(input).println()
}

fun List<String>.formRocks(): Set<Point2D> = this.flatMap(String::formRocks).toSet()

fun String.formRocks(): Set<Point2D> = this.split(" -> ")
    .map { it.split(",").map(String::toInt).let { (x, y) -> Point2D(x, y) } }
    .zipWithNext()
    .flatMap { it.connectingPoints() }
    .toSet()

fun Pair<Point2D, Point2D>.connectingPoints(): Set<Point2D> = when {
    first.x != second.x -> (min(first.x, second.x)..max(first.x, second.x))
        .map { x -> Point2D(x, first.y) }

    else -> (min(first.y, second.y)..max(first.y, second.y))
        .map { y -> Point2D(first.x, y) }
}
    .toSet()

fun dropSand(rocks: Set<Point2D>): Long {
    val start = Point2D(500, 0)
    val sand = mutableSetOf<Point2D>()

    fun localSand(currentLocation: Point2D): Point2D? {
        val occupied = rocks + sand
        val candidate = occupied
            .filter { it.x == currentLocation.x }
            .filter { it.y > currentLocation.y }
            .minByOrNull { it.y }
            ?.north()
            ?: return null
        return when {
            candidate.south().west() !in occupied -> localSand(candidate.south().west())
            candidate.south().east() !in occupied -> localSand(candidate.south().east())
            else -> candidate.also { sand.add(candidate) }
        }
    }

    return generateSequence {
        if (start !in sand) {
            localSand(start)
        } else {
            null
        }
    }
        .count()
        .toLong()
}
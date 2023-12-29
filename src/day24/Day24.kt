package day24

import println
import readInput
import uniquePairs

private const val LOWER = 200000000000000L
private const val UPPER = 400000000000000L

fun main() {
    fun part1(input: List<String>, range: LongRange): Int {
        return input.map { Linear(it) }
            .intersectsInBounds(range)
            .count()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    check(part1(testInput, 7L..27L).also(::println) == 2)
    val testInput2 = readInput("Day24_test")
    check(part2(testInput2).also(::println) == 0)

    val input = readInput("Day24")
    part1(input, LOWER..UPPER).println()
    part2(input).println()
}

private data class Linear(val x: Long, val y: Long, val dx: Long, val dy: Long) {
    val m: Double = dy.toDouble() / dx
    val b: Double = y - (m * x)

    fun intersectPoint(other: Linear): Pair<Double, Double>? {
        if (m == other.m) return null
        val xIntersect: Double = (other.b - b) / (m - other.m)
        val yIntersect: Double = m * xIntersect + b
        return xIntersect to yIntersect
    }

    fun isPointInFuture(point: Pair<Double, Double>): Boolean {
        val validX = if (dx > 0) point.first > x else point.first < x
        val validY = if (dy > 0) point.second > y else point.second < y
        return validX && validY
    }
}

private fun Linear(line: String): Linear {
    val (positions, velocities) = line.split("@")
    val (x, y, _) = positions.split(", ").map { it.trim().toLong() }
    val (dx, dy, _) = velocities.split(", ").map { it.trim().toLong() }
    return Linear(x, y, dx, dy)
}

private fun Pair<Linear, Linear>.futureIntersectPoint(): Pair<Double, Double>? {
    val intersect = first.intersectPoint(second) ?: return null
    return if (first.isPointInFuture(intersect) && second.isPointInFuture(intersect)) intersect else null
}

fun Pair<Double, Double>.isInBounds(range: LongRange) = first > range.first.toDouble()
        && first < range.last.toDouble()
        && second > range.first.toDouble()
        && second < range.last.toDouble()

private fun List<Linear>.intersectsInBounds(range: LongRange): List<Pair<Double, Double>> =
    this.uniquePairs()
        .mapNotNull { it.futureIntersectPoint() }
        .filter { it.isInBounds(range) }
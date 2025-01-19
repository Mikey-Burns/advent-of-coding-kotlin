package year2023.day24

import utils.println
import utils.readInput
import utils.uniquePairs
import kotlin.math.abs
import kotlin.math.roundToLong

private const val LOWER = 200000000000000L
private const val UPPER = 400000000000000L
private const val MAX_OFFSET = 500L

fun main() {
    fun part1(input: List<String>, range: LongRange): Int {
        return input.map { Linear(it) }
            .intersectsInBounds(range)
            .count()
    }

    fun part2(input: List<String>): Long {
        return input.map { Linear(it) }
            .findRockPosition()
            .let { it.x + it.y + it.z }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    check(part1(testInput, 7L..27L).also(::println) == 2)
    val testInput2 = readInput("Day24_test")
    check(part2(testInput2).also(::println) == 47L)

    val input = readInput("Day24")
    part1(input, LOWER..UPPER).println()
    part2(input).println()
}

private data class Linear(val x: Long, val y: Long, val z: Long, val dx: Double, val dy: Double, val dz: Double) {
    val m: Double = dy / dx
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
    val (x, y, z) = positions.split(", ").map { it.trim().toLong() }
    val (dx, dy, dz) = velocities.split(", ").map { it.trim().toDouble() }
    return Linear(x, y, z, dx, dy, dz)
}

private fun Pair<Linear, Linear>.intersectPoint(): Pair<Double, Double>? = first.intersectPoint(second)

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

private fun List<Linear>.findRockPosition(): Linear {
    val xyOffsetCandidates = (0L..MAX_OFFSET)
        .flatMap { xOffset ->
            (0L..MAX_OFFSET)
                .flatMap { yOffset ->
                    setOf(
                        xOffset to yOffset,
                        xOffset to -yOffset,
                        -xOffset to yOffset,
                        -xOffset to -yOffset,
                    )
                }
        }
//    val xyOffsetCandidates = listOf(47L to -360L, -3L to 1L)
    val zOffsetCandidates = -MAX_OFFSET..MAX_OFFSET
//    val zOffsetCandidates = listOf(18, 2)

    return xyOffsetCandidates.firstNotNullOf { (xOffset, yOffset) ->
        val linears = this.map { it.copy(dx = it.dx - xOffset, dy = it.dy - yOffset) }
        val intersects = linears.zipWithNext().mapNotNull { it.intersectPoint() }
        if (!intersects.take(4).zipWithNext().all { it.first eq it.second }) return@firstNotNullOf null

        val intersect = (linears[0] to linears[1]).intersectPoint() ?: return@firstNotNullOf null

        val (z, zOffset) = zOffsetCandidates.firstNotNullOfOrNull { zOffset ->
            val zList = linears.map {
                val t = (intersect.first - it.x) / it.dx
                it.z + (t * (it.dz - zOffset))
            }
            if (zList.take(4).zipWithNext().all { it.first eq it.second }) zList.first() to zOffset else null
        } ?: return@firstNotNullOf null


        Linear(
            intersect.first.toLong(),
            intersect.second.toLong(),
            z.toLong(),
            xOffset.toDouble(),
            yOffset.toDouble(),
            zOffset.toDouble()
        )
    }
}

infix fun Double.eq(other: Double): Boolean {
    if (this.isNaN() || other.isNaN()) return false
    return abs(this.roundToLong() - other.roundToLong()) < 2
}

private infix fun Pair<Double, Double>.eq(other: Pair<Double, Double>): Boolean =
    this.first eq other.first && this.second eq other.second
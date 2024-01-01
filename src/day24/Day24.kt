package day24

import println
import readInput
import uniquePairs
import kotlin.math.abs

private const val LOWER = 200000000000000L
private const val UPPER = 400000000000000L
private const val MAX_OFFSET = 500L

fun main() {
    fun part1(input: List<String>, range: LongRange): Int {
        return input.map { Linear(it) }
            .intersectsInBounds(range)
            .count()
    }

    fun part2(input: List<String>, range: LongRange): Int {
        return input.map { Linear(it) }
            .findRockPosition(range)
            .let { it.x + it.y + it.z }
            .toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    check(part1(testInput, 7L..27L).also(::println) == 2)
    val testInput2 = readInput("Day24_test")
    check(part2(testInput2, 7L..27L).also(::println) == 47)

    val input = readInput("Day24")
    part1(input, LOWER..UPPER).println()
    part2(input, LOWER..UPPER).println()
}

private data class Linear(val x: Long, val y: Long, val z: Long, val dx: Long, val dy: Long, val dz: Long) {
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

    fun predictZ(time: Double, zOffset: Double): Double = (z + time * (dz - zOffset))
}

private fun Linear(line: String): Linear {
    val (positions, velocities) = line.split("@")
    val (x, y, z) = positions.split(", ").map { it.trim().toLong() }
    val (dx, dy, dz) = velocities.split(", ").map { it.trim().toLong() }
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

private fun List<Linear>.findRockPosition(range: LongRange): Linear {
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
    val zOffsetCandidates = -MAX_OFFSET..MAX_OFFSET

    return xyOffsetCandidates.firstNotNullOf { (xOffset, yOffset) ->
        val linears = this.map { it.copy(dx = it.dx - xOffset, dy = it.dy - yOffset) }
        val intersect = (linears[0] to linears[1]).intersectPoint() ?: return@firstNotNullOf null
        if (intersect.first % 1.0 != 0.0 || intersect.second % 1.0 != 0.0) return@firstNotNullOf null
        val target = Linear(intersect.first.toLong(), intersect.second.toLong(), 0, 0, 0, 0)
        if (linears.any { (it to target).intersectPoint() == null }) return@firstNotNullOf null


        val (z, zOffset) = zOffsetCandidates.firstNotNullOfOrNull { zOffset ->
            val zSet = linears.map {
                val t = (intersect.first - it.x) / it.dx
                it.z + (t * (it.dz - zOffset))
            }
                .toSet()
            if (zSet.size == 1) zSet.first() to zOffset else null
        } ?: return@firstNotNullOf null


        Linear(intersect.first.toLong(), intersect.second.toLong(), z.toLong(), xOffset, yOffset, zOffset)
    }
}

infix fun Double.eq(other: Double): Boolean = abs(this - other) < 0.00001
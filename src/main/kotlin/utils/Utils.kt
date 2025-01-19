package utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String, year: String = "2023") =
    Path("src/main/kotlin/year$year/${name.substringBefore("_").lowercase()}/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

// region LCM

fun Int.lcm(other: Int): Int {
    val larger = maxOf(this, other)
    val maxLcm = this * other
    var candidate = larger
    while (candidate <= maxLcm) {
        if (candidate % this == 0 && candidate % other == 0) return candidate
        candidate += larger
    }
    return maxLcm
}

fun List<Int>.lcm(): Int = this.fold(1, Int::lcm)

fun Long.lcm(other: Long): Long {
    val larger = maxOf(this, other)
    val maxLcm = this * other
    var candidate = larger
    while (candidate <= maxLcm) {
        if (candidate % this == 0L && candidate % other == 0L) return candidate
        candidate += larger
    }
    return maxLcm
}

fun List<Long>.lcm(): Long = this.fold(1, Long::lcm)

// endregion

operator fun <T> List<List<T>>.get(point: Point2D): T = this[point.y][point.x]

fun <T> Collection<T>.uniquePairs(): List<Pair<T, T>> = this.flatMapIndexed { index, first ->
    this.filterIndexed { innerIndex, _ -> index < innerIndex }
        .map { second -> first to second }
}

fun List<IntRange>.reduce(): List<IntRange> = this.sortedBy { it.first }
    .let { sorted ->
        if (size == 1) return this
        sorted.drop(1).fold(mutableListOf(sorted.first())) { reduced, range ->
            val lastRange = reduced.last()
            if (range.first <= lastRange.last + 1)
                reduced[reduced.lastIndex] = (lastRange.first..maxOf(lastRange.last, range.last))
            else
                reduced.add(range)
            reduced
        }
    }
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

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

data class Point2D(val x: Int, val y: Int) {
    fun east(): Point2D = copy(x = x + 1)
    fun west(): Point2D = copy(x = x - 1)
    fun north(): Point2D = copy(y = y - 1)
    fun south(): Point2D = copy(y = y + 1)

    fun step(direction: Compass): Point2D = when (direction) {
        Compass.NORTH -> north()
        Compass.EAST -> east()
        Compass.SOUTH -> south()
        Compass.WEST -> west()
    }
}

operator fun Point2D.plus(other: Point2D): Point2D = Point2D(x + other.x, y + other.y)

enum class Compass {
    NORTH, EAST, SOUTH, WEST
}

fun List<List<*>>.isInBounds(location: Point2D) = location.y in this.indices
        && location.x in this[location.y].indices
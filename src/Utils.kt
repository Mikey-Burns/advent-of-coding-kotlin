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
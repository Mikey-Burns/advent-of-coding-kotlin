package year2016.day14

import utils.md5
import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = findKeys(input[0])

    fun part2(input: List<String>): Int = findKeys(input[0], 2016)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test", "2016")
    check(part1(testInput) == 22728)
    val testInput2 = readInput("Day14_test", "2016")
    check(part2(testInput2) == 22551)

    val input = readInput("Day14", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun findKeys(salt: String, stretches: Int = 0): Int {
    // This list of keys that needs to reach 64
    val keys = mutableListOf<Int>()
    // The hashes we've calculated with their source index as key
    val hashes = mutableMapOf<Int, String>()

    // Helper to convert an index to a hash
    // Try to get the value from our cache, or calculate it and cache it
    fun Int.indexToMd5(): String = hashes.getOrPut(this) {
        var md5 = (salt + this).md5()
        // Repeat if we're doing hash stretching
        repeat(stretches) {
            md5 = md5.md5()
        }
        md5
    }

    val threeRegex = Regex("(\\w)\\1\\1")
    var index = 0

    while (keys.size < 64) {
        val md5 = index.indexToMd5()
        val match = threeRegex.find(md5)
        if (match != null) {
            val fiveRepeat = match.value[0].toString().repeat(5)
            if ((1..1000).any { offset -> (index + offset).indexToMd5()
                .contains(fiveRepeat)
            }) {
                keys.add(index)
            }
        }
        index++
    }

    return keys.last()
}
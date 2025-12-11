package year2025.day11

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Long = ServerRack(input).dfsCount("you", "out")

    fun part2(input: List<String>): Long = ServerRack(input).transformedPaths()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test", "2025")
    check(part1(testInput) == 5L)
    val testInput2 = readInput("Day11_test2", "2025")
    check(part2(testInput2) == 2L)

    val input = readInput("Day11", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private data class ServerRack(val map: Map<String, List<String>>) {

    fun dfsCount(
        start: String,
        end: String,
        memoization: MutableMap<String, Long> = mutableMapOf()
    ): Long {
        if (start == end) return 1L
        return memoization.getOrPut(start) {
            map.getValue(start).sumOf { next ->
                dfsCount(next, end, memoization)
            }
        }
    }


    fun transformedPaths(): Long {
        val fftToDac = dfsCount("fft", "dac")
        val dacToFft = dfsCount("dac", "fft")

        return if (fftToDac > 0L) {
            val svrToFft = dfsCount("svr", "fft")
            val dacToOut = dfsCount("dac", "out")

            svrToFft * fftToDac * dacToOut
        } else {
            val svrToDac = dfsCount("svr", "dac")
            val fftToOut = dfsCount("fft", "out")

            svrToDac * dacToFft * fftToOut
        }
    }
}

private fun ServerRack(input: List<String>): ServerRack = input.associate { line ->
    val split = line.split(" ")
    val from = split.first().substringBefore(":")
    val dest = split.drop(1)

    from to dest
}
    .toMutableMap()
    .apply { put("out", emptyList()) }
    .let(::ServerRack)
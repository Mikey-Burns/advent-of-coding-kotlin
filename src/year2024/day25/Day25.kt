package year2024.day25

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val locks = mutableListOf<List<String>>()
        val keys = mutableListOf<List<String>>()
        input.chunked(8) { list ->
            if (list[0].all { it == '#' }) locks.add(list.take(7)) else keys.add(list.take(7))
        }
        val lockSizes = locks.map { lock ->
            lock[0].indices.map { columnIndex ->
                lock.drop(1).takeWhile { line -> line[columnIndex] == '#' }.size
            }
        }
        val keySizes = keys.map { lock ->
            lock[0].indices.map { columnIndex ->
                lock.dropLast(1).takeLastWhile { line -> line[columnIndex] == '#' }.size
            }
        }
        return lockSizes.sumOf { lock ->
            keySizes.count { key ->
                lock.zip(key)
                    .all { (lockCount, keyCount) -> lockCount + keyCount <= 5 }
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test", "2024")
    check(part1(testInput) == 3)

    val input = readInput("Day25", "2024")
    part1(input).println()
}
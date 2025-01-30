package year2016.day07

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.count { it.separate().supportsTls() }

    fun part2(input: List<String>): Int = input.count { it.separate().supportsSsl() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test", "2016")
    check(part1(testInput) == 2)
    val testInput2 = readInput("Day07_test2", "2016")
    check(part2(testInput2) == 3)

    val input = readInput("Day07", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun String.separate(): Pair<List<String>, List<String>> {
    val nonHyper = mutableListOf<String>()
    val hyper = mutableListOf<String>()
    this.split("[", "]")
        .forEachIndexed { index, string ->
            if (index % 2 == 0) nonHyper.add(string) else hyper.add(string)
        }
    return nonHyper to hyper
}

private fun Pair<List<String>, List<String>>.supportsTls(): Boolean {
    val isAbba = first.any(String::abba)
    val nonHyper = second.none(String::abba)
    return isAbba && nonHyper
}

private fun String.abba(): Boolean = "(\\w)(\\w)\\2\\1".toRegex().findAll(this)
    .any { matchResult -> matchResult.groupValues[1] != matchResult.groupValues[2] }

private fun Pair<List<String>, List<String>>.supportsSsl(): Boolean {
    val babList = first.flatMap {
        // Find all instances of aba
        // Use a positive lookahead in a capturing group (the ?=()) to handle overlapping groups
        // such as zazbz, so we get zaz and zbz as valid groups
        "(?=(\\w)(\\w)\\1)".toRegex().findAll(it)
            // Filter groups that are all one letter
            .filter { matchResult -> matchResult.groupValues[1] != matchResult.groupValues[2] }
            // Convert from aba to the desired bab string
            .map { matchResult -> matchResult.groupValues[2] + matchResult.groupValues[1] + matchResult.groupValues[2] }
            .toList()
    }
    return second.any { supernet -> babList.any { bab -> supernet.contains(bab) } }
}
package year2023.day09

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { parseLine(it) }
            .sumOf { nextValue(it) }
    }

    fun part2(input: List<String>): Int {
        return input.map { parseLine(it) }
            .sumOf { previousValue(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput).also(::println) == 114)
    val testInput2 = readInput("Day09_test")
    check(part2(testInput2).also(::println) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

fun List<Int>.allZeros() : Boolean = this.all { it == 0 }

fun List<Int>.differenceList() : List<Int> = this.windowed(2) { (a, b) -> b - a }

fun parseLine(line: String) : List<Int> = line.split(" ").map { it.toInt() }

fun nextValue(list: List<Int>): Int {
    if (list.allZeros()) return 0
    val differenceList = list.differenceList()
    return list.last() + nextValue(differenceList)
}

fun previousValue(list: List<Int>): Int {
    if (list.allZeros()) return 0
    val differenceList = list.differenceList()
    return list.first() - previousValue(differenceList)
}
package year2023.day12

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.map(::Line)
            .sumOf(Line::possibleMatchCount)
    }

    fun part2(input: List<String>): Long {
        return input.map(::expandedLine)
            .sumOf(Line::dynamicCount)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)
    val testInput2 = readInput("Day12_test")
    check(part2(testInput2).also(::println) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

data class Line(val rawSprings: String, val damagedList: List<Int>) {
    private val regex = damagedList.springRegex()
    fun possibleMatchCount(): Int {
        return rawSprings.replaceUnknowns()
            .count { it.matches(regex) }
    }

    fun dynamicCount(): Long {
        val booleanArray = damagedList.toBooleanArray()

        val lineLength = rawSprings.length + 1
        val targetSize = booleanArray.size

        val dynamicPoints = Array(lineLength) { LongArray(targetSize) { 0 } }
        val dynamicBroken = Array(lineLength) { LongArray(targetSize) { 0 } }

        dynamicPoints[0][0] = 1
        dynamicBroken[0][0] = 1

        for (indexInLine in 1..<lineLength) {
            val previousCharacter = rawSprings[indexInLine - 1]
            if (previousCharacter in ".?") {
                dynamicPoints[indexInLine][0] = dynamicPoints[indexInLine - 1][0]
                dynamicBroken[indexInLine][0] = dynamicBroken[indexInLine - 1][0]
            }

            for (indexInTargetPattern in 1..<targetSize) {
                fun handlePoint() {
                    if (!booleanArray[indexInTargetPattern]) {
                        dynamicPoints[indexInLine][indexInTargetPattern] += dynamicPoints[indexInLine - 1][indexInTargetPattern]
                        dynamicBroken[indexInLine][indexInTargetPattern] += dynamicPoints[indexInLine - 1][indexInTargetPattern]
                    }
                }

                fun handleBrokenSpring() {
                    if (booleanArray[indexInTargetPattern]) {
                        dynamicPoints[indexInLine][indexInTargetPattern] += dynamicBroken[indexInLine - 1][indexInTargetPattern - 1]
                        dynamicBroken[indexInLine][indexInTargetPattern] += dynamicBroken[indexInLine - 1][indexInTargetPattern - 1]
                    } else {
                        dynamicPoints[indexInLine][indexInTargetPattern] += dynamicBroken[indexInLine][indexInTargetPattern - 1]
                    }
                }

                when (previousCharacter) {
                    '.' -> handlePoint()
                    '#' -> handleBrokenSpring()
                    '?' -> {
                        handlePoint()
                        handleBrokenSpring()
                    }
                }
            }
        }

        return dynamicPoints.last().last()
    }
}

fun Line(line: String): Line {
    val (rawSprings, list) = line.split(" ")
    val damagedList = list.split(",").map { it.toInt() }
    return Line(rawSprings, damagedList)
}

fun expandedLine(line: String): Line {
    val (unexpandedSprings, list) = line.split(" ")
    val expandedSprings = List(5) { unexpandedSprings }.joinToString("?")
    val unexpandedList = List(5) { list }.joinToString(",")
    val expandedList = unexpandedList.split(",").map { it.toInt() }
    return Line(expandedSprings, expandedList)
}

fun List<Int>.springRegex(): Regex = joinToString(
    separator = "\\.+",
    prefix = "\\.*",
    postfix = "\\.*"
) { numSprings -> "#{$numSprings}" }.toRegex()

fun List<Int>.toBooleanArray(): BooleanArray {
    val targetPattern = this.toTargetPattern()
    return BooleanArray(targetPattern.size) { targetPattern[it] == 1 }
}

fun List<Int>.toTargetPattern(): List<Int> =
    this.joinToString(
        separator = "0",
        prefix = "0",
        postfix = "0",
        transform = "1"::repeat
    )
        .map(Char::digitToInt)

fun String.replaceUnknowns(): List<String> {
    return if (!this.contains("?")) listOf(this)
    else listOf(
        this.replaceFirst("?", "."),
        this.replaceFirst("?", "#")
    )
        .flatMap { it.replaceUnknowns() }
}
package year2023.day13

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return makePatterns(input)
            .sumOf { it.mirrorValue() }
    }

    fun part2(input: List<String>): Int {
        return makePatterns(input)
            .sumOf { it.newMirrorValue() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput).also(::println) == 405)
    val testInput2 = readInput("Day13_test")
    check(part2(testInput2).also(::println) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

data class Pattern(val rows: List<String>) {
    private val numColumns = rows[0].length

    override fun toString(): String {
        return rows.joinToString("\n")
    }

    private fun column(index: Int): String {
        return rows.map { it[index] }.joinToString("")
    }

    fun mirrorValue(pleaseDoNotBe: Int = 0): Int {
        val rowScores = (0..<rows.lastIndex)
            .filter(::isMirrorRow)
            .map { (it + 1) * 100 }
        val columnScores = (0..<numColumns)
            .filter(::isMirrorColumn)
            .map { it + 1 }
        return (rowScores + columnScores + 0)
            .first { it != pleaseDoNotBe }
    }

    fun newMirrorValue(): Int {
        val originalValue = mirrorValue()
        for (rowIndex in rows.indices) {
            for (columnIndex in rows[0].indices) {
                val pattern = Pattern(rows.flipMirror(rowIndex, columnIndex))
                val newValue = pattern.mirrorValue(originalValue)
                if (newValue != 0 && newValue != originalValue) {
                    return newValue
                }
            }
        }
        return originalValue
    }

    private fun isMirrorRow(index: Int): Boolean {
        var top = index
        var bottom = index + 1

        // Emergency check
        if (bottom > rows.lastIndex) return false

        while (top >= 0 && bottom <= rows.lastIndex) {
            if (rows[top] != rows[bottom]) {
                return false
            }
            top--
            bottom++
        }
        return true
    }

    private fun isMirrorColumn(index: Int): Boolean {
        var left = index
        var right = index + 1

        // Emergency check
        if (right == numColumns) return false

        while (left >= 0 && right < numColumns) {
            if (column(left) != column(right)) {
                return false
            }
            left--
            right++
        }
        return true
    }
}

fun makePatterns(input: List<String>): List<Pattern> {
    return input.withIndex()
        .filter { (_, line) -> line.isEmpty() }
        .map { it.index }
        .let { listOf(-1) + it + (input.lastIndex + 1) }
        .windowed(2)
        .map { (before, after) -> input.subList(before + 1, after) }
        .map(::Pattern)
}

fun List<String>.flipMirror(rowIndex: Int, columnIndex: Int): List<String> {
    val mutableList = this.toMutableList()
    val rowToReplace = mutableList[rowIndex].toMutableList()
    rowToReplace[columnIndex] = if (rowToReplace[columnIndex] == '.') '#' else '.'
    mutableList[rowIndex] = rowToReplace.joinToString("")
    return mutableList.toList()
}
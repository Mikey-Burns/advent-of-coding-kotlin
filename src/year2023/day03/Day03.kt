package year2023.day03

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.flatMapIndexed { index, line ->
            findNumbersAndNeighbors(line, input.getOrNull(index - 1), input.getOrNull(index + 1), index)
        }
            .sumOf { it.valueIfNextToSymbol }
    }

    fun part2(input: List<String>): Int {
        return input.flatMapIndexed { index, line ->
            findNumbersAndNeighbors(line, input.getOrNull(index - 1), input.getOrNull(index + 1), index)
        }
            .filter { it.gears.isNotEmpty() }
            .groupBy { it.gears.first() }
            .filter { it.value.size == 2 }
            .values
            .sumOf { it[0].valueIfNextToSymbol * it[1].valueIfNextToSymbol }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    val testInput2 = readInput("Day03_test")
    val part2 = part2(testInput2)
    check(part2 == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

fun findNumbersAndNeighbors(line: String, above: String?, below: String?, lineIndex: Int): List<NumberAndSymbols> {
    return Regex("(\\d+)").findAll(line)
        .toList()
        .map { match ->
            val baseIndex = match.range.first
            val left = (baseIndex - 1).coerceAtLeast(0)
            val right = (match.range.last + 2).coerceAtMost(line.lastIndex)

            val neighborsAbove: List<Char> = above?.substring(left, right)?.toList() ?: listOf()
            val neighborsBelow: List<Char> = below?.substring(left, right)?.toList() ?: listOf()
            val neighborsSide: List<Char> = listOf(line[left], line[right - 1])

            val chars = neighborsAbove + neighborsBelow + neighborsSide

            val gearsAbove =
                neighborsAbove.mapIndexedNotNull { idx, c -> if (c == '*') Gear(left + idx, lineIndex - 1) else null }
            val gearsBelow =
                neighborsBelow.mapIndexedNotNull { idx, c -> if (c == '*') Gear(left + idx, lineIndex + 1) else null }
            val gearsSide = listOfNotNull(
                if (line[left] == '*') Gear(left, lineIndex) else null,
                if (line[right - 1] == '*') Gear(right - 1, lineIndex) else null,
            )

            val gears = gearsAbove + gearsBelow + gearsSide

            NumberAndSymbols(match.value.toInt(), chars, gears)
        }
}

fun findAllGears(line: String, above: String?, below: String?) {
    Regex("\\*").findAll(line)
}

data class NumberAndSymbols(val number: Int, val neighbors: List<Char>, val gears: List<Gear>) {
    val valueIfNextToSymbol: Int = if (neighbors.any(Char::isSymbol)) number else 0
}

data class Gear(val x: Int, val y: Int)

fun Char.isSymbol(): Boolean = !this.isLetterOrDigit() && this != '.'
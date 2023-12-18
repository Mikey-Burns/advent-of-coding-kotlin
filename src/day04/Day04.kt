package day04

import println
import readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { Card(it).score() }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { Card(it) }
        val instances = MutableList(input.size) { 1 }
        cards.forEachIndexed { index, card ->
            // Card ID is 1 above index, so we don't need +1
            // Upper bound is inclusive, so use index instead of ID to avoid -1
            IntRange(card.id, index + card.numWinners)
                .forEach { indexToUpdate ->
                    instances[indexToUpdate] += instances[index]
                }
        }
        return instances.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day04_test")
    check(part2(testInput2) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

data class Card(val id: Int, val winners: List<Int>, val mine: List<Int>) {
    val numWinners = mine.count { it in winners }
    fun score(): Int {
        return if (numWinners > 0) return 2.0.pow(numWinners - 1.0).toInt() else 0
    }
}

fun Card(input: String): Card {
    val cardSplit = input.split(":")
    val id = cardSplit[0].filter(Char::isDigit).toInt()
    val listSplit = cardSplit[1].split("|")
    val winners = listSplit[0].split(" ").mapNotNull(String::toIntOrNull)
    val mine = listSplit[1].split(" ").mapNotNull(String::toIntOrNull)
    return Card(id, winners, mine)
}

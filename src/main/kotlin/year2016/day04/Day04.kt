package year2016.day04

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.map(String::toRoom)
        .filter(Room::isValid)
        .sumOf { it.sectorId }

    fun part2(input: List<String>): Int = input.map(String::toRoom)
        .filter(Room::isValid)
        .single { room -> room.name.decrypt(room.sectorId).contains("northpole") }
        .sectorId


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test", "2016")
    check(part1(testInput) == 1514)
    check("qzmt-zixmtkozy-ivhz".decrypt(343) == "very encrypted name")

    val input = readInput("Day04", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun String.toRoom(): Room {
    this.replace("-", "")
    val name = this.takeWhile { !it.isDigit() }
    val sectorId = this.filter { it.isDigit() }.toInt()
    val checksum = this.substringAfter("[").substringBefore("]")
    return Room(name, sectorId, checksum)
}

private data class Room(val name: String, val sectorId: Int, val checksum: String) {

    fun isValid(): Boolean = name
        .filter { it != '-' }
        .groupingBy { it }
        .eachCount()
        .toList()
        .sortedWith(
            compareBy<Pair<Char, Int>> { (_, count) -> count }.reversed()
                .thenComparing { (letter, _) -> letter }
        )
        .take(5)
        .joinToString("") { (letter, _) -> letter.toString() }
        .let { it == checksum }
}

private fun String.decrypt(steps: Int): String = this.map { c ->
    if (c == '-') " " else c.rollForward(steps % 26)
}.joinToString("").trim()

private fun Char.rollForward(steps: Int): Char {
    var result = this
    repeat(steps) {
        result = if (result == 'z') 'a' else result + 1
    }
    return result
}
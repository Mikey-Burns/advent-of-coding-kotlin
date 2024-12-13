package year2024.day13

import println
import readInput
import kotlin.math.round

fun main() {
    fun part1(input: List<String>): Long = input.toGames().sumOf(Game::cheapestButtonComboCostViaMath)

    fun part2(input: List<String>): Long = input.toBigGames().sumOf(Game::cheapestButtonComboCostViaMath)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test", "2024")
    check(part1(testInput) == 480L)
    val testInput2 = readInput("Day13_test", "2024")
//    check(part2(testInput2) == 0L)

    val input = readInput("Day13", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.toGames(): List<Game> = this.windowed(3, 4, true) { (aLine, bLine, prizeLine) ->
    Game(aLine, bLine, prizeLine)
}

private fun List<String>.toBigGames(): List<Game> = this.windowed(3, 4, true) { (aLine, bLine, prizeLine) ->
    toBigGame(aLine, bLine, prizeLine)
}

private fun Game(aLine: String, bLine: String, prizeLine: String): Game {
    val ax = aLine.substringAfter("X+").substringBefore(",").toLong()
    val aY = aLine.substringAfter("Y+").toLong()
    val bx = bLine.substringAfter("X+").substringBefore(",").toLong()
    val bY = bLine.substringAfter("Y+").toLong()
    val prizeX = prizeLine.substringAfter("X=").substringBefore(",").toLong()
    val prizeY = prizeLine.substringAfter("Y=").toLong()

    return Game(ax, aY, bx, bY, prizeX, prizeY)
}

private fun toBigGame(aLine: String, bLine: String, prizeLine: String): Game {
    val ax = aLine.substringAfter("X+").substringBefore(",").toLong()
    val aY = aLine.substringAfter("Y+").toLong()
    val bx = bLine.substringAfter("X+").substringBefore(",").toLong()
    val bY = bLine.substringAfter("Y+").toLong()
    val prizeX = prizeLine.substringAfter("X=").substringBefore(",").toLong() + 10000000000000
    val prizeY = prizeLine.substringAfter("Y=").toLong() + 10000000000000

    return Game(ax, aY, bx, bY, prizeX, prizeY)
}

private data class Game(val aX: Long, val aY: Long, val bX: Long, val bY: Long, val prizeX: Long, val prizeY: Long) {

    private fun findWinningCombos(): List<Pair<Long, Long>> = buildList {
        for (a in 0..100L) {
            for (b in 0..100L) {
                if ((a * aX + b * bX == prizeX) && (a * aY + b * bY == prizeY)) add(a to b)
            }
        }
    }

    fun cheapestButtonComboCost(): Long = findWinningCombos()
        .also { if (it.isNotEmpty()) println(it.first()) }
        .minOfOrNull { (a, b) -> 3L * a + b }
        ?: 0L

    fun cheapestButtonComboCostViaMath(): Long {
        val b = round((prizeX - (prizeY * aX / aY.toDouble())) / (bX - (bY * aX / aY.toDouble())))
        val a = round((prizeY - (b * bY)) / aY.toDouble())

        return if ((a * aX + b * bX == prizeX.toDouble()) && (a * aY + b * bY == prizeY.toDouble())) {
            (3 * a + b).toLong()
        } else {
            0
        }
    }
}
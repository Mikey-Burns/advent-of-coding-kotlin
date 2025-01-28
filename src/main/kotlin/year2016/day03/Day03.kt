package year2016.day03

import utils.println
import utils.readInput
import kotlin.time.measureTime

private typealias Triangle = Triple<Int, Int, Int>

fun main() {
    fun part1(input: List<String>): Int = input.map { it.toTriple() }
        .map { it.sort() }
        .count { it.isValidTriangle() }

    fun part2(input: List<String>): Int = input.toVerticalTriangles()
        .map { it.sort() }
        .count { it.isValidTriangle() }


    // test if implementation meets criteria from the description, like:
    check(!Triple(5, 10, 25).isValidTriangle())
    val testInput = readInput("Day03_test", "2016")
    check(part2(testInput) == 6)

    val input = readInput("Day03", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun Triangle.sort(): Triangle =
    this.toList().sorted().let { (a, b, c) -> Triple(a, b, c) }

private fun Triangle.isValidTriangle(): Boolean = first + second > third

private fun String.toTriple() = this.split(" ").mapNotNull { it.toIntOrNull() }
    .let { (a, b, c) -> Triple(a, b, c) }

private fun List<String>.toVerticalTriangles(): List<Triangle> = this.chunked(3)
    .flatMap { (lineOne, lineTwo, lineThree) ->
        val splitOne = lineOne.split(" ").mapNotNull { it.toIntOrNull() }
        val splitTwo = lineTwo.split(" ").mapNotNull { it.toIntOrNull() }
        val splitThree = lineThree.split(" ").mapNotNull { it.toIntOrNull() }

        listOf(
            Triangle(splitOne[0], splitTwo[0], splitThree[0]),
            Triangle(splitOne[1], splitTwo[1], splitThree[1]),
            Triangle(splitOne[2], splitTwo[2], splitThree[2]),
        )
    }
package year2025.day12

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.parseInput()
        .validSections()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test", "2025")
//    check(part1(testInput) == 2)
    val testInput2 = readInput("Day12_test", "2025")
    check(part2(testInput2) == 0L)

    val input = readInput("Day12", "2025")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private data class Farm(val shapes: List<Shape>, val sections: List<Section>) {

    fun validSections(): Int = sections.count { section ->
        section.area > section.shapes.mapIndexed { index, count -> shapes[index].spaceConsumed * count }.sum()
    }
}


private data class Shape(val points: List<List<Char>>) {
    val spaceConsumed = points.sumOf { shape -> shape.count { c -> c == '#' } }
}

private data class Section(val x:Int, val y:Int, val shapes: List<Int>) {
    val area = x * y
}

private fun List<String>.parseInput(): Farm {
    val shapes = this.chunked(5) {
        lines -> lines.drop(1).dropLast(1).map { it.toList() }
    }.take(6)
        .map { points -> Shape(points) }

    val sections = this.takeLastWhile { it.isNotBlank() }
        .map {
            val split = it.split(" ")
            val x = split.first().substringBefore("x").toInt()
            val y = split.first().substringAfter("x").substringBefore(":").toInt()
            val shapes = split.drop(1).map(String::toInt)

            Section(x, y, shapes)
        }

    return Farm(shapes, sections)
}
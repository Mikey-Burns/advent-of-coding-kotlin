package day18

import Compass
import Point2D
import println
import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        return shoelaceDig(input.map(::DigLine))
    }

    fun part2(input: List<String>): Long {
        return input.map(::DigLine)
            .map(DigLine::fixLine)
            .let(::shoelaceDig)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput).also(::println) == 62L)
    val testInput2 = readInput("Day18_test")
    check(part2(testInput2).also(::println) == 952408144115L)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}

data class DigLine(val direction: Compass, val steps: Int, val color: String) {

    fun fixLine(): DigLine {
        val direction = when (color.takeLast(1).toInt()) {
            0 -> Compass.EAST
            1 -> Compass.SOUTH
            2 -> Compass.WEST
            3 -> Compass.NORTH
            else -> throw IllegalArgumentException("Bad direction")
        }
        val steps = color.dropLast(1).toInt(16)
        return DigLine(direction, steps, color)
    }
}

fun DigLine(line: String): DigLine {
    val (rawDirection, stepsChar, colorString) = line.split(" ")
    val direction = when (rawDirection) {
        "U" -> Compass.NORTH
        "R" -> Compass.EAST
        "D" -> Compass.SOUTH
        "L" -> Compass.WEST
        else -> throw IllegalArgumentException("Bad direction")
    }
    val steps = stepsChar.toInt()
    val color = colorString.filter { it !in "(#)" }
    return DigLine(direction, steps, color)
}

enum class DigSpace {
    HORIZONTAL, VERTICAL, CORNER_DOWN, CORNER_UP
}

@Suppress("unused")
fun bigDig(digs: List<DigLine>): Long {
    val digMap: MutableMap<Point2D, DigSpace> = mutableMapOf()
    var currentPoint = Point2D(0, 0)

    digs.forEachIndexed { index, digLine ->
        // Non-edge steps
        repeat(digLine.steps) { repeat ->
            currentPoint = currentPoint.step(digLine.direction)
                .also {
                    if (repeat < (digLine.steps - 1)) {
                        digMap[it] = when (digLine.direction) {
                            Compass.NORTH, Compass.SOUTH -> DigSpace.VERTICAL
                            Compass.EAST, Compass.WEST -> DigSpace.HORIZONTAL
                        }
                    } else {
                        val nextDig = digs[(index + 1) % digs.size]
                        digMap[it] = when (digLine.direction) {
                            Compass.NORTH -> DigSpace.CORNER_DOWN
                            Compass.SOUTH -> DigSpace.CORNER_UP
                            Compass.EAST, Compass.WEST ->
                                if (nextDig.direction == Compass.NORTH) DigSpace.CORNER_UP
                                else DigSpace.CORNER_DOWN
                        }
                    }
                }
        }
    }

    val minX = digMap.keys.minOf { it.x }
    val maxX = digMap.keys.maxOf { it.x }
    val minY = digMap.keys.minOf { it.y }
    val maxY = digMap.keys.maxOf { it.y }

    var matches = 0L
    for (x in minX..maxX) {
        for (y in minY..maxY) {
            val candidate = Point2D(x, y)
            if (digMap.containsKey(candidate)) {
                matches++
            } else {
                var collisions = 0
                for (offset in 1..(maxX - x)) {
                    val target = Point2D(x + offset, y)
                    // Try to hit a vertical space or a corner turning up
                    if (digMap[target] in listOf(DigSpace.VERTICAL, DigSpace.CORNER_UP)) collisions++
                }
                if (collisions % 2 == 1) matches++
            }
        }
    }

    return matches
}

fun shoelaceDig(digs: List<DigLine>): Long {
    var x = 0L
    var y = 0L

    // Add the first point to the back as well
    val shoelace = (digs + digs.first()).windowed(2) { (_, end) ->
        val (nextX, nextY) = when (end.direction) {
            Compass.NORTH -> x to (y + end.steps)
            Compass.EAST -> (x + end.steps) to y
            Compass.SOUTH -> x to (y - end.steps)
            Compass.WEST -> (x - end.steps) to y
        }
        val determinant = (x * nextY) - (y * nextX)
//        println("x1: $x, y1: $y, x2: $nextX, y2: $nextY, determinant: $determinant")
        x = nextX
        y = nextY
        determinant
    }
        .sum()
        .let { abs(it / 2) }

    // Include the outline of the shape, off by 1 because of corners
    val outline = digs.sumOf { it.steps } / 2 + 1

    return shoelace + outline
}
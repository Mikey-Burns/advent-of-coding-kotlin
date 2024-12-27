package year2024.day21

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = input.partOne().toLong()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test", "2024")
    check(part1(testInput) == 126384L)
    val testInput2 = readInput("Day21_test", "2024")
    check(part2(testInput2) == 0L)

    val input = readInput("Day21", "2024")
    part1(input).println()
    part2(input).println()
}

fun String.keypadToDirections(): String = buildString {
    val zip = ("A" + this@keypadToDirections).zipWithNext()
    zip.forEach {
            (start, end) ->

        when(start) {
            '0' ->
                when(end) {
                    '0' -> Unit
                    '1' -> append("^<")
                    '2' -> append("^")
                    '3' -> append("^>")
                    '4' -> append("^^<")
                    '5' -> append("^^")
                    '6' -> append("^^>")
                    '7' -> append("^^^<")
                    '8' -> append("^^^")
                    '9' -> append("^^^>")
                    'A' -> append(">")
                }

            '1' ->
                when(end) {
                    '0' -> append(">v")
                    '1' -> Unit
                    '2' -> append(">")
                    '3' -> append(">>")
                    '4' -> append("^")
                    '5' -> append("^>")
                    '6' -> append("^>>")
                    '7' -> append("^^")
                    '8' -> append("^^>")
                    '9' -> append("^^>>")
                    'A' -> append(">>v")
                }

            '2' ->
                when(end) {
                    '0' -> append("v")
                    '1' -> append("<")
                    '2' -> Unit
                    '3' -> append(">")
                    '4' -> append("<^")
                    '5' -> append("^")
                    '6' -> append(">^")
                    '7' -> append("<^^")
                    '8' -> append("^^")
                    '9' -> append(">^^")
                    'A' -> append(">v")
                }

            '3' ->
                when(end) {
                    '0' -> append("v<")
                    '1' -> append("<<")
                    '2' -> append("<")
                    '3' -> Unit
                    '4' -> append("^<<")
                    '5' -> append("^<")
                    '6' -> append("^")
                    '7' -> append("<<^^")
                    '8' -> append("^^<")
                    '9' -> append("^^")
                    'A' -> append("v")
                }

            '4' ->
                when(end) {
                    '0' -> append("vv>")
                    '1' -> append("v")
                    '2' -> append("v>")
                    '3' -> append(">>v")
                    '4' -> Unit
                    '5' -> append(">")
                    '6' -> append(">>")
                    '7' -> append("^")
                    '8' -> append("^>")
                    '9' -> append("^>>")
                    'A' -> append(">>vv")
                }

            '5' ->
                when(end) {
                    '0' -> append("vv")
                    '1' -> append("v<")
                    '2' -> append("v")
                    '3' -> append("v>")
                    '4' -> append("<")
                    '5' -> Unit
                    '6' -> append(">")
                    '7' -> append("^<")
                    '8' -> append("^")
                    '9' -> append("^>")
                    'A' -> append(">vv")
                }

            '6' ->
                when(end) {
                    '0' -> append("vv<")
                    '1' -> append("v<<")
                    '2' -> append("<v")
                    '3' -> append("v")
                    '4' -> append("<<")
                    '5' -> append("<")
                    '6' -> Unit
                    '7' -> append("^<<")
                    '8' -> append("^<")
                    '9' -> append("^")
                    'A' -> append("vv")
                }

            '7' ->
                when(end) {
                    '0' -> append(">vvv")
                    '1' -> append("vv")
                    '2' -> append(">vv")
                    '3' -> append(">>vv")
                    '4' -> append("v")
                    '5' -> append(">v")
                    '6' -> append(">>v")
                    '7' -> Unit
                    '8' -> append(">")
                    '9' -> append(">>")
                    'A' -> append(">>vvv")
                }

            '8' ->
                when(end) {
                    '0' -> append("vvv")
                    '1' -> append("<vv")
                    '2' -> append("vv")
                    '3' -> append(">vv")
                    '4' -> append("<v")
                    '5' -> append("v")
                    '6' -> append(">v")
                    '7' -> append("<")
                    '8' -> Unit
                    '9' -> append(">")
                    'A' -> append(">vvv")
                }

            '9' ->
                when(end) {
                    '0' -> append("<vvv")
                    '1' -> append("<<vv")
                    '2' -> append("<vv")
                    '3' -> append("vv")
                    '4' -> append("<<v")
                    '5' -> append("<v")
                    '6' -> append("v")
                    '7' -> append("<<")
                    '8' -> append("<")
                    '9' -> Unit
                    'A' -> append("vvv")
                }

            'A' ->
                when(end) {
                    '0' -> append("<")
                    '1' -> append("^<<")
                    '2' -> append("^<")
                    '3' -> append("^")
                    '4' -> append("^^<<")
                    '5' -> append("^^<")
                    '6' -> append("^^")
                    '7' -> append("^^^<<")
                    '8' -> append("^^^<")
                    '9' -> append("^^^")
                    'A' -> Unit
                }
        }
        append("A")
    }
}

fun String.directionsToDirections() = buildString {
    ("A" + this@directionsToDirections).zipWithNext().forEach{
            (start, end) -> when(start) {
        'A' -> when(end) {
            'A' -> Unit
            '^' -> append("<")
            '<' -> append("v<<")
            'v' -> append("v<")
            '>' -> append("v")
        }
        '^' -> when(end) {
            'A' -> append(">")
            '^' -> Unit
            '<' -> append("v<")
            'v' -> append("v")
            '>' -> append("v>")
        }
        '<' -> when(end) {
            'A' -> append(">>^")
            '^' -> append(">^")
            '<' -> Unit
            'v' -> append(">")
            '>' -> append(">>")
        }
        'v' -> when(end) {
            'A' -> append(">^")
            '^' -> append("^")
            '<' -> append("<")
            'v' -> Unit
            '>' -> append(">")
        }
        '>' -> when(end) {
            'A' -> append("^")
            '^' -> append("<^")
            '<' -> append("<<")
            'v' -> append("<")
            '>' -> Unit
        }
    }
        append("A")
    }
}

fun List<String>.partOne() : Int {
    return this.sumBy{ line ->
        //println(line)
        val numeric = line.dropLast(1).toInt()
        var directions = line.keypadToDirections()
        repeat (2) {
            //println(directions)
            directions = directions.directionsToDirections()
        }
        //println(directions)
        //println("${directions.length} * $numeric = ${numeric * directions.length}")

        (numeric * directions.length)
        //.also(::println)

    }
}
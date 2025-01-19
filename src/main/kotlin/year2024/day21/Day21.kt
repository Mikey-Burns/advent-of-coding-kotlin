package year2024.day21

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input.sumOf { code -> code.complexity(2) }

    fun part2(input: List<String>): Long = input.sumOf { code -> code.complexity(25) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test", "2024")
    check(part1(testInput) == 126384L)
    val testInput2 = readInput("Day21_test", "2024")
    check(part2(testInput2) == 154115708116294L)

    val input = readInput("Day21", "2024")
    part1(input).println()
    part2(input).println()
}

private fun directionToDirection(start: Char, end: Char) = when (start) {
    'A' -> when (end) {
        'A' -> listOf("")
        '^' -> listOf("<")
        '<' -> listOf("v<<", "<v<")
        'v' -> listOf("v<", "<v")
        '>' -> listOf("v")
        else -> error("Invalid character")
    }

    '^' -> when (end) {
        'A' -> listOf(">")
        '^' -> listOf("")
        '<' -> listOf("v<")
        'v' -> listOf("v")
        '>' -> listOf("v>", ">v")
        else -> error("Invalid character")
    }

    '<' -> when (end) {
        'A' -> listOf(">>^", ">^>")
        '^' -> listOf(">^")
        '<' -> listOf("")
        'v' -> listOf(">")
        '>' -> listOf(">>")
        else -> error("Invalid character")
    }

    'v' -> when (end) {
        'A' -> listOf(">^", "^>")
        '^' -> listOf("^")
        '<' -> listOf("<")
        'v' -> listOf("")
        '>' -> listOf(">")
        else -> error("Invalid character")
    }

    '>' -> when (end) {
        'A' -> listOf("^")
        '^' -> listOf("<^", "^<")
        '<' -> listOf("<<")
        'v' -> listOf("<")
        '>' -> listOf("")
        else -> error("Invalid character")
    }

    else -> error("Invalid character")
}
    .map { directions -> directions + "A" }

private fun keypadToDirection(start: Char, end: Char) = when (start) {
    '0' -> when (end) {
        '0' -> listOf("")
        '1' -> listOf("^<")
        '2' -> listOf("^")
        '3' -> listOf("^>", ">^")
        '4' -> listOf("^^<", "^<^")
        '5' -> listOf("^^")
        '6' -> listOf("^^>", "^>^", ">^^")
        '7' -> listOf("^^^<", "^^<^", "^<^^")
        '8' -> listOf("^^^")
        '9' -> listOf("^^^>", "^^>^", "^>^^", ">^^^")
        'A' -> listOf(">")
        else -> error("Invalid character")
    }

    '1' -> when (end) {
        '0' -> listOf(">v")
        '1' -> listOf("")
        '2' -> listOf(">")
        '3' -> listOf(">>")
        '4' -> listOf("^")
        '5' -> listOf("^>", ">^")
        '6' -> listOf("^>>", ">^>", ">>^")
        '7' -> listOf("^^")
        '8' -> listOf("^^>", "^>^", ">^^")
        '9' -> listOf("^^>>", "^>^>", "^>>^", ">>^^")
        'A' -> listOf(">>v", ">v>")
        else -> error("Invalid character")
    }

    '2' -> when (end) {
        '0' -> listOf("v")
        '1' -> listOf("<")
        '2' -> listOf("")
        '3' -> listOf(">")
        '4' -> listOf("<^", "^<")
        '5' -> listOf("^")
        '6' -> listOf(">^", "^>")
        '7' -> listOf("<^^", "^<^", "^^<")
        '8' -> listOf("^^")
        '9' -> listOf(">^^", "^>^", "^^>")
        'A' -> listOf(">v", "v>")
        else -> error("Invalid character")
    }

    '3' -> when (end) {
        '0' -> listOf("v<", "<v")
        '1' -> listOf("<<")
        '2' -> listOf("<")
        '3' -> listOf("")
        '4' -> listOf("^<<", "<^<", "<<^")
        '5' -> listOf("^<", "<^")
        '6' -> listOf("^")
        '7' -> listOf("<<^^", "<^<^", "<^^<", "^^<<", "^<^<", "^<<^")
        '8' -> listOf("^^<", "^<^", "<^^")
        '9' -> listOf("^^")
        'A' -> listOf("v")
        else -> error("Invalid character")
    }

    '4' -> when (end) {
        '0' -> listOf("vv>", "v>v")
        '1' -> listOf("v")
        '2' -> listOf("v>")
        '3' -> listOf(">>v", ">v>", "v>>")
        '4' -> listOf("")
        '5' -> listOf(">")
        '6' -> listOf(">>")
        '7' -> listOf("^")
        '8' -> listOf("^>", ">^")
        '9' -> listOf("^>>", ">^>", ">>^")
        'A' -> listOf(">>vv", ">v>v", ">vv>")
        else -> error("Invalid character")
    }

    '5' -> when (end) {
        '0' -> listOf("vv")
        '1' -> listOf("v<", "<v")
        '2' -> listOf("v")
        '3' -> listOf("v>")
        '4' -> listOf("<")
        '5' -> listOf("")
        '6' -> listOf(">")
        '7' -> listOf("^<", "<^")
        '8' -> listOf("^")
        '9' -> listOf("^>")
        'A' -> listOf(">vv", "v>v", "vv>")
        else -> error("Invalid character")
    }

    '6' -> when (end) {
        '0' -> listOf("vv<", "v<v", "<vv")
        '1' -> listOf("v<<", "<v<", "<<v")
        '2' -> listOf("<v", "v<")
        '3' -> listOf("v")
        '4' -> listOf("<<")
        '5' -> listOf("<")
        '6' -> listOf("")
        '7' -> listOf("^<<", "<^<", "<<^")
        '8' -> listOf("^<", "<^")
        '9' -> listOf("^")
        'A' -> listOf("vv")
        else -> error("Invalid character")
    }

    '7' -> when (end) {
        '0' -> listOf(">vvv", "v>vv", "vv>v")
        '1' -> listOf("vv")
        '2' -> listOf(">vv", "v>v", "vv>")
        '3' -> listOf(">>vv", ">v>v", ">vv>", "vv>>", "v>>v", "v>v>")
        '4' -> listOf("v")
        '5' -> listOf(">v", "v>")
        '6' -> listOf(">>v", ">v>", "v>>")
        '7' -> listOf("")
        '8' -> listOf(">")
        '9' -> listOf(">>")
        'A' -> listOf(
            ">>vvv",
            ">v>vv", ">vv>v", ">vvv>",
            "v>vv>", "v>>vv", "v>v>v",
            "vv>>v", "vv>v>"
        )

        else -> error("Invalid character")
    }

    '8' -> when (end) {
        '0' -> listOf("vvv")
        '1' -> listOf("<vv", "v<v", "vv<")
        '2' -> listOf("vv")
        '3' -> listOf(">vv", ">v>", "vv>")
        '4' -> listOf("<v", "v<")
        '5' -> listOf("v")
        '6' -> listOf(">v", "v>")
        '7' -> listOf("<")
        '8' -> listOf("")
        '9' -> listOf(">")
        'A' -> listOf(">vvv", "v>vv", "vv>v", "vvv>")
        else -> error("Invalid character")
    }

    '9' -> when (end) {
        '0' -> listOf("<vvv", "v<vv", "vv<v", "vvv<")
        '1' -> listOf("<<vv", "<v<v", "<vv<", "v<v<", "v<<v", "vv<<")
        '2' -> listOf("<vv", "v<v", "vv<")
        '3' -> listOf("vv")
        '4' -> listOf("<<v", "<v<", "v<<")
        '5' -> listOf("<v", "v<")
        '6' -> listOf("v")
        '7' -> listOf("<<")
        '8' -> listOf("<")
        '9' -> listOf("")
        'A' -> listOf("vvv")
        else -> error("Invalid character")
    }

    'A' -> when (end) {
        '0' -> listOf("<")
        '1' -> listOf("^<<", "<^<")
        '2' -> listOf("^<", "<^")
        '3' -> listOf("^")
        '4' -> listOf("^^<<", "^<^<", "^<<^", "<^<^", "<^^<")
        '5' -> listOf("^^<", "^<^", "<^^")
        '6' -> listOf("^^")
        '7' -> listOf(
            "<<^^^",
            "<^<^^", "<^^<^", "<^^^<",
            "^<^^<", "^<<^^", "^<^<^",
            "^^<<^", "^^<^<"
        )

        '8' -> listOf("<^^^", "^<^^", "^^<^", "^^^<")
        '9' -> listOf("^^^")
        'A' -> listOf("")
        else -> error("Invalid character")
    }

    else -> error("Invalid character")
}
    // Each direction ends with 'A'
    .map { directions -> directions + "A" }

private fun String.complexity(directionalKeypads: Int): Long {
    val cache: MutableMap<Pair<String, Int>, Long> = mutableMapOf()

    fun String.findShortestAtDepth(depth: Int, pathFinding: (Char, Char) -> List<String>): Long =
        cache.getOrPut(this to depth) {
            ("A$this").zipWithNext().sumOf { (start, end) ->
                val paths: List<String> = pathFinding(start, end)
                if (depth == 0) {
                    paths.minOf { it.length }.toLong()
                } else {
                    paths.minOf { it.findShortestAtDepth(depth - 1, ::directionToDirection) }
                }
            }
        }

    return this.dropLast(1).toLong() * this.findShortestAtDepth(directionalKeypads, ::keypadToDirection)
}
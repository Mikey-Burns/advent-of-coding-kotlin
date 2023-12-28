package day23

import Point2D
import get
import isInBounds
import orthogonalNeighbors
import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return IceMaze(input).longestPath()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput).also(::println) == 94)
    val testInput2 = readInput("Day23_test")
    check(part2(testInput2).also(::println) == 0)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}

private data class IceMaze(val maze: List<List<Char>>) {
    val start = maze.first().indexOf('.').let { x -> Point2D(x, 0) }
    val finish = maze.last().indexOf('.').let { x -> Point2D(x, maze.lastIndex) }

    fun longestPath(path: List<Point2D> = listOf(start)): Int =
        with(path.last()) {
            // Subtract the start from the size
            if (this == finish) return path.size - 1
            return when (maze[this]) {
                '.' -> orthogonalNeighbors()
                '>' -> listOf(east())
                '<' -> listOf(west())
                '^' -> listOf(north())
                'v' -> listOf(south())
                else -> emptyList()
            }
                .filter { maze.isInBounds(it) }
                .filterNot { maze[it] == '#' }
                .filterNot { it in path }
                .maxOfOrNull { longestPath(path + it) }
                ?: 0
        }
}

private fun IceMaze(input: List<String>): IceMaze = input.map(String::toList).let(::IceMaze)
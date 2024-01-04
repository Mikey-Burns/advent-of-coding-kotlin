package year2023.day23

import utils.Point2D
import get
import utils.isInBounds
import utils.orthogonalNeighbors
import println
import readInput
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        return IceMaze(input).longestPath()
    }

    fun part2(input: List<String>): Int {
        return IceMaze(input).longestFlatPath()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput).also(::println) == 94)
    val testInput2 = readInput("Day23_test")
    check(part2(testInput2).also(::println) == 154)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}

private data class IceMaze(val maze: List<List<Char>>) {
    val start = maze.first().indexOf('.').let { x -> Point2D(x, 0) }
    val finish = maze.last().indexOf('.').let { x -> Point2D(x, maze.lastIndex) }

    fun Point2D.validNeighbors(steepSlopes: Boolean): List<Point2D> {
        val unfilteredNeighbors = if (steepSlopes) {
            when (maze[this]) {
                '.' -> buildList {
                    if (maze.isInBounds(north()) && maze[north()] != 'v') add(north())
                    if (maze.isInBounds(south()) && maze[south()] != '^') add(south())
                    if (maze.isInBounds(east()) && maze[east()] != '<') add(east())
                    if (maze.isInBounds(west()) && maze[west()] != '>') add(west())
                }

                '>' -> listOf(east())
                '<' -> listOf(west())
                '^' -> listOf(north())
                'v' -> listOf(south())
                else -> emptyList()
            }
        } else orthogonalNeighbors()

        return unfilteredNeighbors
            .filter { maze.isInBounds(it) }
            .filterNot { maze[it] == '#' }
    }

    fun Point2D.inBoundsWalkableNeighbors(): List<Point2D> = orthogonalNeighbors()
        .filter { maze.isInBounds(it) }
        .filterNot { maze[it] == '#' }

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

    fun longestFlatPath(): Int {
        val vertices: Set<Point2D> = (maze.first().indices)
            .flatMap { x ->
                (maze.indices)
                    .map { y -> Point2D(x, y) }
                    .filter { maze[it] != '#' }
                    .filter {
                        val validNeighbors = it.orthogonalNeighbors()
                            .filter { neighbor -> maze.isInBounds(neighbor) }
                            .count { neighbor -> maze[neighbor] != '#' }
                        validNeighbors != 2
                    }
            }
            .toSet()

        val graph: Map<Point2D, Map<Point2D, Int>> =
            vertices.associateWith { vertex ->
                vertex.inBoundsWalkableNeighbors()
                    .associate { neighbor ->
                        val path = mutableSetOf(vertex, neighbor)
                        while (path.last() !in vertices) {
                            path.last()
                                .inBoundsWalkableNeighbors()
                                .filterNot { it in path }
                                .single()
                                .also(path::add)
                        }
                        (path.last() to path.size - 1)
                    }
            }

        var best = 0
        val visited = mutableSetOf<Point2D>()

        fun dfs(location: Point2D, steps: Int): Int {
            if (location == finish) {
                best = max(steps, best)
                return best
            }
            visited += location
            graph.getValue(location)
                .filter { (nextLocation, distance) -> nextLocation !in visited }
                .forEach { (nextLocation, distance) -> dfs(nextLocation, distance + steps) }
            visited -= location
            return best
        }

        return dfs(start, 0)
    }

}

private fun IceMaze(input: List<String>): IceMaze = input.map(String::toList).let(::IceMaze)
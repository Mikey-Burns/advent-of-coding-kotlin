package day23

import Point2D
import get
import isInBounds
import orthogonalNeighbors
import println
import readInput
import utils.DijkstraGraph

fun main() {
    fun part1(input: List<String>): Int {
        return IceMaze(input).longestPath()
    }

    fun part2(input: List<String>): Int {
        val iceMaze = IceMaze(input)
        val graph = iceMaze.gridToGraph(false)
        val idealMap = graph.longestPathTree(iceMaze.start)
        val longestPath = graph.idealPath(idealMap, iceMaze.start, iceMaze.finish)
        return graph.weightOfIdealPath(longestPath)
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

    fun gridToGraph(steepSlopes: Boolean): DijkstraGraph<Point2D> {
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

        val pointToPathMap: Map<Point2D, List<Set<Point2D>>> =
            (if (steepSlopes) vertices - finish else vertices)
                .associateWith { vertex ->
                    vertex.validNeighbors(steepSlopes)
                        .map { neighbor ->
                            val path = mutableSetOf(vertex, neighbor)
                            while (path.last() !in vertices) {
                                path.last().validNeighbors(steepSlopes)
                                    .filterNot { it in path }
                                    .also(path::addAll)
                            }
                            path.toSet()
                        }
                }

        val edges: Map<Point2D, Set<Point2D>> = pointToPathMap
            .map { (point, listOfSets) ->
                point to listOfSets.map { it.last() }.toSet()
            }
            .toMutableList()
            .apply {
                if (steepSlopes) {
                    add(finish to setOf(this.single { it.second.contains(finish) }.first))
                }
            }
            .toMap()

        val weights: Map<Pair<Point2D, Point2D>, Int> = pointToPathMap
            .flatMap { (point, listOfSets) ->
                listOfSets.map { set -> (point to set.last()) to (set.size - 1) }
            }
            .toMap()

        return DijkstraGraph(vertices, edges, weights)
    }
}

private fun IceMaze(input: List<String>): IceMaze = input.map(String::toList).let(::IceMaze)
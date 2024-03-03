package year2022.day12

import get
import println
import readInput
import utils.DijkstraGraph
import utils.Point2D
import utils.isInBounds
import utils.orthogonalNeighbors
import java.awt.Point
import java.util.LinkedList
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long =
        input.useDijkstra { source, destination ->
            when {
                source == 'S' -> destination in 'a'..'b'
                destination == 'E' -> source in 'y'..'z'
                else -> destination in 'a'..(source + 1)
            }
        }

    fun part2(input: List<String>): Long {
        return 0L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test", "2022")
    check(part1(testInput).also(::println) == 31L)
    val testInput2 = readInput("Day12_test", "2022")
    check(part2(testInput2).also(::println) == 0L)

    val input = readInput("Day12", "2022")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.findPath(validStep: (Char, Char) -> Boolean): Long {
    val x = this.indexOfFirst { line -> line.contains('S') }
    val y = this[x].indexOf('S')
    val start = Point2D(y, x)
    val points = this.map(String::toList)

    var best = Long.MAX_VALUE
    val visited = mutableSetOf<Point2D>()
    val toVisit = LinkedList<Pair<Point2D, Long>>()
        .apply { add(start to 0) }

    fun dfs(location: Point2D, steps: Long): Long {
        val currentChar = points[location]
        if (currentChar == 'E') {
            best = min(steps, best)
            return best
        }
        visited += location
        location.orthogonalNeighbors()
            .filter { points.isInBounds(it) }
            .filter { it !in visited }
            .filter { validStep(currentChar, points[it]) }
            .forEach { dfs(it, steps + 1) }
        visited -= location
        return best
    }

    fun bfs(): Long {
        while (toVisit.isNotEmpty()) {
            val (location, steps) = toVisit.pop()
            val currentChar = points[location]
            if (currentChar == 'E') {
                return steps
            }
            visited += location
            location.orthogonalNeighbors()
                .filter { points.isInBounds(it) }
                .filterNot { it in visited }
                .filterNot { toVisit.map(Pair<Point2D, Long>::first).contains(it) }
                .filter { validStep(currentChar, points[it]) }
                .forEach { toVisit.add(it to steps + 1) }
        }
        return -1L
    }

    return bfs()
}

private fun List<String>.useDijkstra(validStep: (Char, Char) -> Boolean): Long {
    val points = this.map(String::toList)
    val vertices = this.indices.flatMap { y -> this.first().toList().indices.map { x -> Point2D(x, y) } }
        .toSet()
    val edges = vertices.associateWith { location ->
        location.orthogonalNeighbors()
            .filter { points.isInBounds(it) }
            .filter { validStep(points[location], points[it]) }
            .toSet()
    }
    val weights = edges.flatMap { (location, destinations) ->
        destinations.map { location to it }
    }
        .associateWith { 1 }

    val graph = DijkstraGraph(vertices, edges, weights)
    val start = points.find('S')
    val end = points.find('E')
    val tree = graph.shortestPathTree(start)
    val idealPath = graph.idealPath(tree, start, end)

    return idealPath.size.toLong() - 1L
}

private fun List<List<Char>>.find(c: Char): Point2D {
    val y = this.indexOfFirst { line -> line.contains(c) }
    val x = this[y].indexOf(c)
    return Point2D(x, y)
}
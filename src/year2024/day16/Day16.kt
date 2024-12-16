package year2024.day16

import println
import readInput
import year2024.utils.*
import year2024.utils.Direction.*

fun main() {
    fun part1(input: List<String>): Long = Maze(input).searchForExit()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val smallMaze = readInput("Day16_test", "2024")
    val bigMaze = readInput("Day16_test2", "2024")
    check(part1(smallMaze) == 7036L)
    check(part1(bigMaze) == 11048L)
    check(part2(smallMaze) == 0L)

    val input = readInput("Day16", "2024")
    part1(input).println()
    part2(input).println()
}

private data class Maze(val input: List<String>) {
    val start: Location
        get() {
            val rowNumber = input.indexOfFirst { it.contains('S') }
            val charNumber = input[rowNumber].indexOf('S')
            return rowNumber to charNumber
        }
    val end: Location
        get() {
            val rowNumber = input.indexOfFirst { it.contains('E') }
            val charNumber = input[rowNumber].indexOf('E')
            return rowNumber to charNumber
        }

    val walls: List<Location> = buildList {
        for (rowNumber in input.indices) {
            for (charNumber in input[0].indices) {
                if (input[rowNumber][charNumber] == '#') add(rowNumber to charNumber)
            }
        }
    }

    fun searchForExit(): Long {
        val turnsTaken: MutableSet<Location> = mutableSetOf()
        val currentTurnsTaken: MutableSet<Location> = mutableSetOf()

        fun walkSingleLine(path: MutableList<Location>, directionToWalk: Direction): List<Pair<Path, Direction>> {
            val turningPoints: MutableList<Pair<Path, Direction>> = mutableListOf()
            var currentLocation = path.last()
            while (currentLocation !in walls) {
                // If there are any turns we could take from our location, add them to the list of turning points
                // to attempt later
                currentLocation.perpendiculars(directionToWalk)
                    .filter { (nextLocation, _) -> nextLocation !in walls }
                    .map { (_, nextDirection) -> nextDirection }
                    .let { directions ->
                        if (currentLocation !in turnsTaken) {
                            directions.forEach { nextDirection -> turningPoints.add(path.toList() to nextDirection) }
                            currentTurnsTaken.add(currentLocation)
                        }
                    }
                // Keep walking, even if it bashes into a wall
                currentLocation = currentLocation.step(directionToWalk)
                // If the current location is not a wall, add it to the path
                if (currentLocation !in walls) path.add(currentLocation)
            }

            return turningPoints
        }

        var nextLines = walkSingleLine(mutableListOf(start), RIGHT)
        while (nextLines.none { (path, _) -> path.contains(end) }) {
            nextLines = nextLines.flatMap { (path, direction) ->
                walkSingleLine(path.toMutableList(), direction)
            }
            turnsTaken.addAll(currentTurnsTaken)
            currentTurnsTaken.clear()
        }
        val pathsToExit = nextLines.map { (path, _) -> path }
            .filter { path -> path.contains(end) }
            .map { path -> path.takeWhile { location -> location != end } + end }
        return pathsToExit
            .minOf { path -> path.score() }

    }

    /**
     * Use DFS to find all paths, then find the cheapest score path.
     * This works great on small maps, but cannot handle big ones.
     */
    @Suppress("Unused")
    fun findPathWithLowestScore(): Long {
        fun walkPath(path: Path): List<Path> {
            val currentLocation = path.last()
            if (currentLocation == end) return listOf(path)
            return listOf(
                currentLocation.up(),
                currentLocation.down(),
                currentLocation.left(),
                currentLocation.right()
            )
                .filter { location -> location !in path }
                .filter { location -> location !in walls }
                .flatMap { location -> walkPath(path.toMutableList().apply { add(location) }) }
        }

        return walkPath(listOf(start)).minOf { path -> path.score() }
    }
}

fun Location.perpendiculars(direction: Direction): List<Pair<Location, Direction>> = when (direction) {
    UP, DOWN -> listOf(left() to LEFT, right() to RIGHT)
    LEFT, RIGHT -> listOf(up() to UP, down() to DOWN)
}

typealias Path = List<Location>

private fun Path.score(): Long {
    var direction = RIGHT
    return this.zipWithNext().fold(0L) { score, (current, next) ->
        if (current.step(direction) == next) {
            score + 1L
        } else {
            direction = when (next) {
                current.up() -> UP
                current.down() -> DOWN
                current.left() -> LEFT
                current.right() -> RIGHT
                else -> error("Where did you go?")
            }
            score + 1001L
        }
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        return Maze(input)
            .takeStep()
    }

    fun part2(input: List<String>): Long {
        return Maze(input)
            .manySteps()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput).also(::println) == 6)
    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

private const val START = "AAA"
private const val END = "ZZZ"

private const val NUMBER_OF_HITS = 2

data class Maze(val pattern: String, val directions: List<Direction>) {
    private val directionMap = directions.associateBy (
        { direction -> direction.start },
        { direction -> Pair(direction.left, direction.right) }
    )
//        .also(::println)

    tailrec fun takeStep(currentLocation: String = START, stepsTaken: Int = 0): Int {
        if (currentLocation == END) return stepsTaken
        val nextStep = pattern[stepsTaken % pattern.length]
        val nextLocation = if (nextStep == 'L') directionMap[currentLocation]!!.first else directionMap[currentLocation]!!.second
        return takeStep(nextLocation, stepsTaken + 1)
    }

    fun manySteps(): Long {
        val startingPoints = directionMap.keys.filter { it.last() == 'A' }
        return startingPoints
            .map {manyStep(currentLocation = it) }
//            .also(::println)
            .map { (firstMatch, secondMatch) -> secondMatch - firstMatch }
            .map(Int::toLong)
//            .also(::println)
            .lcm()
            .also(::println)
    }

    tailrec fun manyStep(
        currentLocation: String = directionMap.keys.first { it.last() == 'A' },
        stepsTaken: Int = 0,
        list: MutableList<Int> = mutableListOf()
    ): List<Int> {
        if (list.size > NUMBER_OF_HITS) return list//.also(::println)
        if (currentLocation.last() == 'Z') list.add(stepsTaken)
        val nextStep = pattern[stepsTaken % pattern.length]
        val nextLocation = if (nextStep == 'L') directionMap[currentLocation]!!.first else directionMap[currentLocation]!!.second
        return manyStep(nextLocation, stepsTaken + 1, list)
    }
}

data class Direction(val start: String, val left: String, val right: String)

fun Maze(input: List<String>): Maze {
    val pattern = input.first()
    val directions = input.filter { it.contains("=") }
        .map { line ->
            line.split(" ", "(", ",", ")")
                .filter { lineSegment -> lineSegment.length == 3 }
        }
        .map { (start, left, right) -> Direction(start, left, right) }
    return Maze(pattern, directions)
}

fun Long.lcm(other: Long): Long {
    val larger = maxOf(this, other)
    val maxLcm = this * other
    var candidate = larger
    while (candidate <= maxLcm) {
        if (candidate % this == 0L && candidate % other == 0L) return candidate
        candidate += larger
    }
    return maxLcm
}

fun List<Long>.lcm(): Long = this.fold(1, Long::lcm)
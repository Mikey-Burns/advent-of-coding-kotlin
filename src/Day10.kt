fun main() {
    fun part1(input: List<String>): Int {
        return PipeMaze(input)
            .stepsToStart()
    }

    fun part2(input: List<String>): Int {
        val pipeMaze = PipeMaze(input)
        val pointsInLoop = pipeMaze.pointsInLoop()

        var matches = 0
        val inside = mutableSetOf<Point>()
        for (rowIndex in input.indices) {
            for (columnIndex in input[rowIndex].indices) {
                val candidate = Point(rowIndex, columnIndex)
                if (pipeMaze.isPointInLoop(candidate, pointsInLoop)) {
                    inside.add(candidate)
                    matches++
                }
            }
        }
        return matches
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput).also(::println) == 8)
    val testInput2 = readInput("Day10_test2")
//    check(part2(testInput2).also(::println) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

data class PipeMaze(val rows: List<String>) {
    val pointsInRow = rows[0].lastIndex

    private fun startPoint(): Point {
        val rowIndex = rows.indexOfFirst { it.contains('S') }
        val columnIndex = rows[rowIndex].indexOf('S')
        return rowIndex to columnIndex
    }

    fun symbolAt(point: Point): Char = rows[point.first][point.second]

    private fun Point.startNeighbors(): List<Point> {
        val left = first to second - 1
        val right = first to second + 1
        val top = first - 1 to second
        val bottom = first + 1 to second
        return buildList {
            if (left.second >= 0 && symbolAt(left) in "-LF") add(left)
            if (right.second <= pointsInRow && symbolAt(right) in "-J7") add(right)
            if (top.first >= 0 && symbolAt(top) in "|7F") add(top)
            if (bottom.first <= rows.lastIndex && symbolAt(bottom) in "|LJ") add(bottom)
        }
    }

    private fun Point.neighbors(): List<Point> = when (symbolAt(this)) {
        '|' -> listOf(Point(first - 1, second), Point(first + 1, second))
        '-' -> listOf(Point(first, second - 1), Point(first, second + 1))
        'L' -> listOf(Point(first - 1, second), Point(first, second + 1))
        'J' -> listOf(Point(first - 1, second), Point(first, second - 1))
        '7' -> listOf(Point(first + 1, second), Point(first, second - 1))
        'F' -> listOf(Point(first + 1, second), Point(first, second + 1))
        else -> error("Bad symbol")
    }

    private fun nextPoint(point: Point, previous: Point): Point =
        point.neighbors().filterNot { it == previous }.first()

    fun pointsInLoop(): List<Point> {
        val start = startPoint()
        var current = start.startNeighbors().first()
        var previous = start
        return buildList {
            add(start)
            add(current)
            while (current != start) {
                val next = nextPoint(current, previous)
                previous = current
                current = next
                add(current)
            }
        }
    }

    tailrec fun stepsToStart(
        point: Point = startPoint().startNeighbors().first(),
        previous: Point = startPoint(),
        steps: Int = 1
    ): Int {
        return if (symbolAt(point) == 'S') steps / 2
        else stepsToStart(nextPoint(point, previous), point, 1 + steps)
    }

    fun validPoint(point: Point): Boolean =
        point.first in 0..rows.lastIndex && point.second in 0..pointsInRow

    fun isPointInLoop(point: Point, pointsInLoop: List<Point>): Boolean {
        if (point in pointsInLoop) return false
        var collisions = 0
        for (offset in 1..(pointsInRow - point.second)) {
            val target = Point(point.first, point.second + offset)
            if (target in pointsInLoop && symbolAt(target) in "|LJS") collisions++
        }
        return collisions % 2 == 1
    }

}

typealias Point = Pair<Int, Int>

import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        return LavaMap(input)
            .energize(LavaMap.PointWithDirection(0, 0, LavaMap.Direction.RIGHT))
    }

    fun part2(input: List<String>): Int {
        return LavaMap(input)
            .let { lavaMap ->
                val bottomIndex = input.lastIndex
                val rightIndex = input.first().lastIndex
                val startingPoints = input.indices.flatMap { row ->
                    // Left and Right
                    listOf(
                        LavaMap.PointWithDirection(row, 0, LavaMap.Direction.RIGHT),
                        LavaMap.PointWithDirection(row, rightIndex, LavaMap.Direction.LEFT)
                    )
                } + (0..rightIndex).flatMap {column ->
                    // Top and Bottom
                    listOf(
                        LavaMap.PointWithDirection(0, column, LavaMap.Direction.DOWN),
                        LavaMap.PointWithDirection(bottomIndex, column, LavaMap.Direction.UP)
                    )
                }
                startingPoints.maxOf(lavaMap::energize)
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput).also(::println) == 46)
    val testInput2 = readInput("Day16_test")
    check(part2(testInput2).also(::println) == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

data class LavaMap(val rows: List<String>) {
    private val lastIndex = rows.first().lastIndex

    fun energize(startingPoint: PointWithDirection): Int {
        val energized: MutableSet<PointWithDirection> = mutableSetOf()
        val toEnergize: Stack<PointWithDirection> = Stack<PointWithDirection>()
            .apply { add(startingPoint) }
        while (toEnergize.isNotEmpty()) {
            val point = toEnergize.pop()
            if (isPointValid(point) && !energized.contains(point)) {
                point.energize(rows)
                    .also(toEnergize::addAll)
                energized.add(point)
            }
        }

        return energized.map(PointWithDirection::toPoint)
            .toSet()
            .size
    }

    private fun isPointValid(point: PointWithDirection): Boolean =
        point.x in 0..lastIndex && point.y in 0..rows.lastIndex

    data class PointWithDirection(val x: Int, val y: Int, val direction: Direction) {

        private fun up(): PointWithDirection = this.copy(x = x - 1, direction = Direction.UP)
        private fun down(): PointWithDirection = this.copy(x = x + 1, direction = Direction.DOWN)
        private fun left(): PointWithDirection = this.copy(y = y - 1, direction = Direction.LEFT)
        private fun right(): PointWithDirection = this.copy(y = y + 1, direction = Direction.RIGHT)

        fun energize(rows: List<String>): List<PointWithDirection> {
            val symbol = rows[x][y]
            return when (direction) {
                Direction.UP -> when (symbol) {
                    '.', '|' -> listOf(up())
                    '/' -> listOf(right())
                    '\\' -> listOf(left())
                    '-' -> listOf(left(), right())
                    else -> emptyList()
                }

                Direction.DOWN -> when (symbol) {
                    '.', '|' -> listOf(down())
                    '/' -> listOf(left())
                    '\\' -> listOf(right())
                    '-' -> listOf(left(), right())
                    else -> emptyList()
                }

                Direction.LEFT -> when (symbol) {
                    '.', '-' -> listOf(left())
                    '/' -> listOf(down())
                    '\\' -> listOf(up())
                    '|' -> listOf(up(), down())
                    else -> emptyList()
                }

                Direction.RIGHT -> when (symbol) {
                    '.', '-' -> listOf(right())
                    '/' -> listOf(up())
                    '\\' -> listOf(down())
                    '|' -> listOf(up(), down())
                    else -> emptyList()
                }
            }
                .also { println("x: $x, y: $y, symbol: $symbol, direction: $direction, list: $it") }
        }

        fun toPoint(): Pair<Int, Int> = x to y
    }

    enum class Direction {
        UP, LEFT, DOWN, RIGHT
    }
}
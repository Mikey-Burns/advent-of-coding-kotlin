fun main() {
    fun part1(input: List<String>): Int {
        return bigDig(input.map(::DigLine))
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput).also(::println) == 62)
    val testInput2 = readInput("Day18_test")
    check(part2(testInput2).also(::println) == 0)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}

data class DigLine(val direction: Compass, val steps: Int, val color: String)

fun DigLine(line: String): DigLine {
    val (rawDirection, stepsChar, colorString) = line.split(" ")
    val direction = when (rawDirection) {
        "U" -> Compass.NORTH
        "R" -> Compass.EAST
        "D" -> Compass.SOUTH
        "L" -> Compass.WEST
        else -> throw IllegalArgumentException("Bad direction")
    }
    val steps = stepsChar.toInt()
    val color = colorString.filter { it !in "()" }
    return DigLine(direction, steps, color)
}

enum class DigSpace {
    HORIZONTAL, VERTICAL, CORNER_DOWN, CORNER_UP
}

fun bigDig(digs: List<DigLine>): Int {
    val digMap: MutableMap<Point2D, DigSpace> = mutableMapOf()
    var currentPoint = Point2D(0, 0)

    digs.forEachIndexed { index, digLine ->
        // Non-edge steps
        repeat(digLine.steps) { repeat ->
            currentPoint = currentPoint.step(digLine.direction)
                .also {
                    if (repeat < (digLine.steps - 1)) {
                        digMap[it] = when (digLine.direction) {
                            Compass.NORTH, Compass.SOUTH -> DigSpace.VERTICAL
                            Compass.EAST, Compass.WEST -> DigSpace.HORIZONTAL
                        }
                    } else {
                        val nextDig = digs[(index + 1) % digs.size]
                        digMap[it] = when (digLine.direction) {
                            Compass.NORTH -> DigSpace.CORNER_DOWN
                            Compass.SOUTH -> DigSpace.CORNER_UP
                            Compass.EAST, Compass.WEST ->
                                if (nextDig.direction == Compass.NORTH) DigSpace.CORNER_UP
                                else DigSpace.CORNER_DOWN
                        }
                    }
                }
        }
    }

    val minX = digMap.keys.minOf { it.x }
    val maxX = digMap.keys.maxOf { it.x }
    val minY = digMap.keys.minOf { it.y }
    val maxY = digMap.keys.maxOf { it.y }

    var matches = 0
    for (x in minX..maxX) {
        for (y in minY..maxY) {
            val candidate = Point2D(x, y)
            if (digMap.containsKey(candidate)) {
                matches++
            } else {
                var collisions = 0
                for (offset in 1..(maxX - x)) {
                    val target = Point2D(x + offset, y)
                    // Try to hit a vertical space or a corner turning up
                    if (digMap[target] in listOf(DigSpace.VERTICAL, DigSpace.CORNER_UP)) collisions++
                }
                if (collisions % 2 == 1) matches++
            }
        }
    }

    return matches
}
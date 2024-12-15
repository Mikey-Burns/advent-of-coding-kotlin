package year2024.day15

import println
import readInput
import year2024.day15.Direction.*

private typealias Location = Pair<Int, Int>

fun main() {
    fun part1(input: List<String>): Long {
        val warehouse = input.takeWhile { it.isNotBlank() }.toWarehouse()
        val directions = input.takeLastWhile { it.isNotBlank() }.toDirections()

        return warehouse.followDirections(directions).boxGps().sum()
    }

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val smallTest = readInput("Day15_test", "2024")
    val bigTest = readInput("Day15_test2", "2024")
    check(part1(smallTest) == 2028L)
    check(part1(bigTest) == 10092L)
    check(part2(smallTest) == 0L)

    val input = readInput("Day15", "2024")
    part1(input).println()
    part2(input).println()
}

private data class Warehouse(var robot: Location, val walls: MutableList<Location>, val boxes: MutableList<Location>) {

    fun followDirections(directions: List<Direction>): Warehouse = apply {
        directions.forEach { direction ->
            val targetLocation = robot.step(direction)
            if (targetLocation !in walls && targetLocation !in boxes) {
                robot = targetLocation
            } else if (targetLocation in walls) {
                // No movement
                Unit
            } else {
                // It's a box
                val boxToDrop = targetLocation
                var nextLocation = targetLocation.step(direction)
                while (nextLocation !in walls && nextLocation in boxes) {
                    nextLocation = nextLocation.step(direction)
                }
                if (nextLocation in walls) {
                    // Boxes all the way to the wall
                    Unit
                } else {
                    // nextLocation is an empty space
                    boxes.remove(boxToDrop)
                    boxes.add(nextLocation)
                    robot = targetLocation
                }
            }

//            printWarehouse()
        }
    }

    private fun printWarehouse() = buildString {
        for (rowNumber in 0..walls.last().first) {
            for (charNumber in 0..walls.last().second) {
                val location = rowNumber to charNumber
                when (location) {
                    in walls -> append("#")
                    in boxes -> append("O")
                    robot -> append("@")
                    else -> append(".")
                }
            }
            append("\n")
        }
        append("\n\n")
    }
        .println()

    fun boxGps(): List<Long> = boxes.map { (rowNumber, charNumber) -> rowNumber * 100L + charNumber }
}

private fun List<String>.toWarehouse(): Warehouse {
    var robot: Location? = null
    val walls = mutableListOf<Location>()
    val boxes = mutableListOf<Location>()
    this.forEachIndexed { rowNumber, row ->
        row.forEachIndexed { charNumber, c ->
            when (c) {
                '@' -> robot = rowNumber to charNumber
                '#' -> walls.add(rowNumber to charNumber)
                'O' -> boxes.add(rowNumber to charNumber)
            }
        }
    }
    return Warehouse(robot ?: error("No robot found"), walls, boxes)
}

private fun List<String>.toDirections(): List<Direction> = this.joinToString("").map(Direction::fromChar)

private enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    companion object {
        fun fromChar(c: Char): Direction = when (c) {
            '^' -> UP
            '>' -> RIGHT
            'v' -> DOWN
            '<' -> LEFT
            else -> error("Bad direction character")
        }
    }
}


private fun Location.step(direction: Direction): Location = when (direction) {
    UP -> up()
    RIGHT -> right()
    DOWN -> down()
    LEFT -> left()
}

private fun Location.up(): Location = first - 1 to second
private fun Location.down(): Location = first + 1 to second
private fun Location.left(): Location = first to second - 1
private fun Location.right(): Location = first to second + 1

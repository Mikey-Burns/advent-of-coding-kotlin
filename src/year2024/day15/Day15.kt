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

    fun part2(input: List<String>): Long {
        val bigWarehouse = input.takeWhile { it.isNotBlank() }.toBigWarehouse()
        val directions = input.takeLastWhile { it.isNotBlank() }.toDirections()

        return bigWarehouse.followDirections(directions).boxGps().sum()
    }

    // test if implementation meets criteria from the description, like:
    val smallTest = readInput("Day15_test", "2024")
    val bigTest = readInput("Day15_test2", "2024")
    check(part1(smallTest) == 2028L)
    check(part1(bigTest) == 10092L)
    check(part2(bigTest) == 9021L)

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

    @Suppress("unused")
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

private data class BigWarehouse(var robot: Location, val walls: MutableList<Location>, val boxes: MutableList<BigBox>) {
    fun followDirections(directions: List<Direction>): BigWarehouse = apply {
        directions.forEach { direction ->
            val targetLocation = robot.step(direction)
            if (targetLocation !in walls && targetLocation !in boxes) {
                robot = targetLocation
            } else if (targetLocation in walls) {
                // No movement
                Unit
            } else {
                // It's a box!

                // Find the box that we are immediately pushing into, add it to a mutable list
                val boxesToDrop = boxes.filter { it.contains(targetLocation) }.toMutableList()
                // The next set of boxes to try pushing is initially the box the robot pushes
                var boxesToPush = boxesToDrop.toSet()
                var destinationLocations = boxesToPush
                    // Push the boxes
                    .map { box -> box.push(direction) }
                    // Get the locations
                    .flatMap(BigBox::toLocations)
                    .toSet()
                // While there are no walls to be pushed into, we keep trying to push
                while (destinationLocations.none { location -> location in walls } && boxesToPush.isNotEmpty()) {
                    // Find boxes we are pushing into
                    boxesToPush =
                        boxes.filter { box -> box.left in destinationLocations || box.right in destinationLocations }
                            // and filter out any we've already pushed
                            .filter { box -> box !in boxesToDrop }
                            .toSet()
                            .also { boxesToDrop.addAll(it) }
                    destinationLocations = boxesToPush
                        // Push the boxes
                        .map { box -> box.push(direction) }
                        // Get the locations
                        .flatMap(BigBox::toLocations)
                        .toSet()
                }

                // If we ran into a wall after all, we don't push anything
                if (destinationLocations.any { location -> location in walls }) {
                    Unit
                } else {
                    boxes.removeAll(boxesToDrop)
                    boxes.addAll(boxesToDrop.map { box -> box.push(direction) })
                    robot = targetLocation
                }
            }

//            printWarehouse()
        }
    }

    fun boxGps(): List<Long> = boxes.map { box -> box.left }
        .map { (rowNumber, charNumber) -> rowNumber * 100L + charNumber }

    @Suppress("unused")
    fun printWarehouse() = buildString {
        for (rowNumber in 0..walls.last().first) {
            for (charNumber in 0..walls.last().second) {
                val location = rowNumber to charNumber
                when {
                    location in walls -> append("#")
                    boxes.any { bigBox -> bigBox.left == location } -> append("[")
                    boxes.any { bigBox -> bigBox.right == location } -> append("]")
                    location == robot -> append("@")
                    else -> append(".")
                }
            }
            append("\n")
        }
        append("\n\n")
    }
        .println()
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

private fun List<String>.toBigWarehouse(): BigWarehouse {
    var robot: Location? = null
    val walls = mutableListOf<Location>()
    val boxes = mutableListOf<BigBox>()
    this.forEachIndexed { rowNumber, row ->
        row.forEachIndexed { charNumber, c ->
            when (c) {
                '@' -> robot = rowNumber to charNumber * 2
                '#' -> {
                    walls.add(rowNumber to charNumber * 2)
                    walls.add(rowNumber to charNumber * 2 + 1)
                }

                'O' -> {
                    boxes.add(
                        BigBox(
                            rowNumber to charNumber * 2,
                            rowNumber to charNumber * 2 + 1
                        )
                    )
                }
            }
        }
    }
    return BigWarehouse(robot ?: error("No robot found"), walls, boxes)
}

private fun List<String>.toDirections(): List<Direction> = this.joinToString("").map(Direction::fromChar)

private data class BigBox(val left: Location, val right: Location) {

    fun contains(location: Location): Boolean = location == left || location == right

    fun push(direction: Direction): BigBox {
        return when (direction) {
            UP -> BigBox(up())
            RIGHT -> BigBox(right())
            DOWN -> BigBox(down())
            LEFT -> BigBox(left())
        }
    }

    val toLocations: List<Location>
        get() = listOf(left, right)

    companion object {
        fun BigBox(locations: Pair<Location, Location>): BigBox = BigBox(locations.first, locations.second)
        fun BigBox.up(): Pair<Location, Location> = left.up() to right.up()
        fun BigBox.down(): Pair<Location, Location> = left.down() to right.down()
        fun BigBox.left(): Pair<Location, Location> = left.left() to right.left()
        fun BigBox.right(): Pair<Location, Location> = left.right() to right.right()
    }
}

private operator fun List<BigBox>.contains(location: Location): Boolean =
    this.any { bigBox -> bigBox.contains(location) }

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

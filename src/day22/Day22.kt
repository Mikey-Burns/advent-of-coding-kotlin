package day22

import println
import readInput
import utils.Point3D

fun main() {
    fun part1(input: List<String>, letterBased: Boolean): Int {
        val sandGrid = SandGrid(input, letterBased)
        return sandGrid.canBeDisintegrated()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    val testInputShuffle = readInput("Day22_test_shuffle")
    check(part1(testInput, true).also(::println) == 5)
    check(part1(testInputShuffle, true).also(::println) == 5)
    val testInput2 = readInput("Day22_test")
    check(part2(testInput2).also(::println) == 0)

    val input = readInput("Day22")
    part1(input, false).println()
    part2(input).println()
}

private data class SandGrid(val grid: MutableMap<Point3D, String>) {

    fun canBeDisintegrated(): Int {
        // Find lower neighbors
        val lower: Map<String, List<String>> = grid.values.associateWith { name ->
            // For each block...
            grid.filterValues { it == name }.keys
                // Find the blocks above
                .mapNotNull { grid[it + Point3D.BELOW] }
                // Ignore self
                .filter { it != name }
        }
        // Can disintegrate if we are not the only lower neighbor for anyone
        return grid.values.filter { name ->
            lower.values.all { low ->
                (low.contains(name) && low.size > 1) || !low.contains(name)
            }
        }
            .toSet()
            .size
    }
}

private fun SandGrid(input: List<String>, letterBased: Boolean): SandGrid {
    val grid = mutableMapOf<Point3D, String>()
    val rawBlocks = input.mapIndexed { index, line ->
        val (left, right) = line.split("~")
        val (leftX, leftY, leftZ) = left.split(",").map(String::toInt)
        val (rightX, rightY, rightZ) = right.split(",").map(String::toInt)
        val blocks = when {
            leftX < rightX -> (leftX..rightX)
                .map { x -> Point3D(x, leftY, leftZ) }

            leftY < rightY -> (leftY..rightY)
                .map { y -> Point3D(leftX, y, leftZ) }

            leftZ < rightZ -> (leftZ..rightZ)
                .map { z -> Point3D(leftX, leftY, z) }

            else -> listOf(Point3D(leftX, leftY, leftZ))
        }
            .toMutableList()
        index to blocks
    }
    
    
    
    rawBlocks
        .sortedBy { (_, blocks) -> blocks.minOf { block -> block.z } }
        .forEach { (index, blocks) ->
            val name = if (letterBased) "Block-${'A' + index}" else "Block-${index + 1}"
            while (blocks.none { it.z == 1 || grid[it + Point3D.BELOW] != null }) {
                blocks.replaceAll { it + Point3D.BELOW }
            }
            blocks.forEach { grid[it] = name }
        }
    grid.entries.sortedBy { it.key.z }.forEach(::println)
    return SandGrid(grid)
}
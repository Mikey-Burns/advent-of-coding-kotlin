package year2023.day22

import println
import readInput
import year2023.day22.Block.Companion.BOTTOM_HEIGHT

fun main() {
    fun part1(input: List<String>, letterBased: Boolean): Int {
        val blocks = processBlocks(input, letterBased)

        return blocks.size - blocks.loadBearing().size
    }

    fun part2(input: List<String>, letterBased: Boolean): Int {
        val blocks = processBlocks(input, letterBased)

        return blocks.loadBearing().sumOf { it.chainReaction(blocks) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    val testInputShuffle = readInput("Day22_test_shuffle")
    check(part1(testInput, true).also(::println) == 5)
    check(part1(testInputShuffle, true).also(::println) == 5)
    val testInput2 = readInput("Day22_test")
    check(part2(testInput2, true).also(::println) == 7)

    val input = readInput("Day22")
    part1(input, false).println()
    part2(input, false).println()
}

private fun processBlocks(input: List<String>, letterBased: Boolean): List<Block> {
    return input.mapIndexed { index, line -> Block(index, line, letterBased) }
        .sorted()
        .collapse()
}

private data class Block(val name: String, val x: IntRange, val y: IntRange, val z: IntRange) : Comparable<Block> {
    val supporting = mutableSetOf<Block>()
    val supportedBy = mutableSetOf<Block>()

    override fun compareTo(other: Block): Int = z.first - other.z.first

    fun beUnder(other: Block) {
        supporting += other
        other.supportedBy += this
    }

    fun canSupport(other: Block): Boolean =
        x intersects other.x && y intersects other.y && z.last + 1 == other.z.first

    fun onGround(): Boolean = z.first == BOTTOM_HEIGHT

    fun fall(height: Int): Block = copy(z = height..(height + z.size))

    fun chainReaction(allBlocks: List<Block>): Int {
        val removedBlocks = mutableSetOf(this)
        val hasNotFallenYet = (allBlocks - this).toMutableSet()
        val falling = mutableSetOf(this)
        while (falling.isNotEmpty()) {
            hasNotFallenYet.filter { it.supportedBy.isNotEmpty() }
                .filter { it.supportedBy.all { support -> support in removedBlocks } }
                .also {
                    hasNotFallenYet.removeAll(it.toSet())
                    removedBlocks.addAll(it)

                    falling.clear()
                    falling.addAll(it)
                }
        }
        return removedBlocks.size - 1
    }

    companion object {
        const val BOTTOM_HEIGHT = 1
    }
}

private fun Block(index: Int, line: String, letterBased: Boolean): Block {
    val name = if (letterBased) "Block-${'A' + index}" else "Block-${index + 1}"
    val (left, right) = line.split("~")
    val (leftX, leftY, leftZ) = left.split(",").map(String::toInt)
    val (rightX, rightY, rightZ) = right.split(",").map(String::toInt)
    return Block(name, leftX..rightX, leftY..rightY, leftZ..rightZ)
}

infix fun IntRange.intersects(other: IntRange): Boolean =
    first <= other.last && last >= other.first

val IntRange.size: Int
    get() = this.last - first

private fun List<Block>.collapse(): List<Block> {
    val settledBlocks = mutableListOf<Block>()
    this.forEach { block ->
        var current = block
        var settled = false
        while (!settled) {
            val supporters = settledBlocks.filter { below -> below.canSupport(current) }
            // If there are no settled blocks below us, and we aren't on the ground
            if (supporters.isEmpty() && !current.onGround()) {
                val heightToFall = settledBlocks.filter { it.z.last < current.z.first - 1 }
                    .maxOfOrNull { it.z.last + 1 } ?: BOTTOM_HEIGHT
                current = current.fall(heightToFall)
            } else {
                settled = true
                supporters.forEach { below -> below.beUnder(current) }
                settledBlocks.add(current)
            }
        }
    }
    return settledBlocks
}

private fun List<Block>.loadBearing(): List<Block> =
    filter { block -> block.supporting.any { it.supportedBy.size == 1 } }
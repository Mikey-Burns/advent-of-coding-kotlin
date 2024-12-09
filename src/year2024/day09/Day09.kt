package year2024.day09

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = input.first().toDiskMapList()
        .compress()
        .checksum()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test", "2024")
    check(part1(testInput) == 1928L)
    val testInput2 = readInput("Day09_test", "2024")
    check(part2(testInput2) == 0L)

    val input = readInput("Day09", "2024")
    part1(input).println()
    part2(input).println()
}

private fun String.toDiskMapList(): List<Block> = this.flatMapIndexed { index, c ->
    val number = c.digitToInt()
    List(number) {
        if (index % 2 == 0) Block.Data(index / 2) else Block.Empty
    }
}

private tailrec fun List<Block>.compress(): List<Block> {
    val compressed = this.dropLastWhile { it == Block.Empty }.toMutableList()
    if (!compressed.contains(Block.Empty)) return compressed
    val index = compressed.indexOf(Block.Empty)
    compressed[index] = compressed.last()
    return compressed.dropLast(1).compress()
}

private fun List<Block>.checksum(): Long =
    this.foldIndexed(0L) { index, total, block -> total + index * (block as Block.Data).id }

sealed interface Block {
    data object Empty : Block {
        override fun toString(): String = "."
    }
    data class Data(val id: Int) : Block {
        override fun toString(): String = "$id"
    }
}
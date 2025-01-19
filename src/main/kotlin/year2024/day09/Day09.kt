package year2024.day09

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long = input.first().toDiskMap()
        .compress()
        .checksum()

    fun part2(input: List<String>): Long = input.first().toDiskSegments()
        .compressSegments()
        .checksum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test", "2024")
    check(part1(testInput) == 1928L)
    val testInput2 = readInput("Day09_test", "2024")
    check(part2(testInput2) == 2858L)

    val input = readInput("Day09", "2024")
    part1(input).println()
    part2(input).println()
}

private fun String.toDiskMap(): List<Block> = this.flatMapIndexed { index, c ->
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

private fun String.toDiskSegments(): List<List<Block>> = this.mapIndexed { index, c ->
    val number = c.digitToInt()
    List(number) {
        if (index % 2 == 0) Block.Data(index / 2) else Block.Empty
    }
}

private fun List<List<Block>>.compressSegments(): List<Block> {
    val maxId = this.last { it.first() is Block.Data }.first().value
    val valueToListMap = this
        .filter { it.isNotEmpty() && it.first() is Block.Data }
        .associateBy { it.first().value }

    val compressed = this.filter { it.isNotEmpty() }.toMutableList()
    for (id in maxId downTo 0) {
        val dataList = valueToListMap[id] ?: continue
        val idIndex = compressed.indexOf(dataList)
        val emptyIndex = compressed.indexOfFirst { list -> list.first() is Block.Empty && list.size >= dataList.size }
        if (emptyIndex == -1 || emptyIndex > idIndex) continue
        val emptyList = compressed[emptyIndex]
        when {
            // Equal sizes, we swap
            dataList.size == emptyList.size -> {
                compressed[emptyIndex] = dataList
                compressed[idIndex] = emptyList
            }
            // Otherwise, split the empty list because there is extra empty space
            else -> {
                compressed[emptyIndex] = List(emptyList.size - dataList.size) { Block.Empty }
                compressed[idIndex] = List(dataList.size) { Block.Empty}
                compressed.add(emptyIndex, dataList)
            }
        }
    }

    return compressed.flatten()
}

private fun List<Block>.checksum(): Long =
    this.foldIndexed(0L) { index, total, block -> total + index * block.value }

sealed interface Block {
    val value: Int

    data object Empty : Block {
        override val value: Int
            get() = 0

        override fun toString(): String = "."
    }

    data class Data(val id: Int) : Block {
        override fun toString(): String = "$id"

        override val value: Int
            get() = id
    }
}
package year2016.day22

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = input.drop(2).map { it.toNode() }.viablePairs()

    fun part2(input: List<String>): Int = input.drop(2).map { it.toNode() }.migrateData()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test", "2016")
    check(part1(testInput) == 3)

    val input = readInput("Day22", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun String.toNode(): Node {
    val split = this.split(" ").filter { it.isNotEmpty() }
    val x = split[0].substringAfter("-x").substringBefore("-y").toInt()
    val y = split[0].substringAfter("-y").toInt()
    val size = split[1].dropLast(1).toInt()
    val used = split[2].dropLast(1).toInt()
    val available = split[3].dropLast(1).toInt()

    return Node(x, y, size, used, available)
}

private data class Node(val x: Int, val y: Int, val size: Int, var used: Int, var available: Int)

private fun List<Node>.viablePairs(): Int = this.filter { node -> node.used > 0 }
    .sumOf { node ->
        this
            .filter { other -> other != node }
            .count { other -> other.available >= node.used }
    }

private fun List<Node>.migrateData(): Int {
    this.filter { node -> node.used > 0 }
        .flatMap { node ->
            this.filter { other -> other != node }
                .filter { other ->
                    (node.x in (other.x - 1)..(other.x + 1) && node.y == other.y)
                            || ((node.x == other.x) && (node.y in (other.y - 1)..(other.y + 1)))
                }
                .filter { other -> other.available >= node.used }
                .map { other -> node to other }
        }
        .onEach { (node, other) -> println("node: $node,\tother: $other") }
    println()
    this.print()
    return 0
}

private fun List<Node>.print() {
    this.groupBy { it.y }
        .toSortedMap()
        .toList()
        .forEach { (_, nodes) ->
            nodes.sortedBy { it.x }
                .joinToString("\t") { node -> "${node.used}/${node.size}" }
                .also(::println)
        }
}
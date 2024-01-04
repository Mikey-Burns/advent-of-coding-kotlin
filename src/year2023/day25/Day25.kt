package year2023.day25

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return karger(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput).also(::println) == 54)

    val input = readInput("Day25")
    part1(input).println()
}

fun karger(input: List<String>): Int {
    while (true) {
        val graph = parseInput(input)
        val counts = graph.keys.associateWith { 1 }.toMutableMap()

        while (graph.size > 2) {
            val a = graph.keys.random()
            val b = graph.getValue(a).random()
            val newNode = "$a-$b"

            counts[newNode] = (counts.remove(a) ?: 0) + (counts.remove(b) ?: 0)
            graph.combineNodes(a, b, newNode)
            graph.mergeNodes(a, newNode)
            graph.mergeNodes(b, newNode)
        }

        // There are only two nodes left
        val (nodeA, nodeB) = graph.keys.toList()
        // Per our problem, we need to find a solution where we cut 3 lines
        if (graph.getValue(nodeA).size == 3) {
            return counts.getValue(nodeA) * counts.getValue(nodeB)
        }
    }
}

private fun parseInput(input: List<String>): MutableMap<String, MutableList<String>> {
    val map = mutableMapOf<String, MutableList<String>>()
    input.forEach { line ->
        val sourceName = line.substringBefore(":")
        // Get our list of edges or an empty list
        val sourceList = map.getOrPut(sourceName) { mutableListOf() }
        line.substringAfter(": ")
            .split(" ")
            .forEach { destination ->
                // Add the destination to our list
                sourceList += destination
                // Add the opposite direction
                map.getOrPut(destination) { mutableListOf() }.add(sourceName)
            }
    }
    return map
}

private fun MutableMap<String, MutableList<String>>.mergeNodes(oldNode: String, newNode: String) {
    // Remove the old node.
    // For each destination that the old node had,
    // go into the destination's connections and update any references to
    // the old node to be the new node.
    remove(oldNode)?.forEach { destination ->
        getValue(destination).replaceAll { if (it == oldNode) newNode else it }
    }
}

private fun MutableMap<String, MutableList<String>>.combineNodes(a: String, b: String, newNode: String) {
    // Create a new node that has the connections from two existing nodes
    this[newNode] = (this.getValue(a).filter { it != b } + this.getValue(b).filter { it != a }).toMutableList()
}
package year2024.day23

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int = input.toDatalinkMap()
        .findGroupsOfThree()
        .count { it.startsWithT() }

    fun part2(input: List<String>): String = input.toDatalinkMap()
        .findAllGroups()
        .maxBy { it.size }
        .sorted()
        .joinToString(",")

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test", "2024")
    check(part1(testInput) == 7)
    val testInput2 = readInput("Day23_test", "2024")
    check(part2(testInput2) == "co,de,ka,ta")

    val input = readInput("Day23", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.toDatalinkMap(): Map<String, List<String>> = buildMap {
    this@toDatalinkMap.map { line -> line.split("-") }
        .forEach { (from, to) ->
            this[from] = (this[from] ?: mutableListOf()) + to
            this[to] = (this[to] ?: mutableListOf()) + from
        }
}

private fun Map<String, List<String>>.findGroupsOfThree(): List<Triple<String, String, String>> =
    entries.flatMap { (name, connections) ->
        connections.flatMap { connection ->
            getValue(connection)
                .filter { thirdDegree -> thirdDegree in connections }.map { thirdDegree ->
                    listOf(name, connection, thirdDegree)
                        .sorted()
                        .toTriple()
                }
        }
    }
        .distinct()
        .sortedBy { it.first }

private fun Map<String, List<String>>.findAllGroups(): List<List<String>> =
    entries.flatMap { (name, connections) ->
        connections.map { connection ->
            val network = mutableListOf(name, connection)
            getValue(connection)
                .forEach { thirdDegree ->
                    if (network.all { getValue(it).contains(thirdDegree) }) {
                        network.add(thirdDegree)
                    }
                }
            network.sorted()
        }
    }
        .distinct()

private fun List<String>.toTriple(): Triple<String, String, String> = Triple(this[0], this[1], this[2])

private fun Triple<String, String, String>.startsWithT(): Boolean =
    first.startsWith('t') || second.startsWith('t') || third.startsWith('t')
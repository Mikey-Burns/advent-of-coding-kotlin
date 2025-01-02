package year2024.day24

import println
import readInput
import year2024.day24.Operator.*

fun main() {
    fun part1(input: List<String>): Long = input.toWireMap().processWires()

    fun part2(input: List<String>): Long = 0L

    // test if implementation meets criteria from the description, like:
    val smallInput = readInput("Day24_test", "2024")
    val bigInput = readInput("Day24_test2", "2024")
    check(part1(smallInput) == 4L)
    check(part1(bigInput) == 2024L)
    val testInput2 = readInput("Day24_test", "2024")
    check(part2(testInput2) == 0L)

    val input = readInput("Day24", "2024")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.toWireMap(): WireMap {
    val gates = this.takeLastWhile { it.isNotBlank() }
        .map { line -> line.split(" ") }
        .map { split -> Gate(split[0], split[2], split[4], Operator(split[1])) }
        .toSet()
    val wires = buildMap {
        gates.forEach { gate ->
            put(gate.first, null)
            put(gate.second, null)
            put(gate.destination, null)
        }
        this@toWireMap.takeWhile { it.isNotBlank() }
            .forEach { line ->
                val split = line.split(": ")
                put(split[0], split[1] == "1")
            }
    }
    return WireMap(gates, wires)
}

private data class WireMap(val gates: Set<Gate>, val initialWires: Map<String, Boolean?>) {
    private val wires = initialWires.toMutableMap()

    fun processWires(): Long {
        val processedGates = mutableSetOf<Gate>()
        while (processedGates != gates) {
            gates.forEach { gate ->
                if (wires[gate.first] != null && wires[gate.second] != null) {
                    wires[gate.destination] = when(gate.operator) {
                        AND -> wires.getValue(gate.first)!! && wires.getValue(gate.second)!!
                        OR -> wires.getValue(gate.first)!! || wires.getValue(gate.second)!!
                        XOR -> wires.getValue(gate.first)!! xor  wires.getValue(gate.second)!!
                    }
                    processedGates.add(gate)
                }
            }
        }

        return buildString {
            wires.filterKeys { it.startsWith('z') }
                .toList()
                .sortedBy { it.first }
                .reversed()
                .forEach { (key, wireState) -> append(if (wireState == true) "1" else "0") }
        }
            .toLong(2)
    }
}

private data class Gate(val first: String, val second: String, val destination: String, val operator: Operator)

private enum class Operator {
    AND, OR, XOR
}

private fun Operator(input: String) = when (input) {
    "OR" -> OR
    "XOR" -> XOR
    "AND" -> AND
    else -> error("Invalid operator: $input")
}
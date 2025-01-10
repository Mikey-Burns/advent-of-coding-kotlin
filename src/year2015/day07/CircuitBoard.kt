package year2015.day07

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = CircuitBoard(input.map { CircuitBoard.Circuit.of(it) }).runCircuits()

    fun part2(input: List<String>): Int = part1(input)
        .let { aValue ->
            val circuits = input.map { CircuitBoard.Circuit.of(it) }
            val bCircuit = circuits.single { circuit -> circuit.output == "b" }
            val newCircuits = circuits - bCircuit + CircuitBoard.Circuit.ValueWire(aValue.toString(), "b")
            CircuitBoard(newCircuits).runCircuits()
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test", "2015")
    check(part1(testInput) == 65079)

    val input = readInput("Day07", "2015")
    part1(input).println()
    part2(input).println()
}

private data class CircuitBoard(val circuitInput: Collection<Circuit>) {
    private val circuits = circuitInput.toSet()

    fun runCircuits(): Int {
        val processedCircuits = mutableSetOf<Circuit>()
        val wireMap = mutableMapOf<String, Int>()

        fun String.fromMapOrAsInt(): Int = wireMap.getOrElse(this) { this.toInt() }

        while (processedCircuits != circuits) {
            circuits.forEach { circuit ->
                if (circuit.inputs
                        .filter { input -> input.none { it.isDigit() } }
                        .all { wireMap.containsKey(it) }
                ) {
                    wireMap[circuit.output] = when (circuit) {
                        // Limit to 16 bits
                        is Circuit.NotWire -> circuit.input.fromMapOrAsInt().inv() and 65535
                        is Circuit.AndWire -> circuit.left.fromMapOrAsInt() and circuit.right.fromMapOrAsInt()
                        is Circuit.OrWire -> circuit.left.fromMapOrAsInt() or circuit.right.fromMapOrAsInt()
                        is Circuit.LShiftWire -> circuit.input.fromMapOrAsInt() shl circuit.shift
                        is Circuit.RShiftWire -> circuit.input.fromMapOrAsInt() shr circuit.shift
                        is Circuit.ValueWire -> circuit.input.fromMapOrAsInt()
                    }
                    processedCircuits.add(circuit)
                }
            }
        }

        return wireMap.getValue("a")
    }

    sealed class Circuit(val inputs: List<String>, val output: String) {
        class ValueWire(val input: String, output: String) : Circuit(listOf(input), output)
        class NotWire(val input: String, output: String) : Circuit(listOf(input), output)
        class AndWire(val left: String, val right: String, output: String) :
            Circuit(listOf(left, right), output)

        class OrWire(val left: String, val right: String, output: String) :
            Circuit(listOf(left, right), output)

        class LShiftWire(val input: String, val shift: Int, output: String) :
            Circuit(listOf(input), output)

        class RShiftWire(val input: String, val shift: Int, output: String) :
            Circuit(listOf(input), output)

        companion object {
            fun of(wire: String): Circuit {
                val split = wire.split(" ")
                return when (split.size) {
                    3 -> ValueWire(split[0], split[2])
                    4 -> NotWire(split[1], split[3])
                    else -> when (split[1]) {
                        "AND" -> AndWire(split[0], split[2], split[4])
                        "OR" -> OrWire(split[0], split[2], split[4])
                        "LSHIFT" -> LShiftWire(split[0], split[2].toInt(), split[4])
                        "RSHIFT" -> RShiftWire(split[0], split[2].toInt(), split[4])
                        else -> error("Bad wire: $wire")
                    }
                }
            }
        }
    }
}
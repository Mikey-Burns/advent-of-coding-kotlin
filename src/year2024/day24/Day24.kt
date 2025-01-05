package year2024.day24

import println
import readInput
import year2024.day24.Operator.*

fun main() {
    fun part1(input: List<String>): Long = input.toWireMap().processWires()

    fun part2(input: List<String>): String = input.toWireMap()
        .let {
            val swaps = listOf(
                "z14" to "hbk",
                "z18" to "kvn",
                "z23" to "dbb",
                "cvh" to "tfn"
            )
            it.swapGates(swaps)
                .also { wireMap ->
                    wireMap.processWires()
                    val mappedGates =
                        wireMap.mapGates().toList().sortedWith(compareBy { (_, value) -> value }).toMap()
                    wireMap.printXYZ()
                    println()
                }
            return swaps.flatMap(Pair<String, String>::toList).sorted().joinToString(",")
        }

    // test if implementation meets criteria from the description, like:
    val smallInput = readInput("Day24_test", "2024")
    val bigInput = readInput("Day24_test2", "2024")
    check(part1(smallInput) == 4L)
    check(part1(bigInput) == 2024L)

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
                    wires[gate.destination] = when (gate.operator) {
                        AND -> wires.getValue(gate.first)!! && wires.getValue(gate.second)!!
                        OR -> wires.getValue(gate.first)!! || wires.getValue(gate.second)!!
                        XOR -> wires.getValue(gate.first)!! xor wires.getValue(gate.second)!!
                    }
                    processedGates.add(gate)
                }
            }
        }

        return findForLetter('z').toLong(2)
    }

    fun mapGates(): Map<String, String> = buildMap {
        fun mappedHeritage(name: String): String = getOrPut(name) {
            val myGate = gates.singleOrNull { gate -> gate.destination == name } ?: return name
            val (firstValue, secondValue) = listOf(mappedHeritage(myGate.first), mappedHeritage(myGate.second)).sorted()

            "($firstValue ${myGate.operator} $secondValue)"
        }

        wires.keys.forEach { wire ->
            getOrPut(wire) { traceHeritage(wire) }
        }
    }

    fun swapGates(swaps: List<Pair<String, String>>): WireMap {
        if (swaps.isEmpty()) return this
        return copy(gates = swaps.fold(gates) { newGates, (d1, d2) ->
            val g1 = newGates.single { gate -> gate.destination == d1 }
            val g2 = newGates.single { gate -> gate.destination == d2 }
            (((newGates - g1) - g2) + g1.copy(destination = d2)) + g2.copy(destination = d1)
        })
    }

    private fun findForLetter(letter: Char): String = buildString {
        wires.filterKeys { it.startsWith(letter) }
            .toList()
            .sortedBy { it.first }
            .reversed()
            .forEach { (_, wireState) -> append(if (wireState == true) "1" else "0") }
    }

    fun printXYZ() {
        val x = findForLetter('x')
        println("x: $x - ${x.toLong(2)}")
        val y = findForLetter('y')
        println("y: $y - ${y.toLong(2)}")
        val desired = x.toLong(2) + y.toLong(2)
        println("d: ${desired.toULong().toString(2)} - $desired")
        val z = findForLetter('z')
        println("z: $z - ${z.toLong(2)}")

        val expected = desired.toULong().toString(2)

        expected.reversed().zip(z.reversed())
            .forEachIndexed { index, pair ->
                val wire = "z${index.toString().padStart(2, '0')}"
                if (pair.first != pair.second) {
                    println("$wire does not match")
                }

                // Find heritage

                val withNames = traceHeritage(wire)
                println("$wire = $withNames")
            }
    }


    fun traceHeritage(wire: String): String {
        val myGate = gates.singleOrNull { gate -> gate.destination == wire } ?: return wire
        val (firstValue, secondValue) = listOf(traceHeritage(myGate.first), traceHeritage(myGate.second)).sorted()

        return "($firstValue ${myGate.operator} $secondValue)"
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
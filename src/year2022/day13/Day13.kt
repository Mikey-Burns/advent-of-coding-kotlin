package year2022.day13

import println
import readInput
import year2022.day13.Packet.Group
import year2022.day13.Packet.Value

fun main() {
    fun part1(input: List<String>): Long = input.toPairs()
        .mapIndexed { index, pair -> if (pair.inRightOrder()) index + 1L else 0L }
        .sum()

    fun part2(input: List<String>): Long = input.filter(String::isNotEmpty).map(String::toPacket)
        .let { it + Group(2) + Group(6) }
        .sortedWith { left, right -> if (left.inRightOrder(right)) -1 else 1 }
        .let { (it.indexOf(Group(2)) + 1) * (it.indexOf(Group(6)) + 1) }
        .toLong()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test", "2022")
    check(part1(testInput).also(::println) == 13L)
    val testInput2 = readInput("Day13_test", "2022")
    check(part2(testInput2).also(::println) == 140L)

    val input = readInput("Day13", "2022")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.toPairs(): List<PacketPair> =
    this.chunked(3) { (first, second, _) -> PacketPair(first.toPacket(), second.toPacket()) }

private data class PacketPair(val left: Packet, val right: Packet) {
    fun inRightOrder(): Boolean = left.inRightOrder(right)
}

private sealed interface Packet {
    fun inRightOrder(right: Packet): Boolean

    fun areEqual(right: Packet): Boolean

    data class Group(val packets: List<Packet>) : Packet {
        constructor(packet: Packet) : this(listOf(packet))
        constructor(value: Int) : this(Value(value))

        private fun dropOne() = copy(packets = packets.drop(1))
        override fun inRightOrder(right: Packet): Boolean {
            return when (right) {
                is Group -> when {
                    packets.isEmpty() -> true
                    right.packets.isEmpty() -> false
                    packets.first().areEqual(right.packets.first()) -> dropOne().inRightOrder(right.dropOne())
                    else -> packets.first().inRightOrder(right.packets.first())
                }

                is Value -> inRightOrder(Group(right))
            }
        }

        override fun areEqual(right: Packet): Boolean = when (right) {
            is Group -> packets.size == right.packets.size
                    && packets.zip(right.packets).all { (l, r) -> l.areEqual(r) }

            is Value -> packets.size == 1 && packets.first() is Value
                    && (packets.first() as Value).value == right.value
        }

        override fun toString(): String = "[${packets.joinToString(",")}]"
    }

    data class Value(val value: Int) : Packet {
        override fun inRightOrder(right: Packet): Boolean = when (right) {
            is Group -> Group(this).inRightOrder(right)
            is Value -> value <= right.value
        }

        override fun areEqual(right: Packet): Boolean = when (right) {
            is Group -> Group(this).areEqual(right)
            is Value -> value == right.value
        }

        override fun toString(): String = value.toString()
    }
}

private fun String.toPacket(): Group {

    fun String.buildGroup(): Pair<Group, Int> {
        var index = 0
        val packets = mutableListOf<Packet>()
        while (index <= lastIndex) {
            if (this[index] == '[') {
                // Move past the open bracket
                index += 1
                // Substring without the bracket
                this.drop(index).buildGroup()
                    .also { (group, offset) ->
                        packets.add(group)
                        index += offset
                    }
            } else if (this[index] == ']') {
                return Group(packets) to index + 1
            } else if (this[index] == ',') {
                index++
            } else {
                this.substring(index).takeWhile { it.isDigit() }
                    .also { index += it.length }
                    .toInt()
                    .let(::Value)
                    .also(packets::add)
            }
        }

        error("Mismatched brackets!")
    }

    return this.drop(1).buildGroup().first
}
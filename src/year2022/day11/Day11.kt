package year2022.day11

import lcm
import println
import readInput
import java.util.*

fun main() {
    fun part1(input: List<String>): Long {
        return input.toMonkeys().processRounds(20) { it / 3 }.calculateMonkeyBusiness()
    }

    fun part2(input: List<String>): Long {
        return input.toMonkeys()
            .let { monkeys ->
                monkeys.processRounds(10000) { it % monkeys.map(Monkey::divisor).lcm() }
            }.calculateMonkeyBusiness()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test", "2022")
    check(part1(testInput).also(::println) == 10605L)
    val testInput2 = readInput("Day11_test", "2022")
    check(part2(testInput2).also(::println) == 2713310158L)

    val input = readInput("Day11", "2022")
    part1(input).println()
    part2(input).println()
}

private data class Monkey(
    val items: Queue<Long>,
    val operation: (Long) -> Long,
    val divisor: Long,
    val destinations: Pair<Int, Int>
) {
    var itemsInspected = 0
}

private fun List<String>.toMonkeys(): List<Monkey> = this.chunked(7) {
    val queue = LinkedList<Long>().apply { addAll(it[1].substringAfter(": ").split(", ").map(String::toLong)) }
    val operation: (Long) -> Long = with(it[2].substringAfter("old ")) {
        when (this.first()) {
            '*' -> { worry -> worry * (substringAfter(" ").toLongOrNull() ?: worry) }
            '+' -> { worry -> worry + this.substringAfter(" ").toLong() }
            else -> error("Invalid operation!")
        }
    }
    val divisor = it[3].substringAfter(" by ").toLong()
    val destinations = it[4].substringAfter("monkey ").toInt() to it[5].substringAfter("monkey ").toInt()
    Monkey(queue, operation, divisor, destinations)
}

private fun List<Monkey>.processRounds(numberOfRounds: Int = 1, worryOperation: (Long) -> Long): List<Monkey> = also {
    repeat(numberOfRounds) {
        this.forEach { monkey ->
            while (monkey.items.isNotEmpty()) {
                monkey.itemsInspected++
                val startingValue = monkey.items.poll()
                val worry = worryOperation(monkey.operation(startingValue))
                if (worry % monkey.divisor == 0L) {
                    this[monkey.destinations.first].items.add(worry)
                } else {
                    this[monkey.destinations.second].items.add(worry)
                }
            }
        }
    }
}

private fun List<Monkey>.calculateMonkeyBusiness(): Long = this.sortedByDescending { it.itemsInspected }
    .take(2)
    .fold(1L) { acc, monkey -> acc * monkey.itemsInspected }
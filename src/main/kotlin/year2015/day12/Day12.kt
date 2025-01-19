package year2015.day12

import kotlinx.serialization.json.*
import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { Json.parseToJsonElement(it).sum() }

    fun part2(input: List<String>): Int = input.sumOf { Json.parseToJsonElement(it).sumNoReds() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test", "2015")
    check(part1(testInput) == 18)
    val testInput2 = readInput("Day12_test2", "2015")
    check(part2(testInput2) == 16)

    val input = readInput("Day12", "2015")
    part1(input).println()
    part2(input).println()
}

private fun JsonElement.sum(): Int {
    return when (this) {
        is JsonArray -> this.sumOf(JsonElement::sum)
        is JsonObject -> this.values.sumOf(JsonElement::sum)
        is JsonPrimitive -> this.intOrNull ?: 0
        else -> 0
    }
}

private fun JsonElement.sumNoReds(): Int {
    return when (this) {
        is JsonArray -> this.sumOf(JsonElement::sumNoReds)
        is JsonObject -> {
            if (values.any { value -> value.toString() == "\"red\"" }) 0 else this.values.sumOf(JsonElement::sumNoReds)
        }

        is JsonPrimitive -> this.intOrNull ?: 0
        else -> 0
    }
}
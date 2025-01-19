package year2015.day16

import utils.println
import utils.readInput

fun main() {
    val idealSue = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    )

    fun part1(input: List<String>): Int = input.map { Sue.of(it) }
        .single { sue -> sue.matches(idealSue) }.number

    fun part2(input: List<String>): Int = input.map { Sue.of(it) }
        .single { sue -> sue.fuzzyMatches(idealSue) }.number

    // test if implementation meets criteria from the description, like:

    val input = readInput("Day16", "2015")
    part1(input).println()
    part2(input).println()
}

private data class Sue(val number: Int, val properties: Map<String, Int>) {

    fun matches(idealSue: Map<String, Int>): Boolean = properties.all { (name, value) -> idealSue[name] == value }

    fun fuzzyMatches(idealSue: Map<String, Int>): Boolean = properties.all { (name, value) ->
        when (name) {
            "cats", "trees" -> value > idealSue.getValue(name)
            "pomeranians", "goldfish" -> value < idealSue.getValue(name)
            else -> value == idealSue[name]
        }
    }

    companion object {
        fun of(line: String): Sue {
            val number = line.substringAfter("Sue ").substringBefore(":").toInt()
            val split = line.split(" ").map { it.trimEnd(':', ',') }
            val properties = mapOf(
                split[2] to split[3].toInt(),
                split[4] to split[5].toInt(),
                split[6] to split[7].toInt()
            )

            return Sue(number, properties)
        }
    }
}
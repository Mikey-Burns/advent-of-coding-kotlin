package year2015.day19

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = input.toChemistryMachine().distinctMolecules()

    fun part2(input: List<String>): Int = 0

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test", "2015")
    check(part1(testInput) == 4)
    val testInput2 = readInput("Day19_test2", "2015")
    check(part2(testInput2) == 0)

    val input = readInput("Day19", "2015")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.toChemistryMachine(): ChemistryMachine {
    val replacements = mutableListOf<Pair<String, String>>()
    this.takeWhile(String::isNotBlank)
        .forEach { line ->
            val (source, destination) = line.split(" => ")
            replacements.add(source to destination)
        }
    val startingMolecule = this.last()

    return ChemistryMachine(replacements, startingMolecule)
}

private data class ChemistryMachine(val replacements: List<Pair<String, String>>, val startingMolecule: String) {

    fun distinctMolecules(): Int {
        val molecules = mutableSetOf<String>()
        replacements.forEach { (key, replacement) ->
            key.toRegex().findAll(startingMolecule)
                .forEach { matchResult ->
                    molecules.add(startingMolecule.replaceRange(matchResult.range, replacement))
                }
        }

        return molecules.size
    }
}
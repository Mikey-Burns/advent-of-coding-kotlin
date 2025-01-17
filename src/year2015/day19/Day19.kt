package year2015.day19

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = input.toChemistryMachine().distinctMolecules()

    fun part2(input: List<String>): Int = input.toChemistryMachine().magic()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test", "2015")
    check(part1(testInput) == 4)
    val testInput2 = readInput("Day19_test2", "2015")
    // Add one back because there are direct transitions from e
    check(part2(testInput2) + 1 == 6)

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


    fun magic(): Int {
        val subbed = replacements.fold(
            // Reddit pointed out that Y is always in the middle of other clauses, make it a comma
            startingMolecule.replace("Y", ",")
                // Rn always leads before we get to Ar
                .replace("Rn", "(")
                // Ar always follows Rn
                .replace("Ar", ")")
        ) { s, (key, _) ->
            // Convert our keys to a single character
            s.replace(key, "X")
        }
        // Length of string - number of wrappers - (2x instances of Y because it takes out 2 characters)
        // - 1 because the initial step always goes from 1 character to 2
        return subbed.count() - subbed.count { it in "()" } - (2 * subbed.count { it == ',' }) - 1
    }
}
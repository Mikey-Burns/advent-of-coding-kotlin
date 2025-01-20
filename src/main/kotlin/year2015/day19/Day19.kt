package year2015.day19

import utils.Element.NonTerminal
import utils.Element.Terminal
import utils.ElementalCykParser
import utils.ElementalRule
import utils.println
import utils.readInput
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

fun main() {
    fun part1(input: List<String>): Int = input.toChemistryMachine().distinctMolecules()

    fun part2(input: List<String>): Int = input.toChemistryMachine().cykAlgorithm()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test", "2015")
    check(part1(testInput) == 4)
    val testInput2 = readInput("Day19_test2", "2015")
//    check(part2(testInput2)== 6)

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

    fun cykAlgorithm(): Int {
        val nameMap = mutableMapOf<String, String>()
        fun buildGrammar(): List<ElementalRule> {
            val keys = replacements.map { it.first }.toSet()
            val nonTerminals = keys.associateWith { NonTerminal(it) }
            val rules = replacements.map { (key, replacement) ->
                val right = Regex("[A-Z][a-z]?").findAll(replacement)
                    .map { matchResult ->
                        when {
                            matchResult.value in keys -> {
                                // We found a non-terminal
                                NonTerminal(matchResult.value)
                            }

                            matchResult.value.length == 1 -> {
                                // We found a terminal capital letter
                                Terminal(matchResult.value)
                            }

                            else -> {
                                // We have a terminal with two characters
                                Terminal(matchResult.value)
                            }
                        }
                    }
                    .toList()

                ElementalRule(nonTerminals.getValue(key), right)
            }

            fun List<ElementalRule>.toChomskyNormalForm(): List<ElementalRule> {
                var ruleCount = 0

                fun nextName(): String = ruleCount++.toString().padStart(2, '0')

                fun ElementalRule.toValidRules(): List<ElementalRule> = when {
                    isValid() -> listOf(this)
                    right.size == 2 -> {
                        // Invalid with size two means a mix of terminal and non-terminal
                        val newNonTerminal = NonTerminal(nextName())
                        val newTerminalRule = ElementalRule(newNonTerminal, right.filterIsInstance<Terminal>())
                        val newRight = if (right[0] is NonTerminal) {
                            nameMap[newNonTerminal.text] = right[0].text
                            listOf(newNonTerminal, right[1])
                        } else {
                            nameMap[newNonTerminal.text] = right[1].text
                            listOf(right[0], newNonTerminal)
                        }
                        val newNonTerminalRule = ElementalRule(left, newRight)
                        listOf(newNonTerminalRule, newTerminalRule)
                    }

                    right.size > 2 -> {
                        val newNonTerminal = NonTerminal(nextName())
                        val newChildRule = ElementalRule(newNonTerminal, right.take(2))
                        nameMap[newNonTerminal.text] = right.take(2).joinToString("_") { it.text }
                        val newParentRule = ElementalRule(left, listOf(newNonTerminal) + right.drop(2))
                        listOf(newChildRule, newParentRule).flatMap(ElementalRule::toValidRules)
                    }

                    else -> error("Uh oh")
                }

                return this.flatMap(ElementalRule::toValidRules)
            }

            return rules.toChomskyNormalForm()
        }

        val rules = buildGrammar()

        val inString = ElementalCykParser("e", rules, "[A-Z][a-z]?")
            .isStringInComplexGrammar(startingMolecule)


        return 6
    }
}

private fun ElementalRule.isValid(): Boolean = when {
    right.size == 1 && right[0] is Terminal -> true
    right.size > 2 -> false
    right.size == 2 && right.all { it is NonTerminal } -> true
    else -> false
}
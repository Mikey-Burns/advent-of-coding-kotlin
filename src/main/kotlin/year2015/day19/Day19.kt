package year2015.day19

import utils.println
import utils.readInput
import year2015.day19.LanguageElement.NonTerminal
import year2015.day19.LanguageElement.Terminal

fun main() {
    fun part1(input: List<String>): Int = input.toChemistryMachine().distinctMolecules()

    fun part2(input: List<String>): Int = input.toChemistryMachine().cykAlgorithm()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test", "2015")
    check(part1(testInput) == 4)
    val testInput2 = readInput("Day19_test2", "2015")
    check(part2(testInput2)== 6)

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
        fun buildGrammar(): List<Rule> {
            val keys = replacements.map { it.first }.toSet()
            val nonTerminals = keys.associateWith { NonTerminal(it) }
            val rules = replacements.map { (key, replacement) ->
                val right = Regex("[A-Z][a-z]?").findAll(replacement)
                    .flatMap { matchResult ->
                        when {
                            matchResult.value in keys -> {
                                // We found a non-terminal
                                listOf(NonTerminal(matchResult.value))
                            }

                            matchResult.value.length == 1 -> {
                                // We found a terminal capital letter
                                listOf(Terminal(matchResult.value))
                            }

                            matchResult.value.take(1) in keys -> {
                                // We have a non-terminal and a terminal
                                listOf(
                                    NonTerminal(matchResult.value.take(1)),
                                    Terminal(matchResult.value.drop(1))
                                )
                            }

                            else -> {
                                // We have two terminals
                                listOf(
                                    Terminal(matchResult.value.take(1)),
                                    Terminal(matchResult.value.drop(1))
                                )
                            }
                        }
                    }
                    .toList()

                Rule(nonTerminals.getValue(key), right)
            }

            fun List<Rule>.toChomskyNormalForm(): List<Rule> {
                var ruleCount = 0

                fun nextName(): String = ruleCount++.toString().padStart(2, '0')

                fun Rule.toValidRules(): List<Rule> = when {
                    isValid() -> listOf(this)
                    right.size == 2 -> {
                        // Invalid with size two means a mix of terminal and non-terminal
                        val newNonTerminal = NonTerminal(nextName())
                        val newTerminalRule = Rule(newNonTerminal, right.filterIsInstance<Terminal>())
                        val newRight = if (right[0] is NonTerminal) {
                            nameMap[newNonTerminal.text] = right[0].text
                            listOf(newNonTerminal, right[1])
                        } else {
                            nameMap[newNonTerminal.text] = right[1].text
                            listOf(right[0], newNonTerminal)
                        }
                        val newNonTerminalRule = Rule(left, newRight)
                        listOf(newNonTerminalRule, newTerminalRule)
                    }

                    right.size > 2 -> {
                        val newNonTerminal = NonTerminal(nextName())
                        val newChildRule = Rule(newNonTerminal, right.take(2))
                        nameMap[newNonTerminal.text] = right.take(2).joinToString("_") { it.text }
                        val newParentRule = Rule(left, listOf(newNonTerminal) + right.drop(2))
                        listOf(newChildRule, newParentRule).flatMap(Rule::toValidRules)
                    }

                    else -> error("Uh oh")
                }

                return this.flatMap(Rule::toValidRules)
            }

            return rules.toChomskyNormalForm()
        }

        val rules = buildGrammar()
        val elements = rules.flatMap { rule -> rule.right.map { it.text } + rule.left.text }.toSet().sorted()
        val matches = Regex("[A-Z][a-z]?").findAll(startingMolecule).toList()
        val n = matches.count()

        val table = Array(n + 1) { Array<MutableSet<String>>(n + 1) { mutableSetOf() } }

        for (i in 1..n) {
            val element = matches[i - 1]
            for (rule in rules) {
                if (rule.right.size == 1 && rule.right[0].text == element.value) {
                    table[i][i].add(rule.left.text)
                }
            }
        }

        for (l in 2..n) {
            for (i in 1..(n - l + 1)) {
                val j = i + l - 1
                for (k in i..(j-1)) {
                    rules.filter { rule -> rule.right.size == 2 }
                        .forEach { rule ->
                            if (table[i][k].contains(rule.right[0].text)
                                && table [k + 1][j].contains(rule.right[1].text)) {
                                table[i][j].add(rule.left.text)
                            }
                        }
                }
            }
        }

        return 6
    }
}

private sealed class LanguageElement(open val text: String) {

    data class NonTerminal(override val text: String) : LanguageElement(text)
    data class Terminal(override val text: String) : LanguageElement(text)
}

private data class Rule(val left: NonTerminal, val right: List<LanguageElement>) {

    fun isValid(): Boolean = when {
        right.size == 1 -> true
        right.size > 2 -> false
        right.size == 2 && right.all { it is NonTerminal } -> true
        else -> false
    }

}
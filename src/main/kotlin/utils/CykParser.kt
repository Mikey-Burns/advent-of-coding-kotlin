package utils

import utils.Element.NonTerminal
import utils.Element.Terminal

class CykParser(start: Char, rules: List<CykRule>) {
    private val nonTerminalKeys = rules.map { rule -> rule.left.toString() }.toSet()
    private val elementalRules = rules.map { rule ->
        val left = NonTerminal(rule.left.toString())
        val right = rule.right
            .map { it.toString() }
            .map { c ->
                if (c in nonTerminalKeys) NonTerminal(c) else Terminal(c)
            }
        ElementalRule(left, right)
    }

    private val elementalParser = ElementalCykParser(start.toString(), elementalRules)

    fun isStringInGrammar(input: String): Boolean = elementalParser.isStringInGrammar(input)
}

data class CykRule(val left: Char, val right: String)

class ElementalCykParser(private val start: String, rules: List<ElementalRule>, private val nonTerminalRegex: String = "[A-Z]") {
    private val nonTerminalRules = rules.filter { rule -> rule.right.size == 2 }
    private val terminalRules = rules.filter { rule -> rule.right.size == 1 && rule.right[0] is Terminal }
    private val nonTerminalKeys = nonTerminalRules.map { rule -> rule.left.text }.toSet()
    private val terminalKeys = terminalRules.flatMap { rule -> rule.right.map { it.text } }.toSet()

    fun isStringInGrammar(input: String): Boolean {
        val dp = Array(input.length) { Array<MutableSet<String>>(input.length) { mutableSetOf() } }
        // Base case
        for (i in input.indices) {
            for (rule in terminalRules) {
                if (rule.right[0].text == input[i].toString()) {
                    dp[i][i].add(rule.left.text)
                }
            }
        }
        // Iterate
        for (length in 2..input.length) {
            for (i in 0..(input.length - length)) {
                val j = i + (length - 1)
                for (k in i..<j) {
                    for (rule in nonTerminalRules) {
                        if (dp[i][k].contains(rule.right[0].text) && dp[k + 1][j].contains(rule.right[1].text)) {
                            dp[i][j].add(rule.left.text)
                        }
                    }
                }
            }
        }

        return dp[0][input.lastIndex].contains(start)
    }

    fun isStringInComplexGrammar(input: String): Boolean {
        if (!Regex("($nonTerminalRegex)+").matches(input)) return false
        val inputMatches = Regex(nonTerminalRegex).findAll(input)
            .flatMap { matchResult ->
                when (matchResult.value) {
                    in nonTerminalKeys -> listOf(NonTerminal(matchResult.value))
                    in terminalKeys -> listOf(Terminal(matchResult.value))
                    else -> {
                        // Going to be false
                        listOf(Terminal(matchResult.value))
                    }
                }
            }
            .toList()

        val dp = Array(inputMatches.size) { Array<MutableSet<String>>(inputMatches.size) { mutableSetOf() } }
        // Base case
        for (i in inputMatches.indices) {
            for (rule in terminalRules) {
                if (rule.right[0].text == inputMatches[i].text) {
                    dp[i][i].add(rule.left.text)
                }
            }
        }
        // Iterate
        for (length in 2..inputMatches.size) {
            for (i in 0..(inputMatches.size - length)) {
                val j = i + (length - 1)
                for (k in i..<j) {
                    for (rule in nonTerminalRules) {
                        if (dp[i][k].contains(rule.right[0].text) && dp[k + 1][j].contains(rule.right[1].text)) {
                            dp[i][j].add(rule.left.text)
                        }
                    }
                }
            }
        }

        return dp[0][inputMatches.lastIndex].contains(start)
    }
}

sealed class Element(open val text: String) {

    data class NonTerminal(override val text: String) : Element(text)
    data class Terminal(override val text: String) : Element(text)
}

data class ElementalRule(val left: NonTerminal, val right: List<Element>) {
    constructor(left: NonTerminal, vararg right: Element) : this(left, right.toList())
}
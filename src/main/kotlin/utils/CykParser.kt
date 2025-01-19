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

class ElementalCykParser(private val start: String, rules: List<ElementalRule>) {
    private val nonTerminalRules = rules.filter { rule -> rule.right.size == 2 }
    private val terminalRules = rules.filter { rule -> rule.right.size == 1 && rule.right[0] is Terminal }

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
}

sealed class Element(open val text: String) {

    data class NonTerminal(override val text: String) : Element(text)
    data class Terminal(override val text: String) : Element(text)
}

data class ElementalRule(val left: NonTerminal, val right: List<Element>) {
    constructor(left: NonTerminal, vararg right: Element) : this(left, right.toList())
}
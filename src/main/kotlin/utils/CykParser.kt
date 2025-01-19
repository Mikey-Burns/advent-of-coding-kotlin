package utils

class CykParser(private val start: Char, rules: List<CykRule>) {
    private val nonTerminalRules = rules.filter { rule -> rule.right.length == 2 }
    private val terminalRules = rules.filter { rule -> rule.right.length == 1 }

    fun isStringInGrammar(input: String): Boolean {
        val dp = Array(input.length) { Array<MutableSet<Char>>(input.length) { mutableSetOf() } }
        // Base case
        for (i in input.indices) {
            for (rule in terminalRules) {
                if (rule.right[0] == input[i]) {
                    dp[i][i].add(rule.left)
                }
            }
        }
        // Iterate
        for (length in 2..input.length) {
            for (i in 0..(input.length - length)) {
                val j = i + (length - 1)
                for (k in i..<j) {
                    for (rule in nonTerminalRules) {
                        if (dp[i][k].contains(rule.right[0]) && dp[k + 1][j].contains(rule.right[1])) {
                            dp[i][j].add(rule.left)
                        }
                    }
                }
            }
        }

        return dp[0][input.lastIndex].contains(start)
    }
}

data class CykRule(val left: Char, val right: String)
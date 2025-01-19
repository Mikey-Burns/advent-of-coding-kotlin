package utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CykParserTest {

    @Test
    fun simpleGrammarHandlesSingleLetters() {
        val rules = listOf(
            CykRule('S', "a")
        )
        val grammar = CykParser('S', rules)
        assertTrue(grammar.isStringInGrammar("a"))
        assertFalse(grammar.isStringInGrammar("b"))
    }

    @Test
    fun grammarWithOneIndirect() {
        val rules = listOf(
            CykRule('S', "AB"),
            CykRule('A', "a"),
            CykRule('B', "b"),
        )
        val grammar = CykParser('S', rules)
        assertTrue(grammar.isStringInGrammar("ab"))
        assertFalse(grammar.isStringInGrammar("a"))
        assertFalse(grammar.isStringInGrammar("b"))
        assertFalse(grammar.isStringInGrammar("ba"))
        assertFalse(grammar.isStringInGrammar("aba"))
    }

    @Test
    fun grammarWithMultipleIndirects() {
        val rules = listOf(
            CykRule('S', "AB"),
            CykRule('S', "BC"),
            CykRule('A', "BA"),
            CykRule('A', "a"),
            CykRule('B', "CC"),
            CykRule('B', "b"),
            CykRule('C', "AB"),
            CykRule('C', "a"),
        )
        val grammar = CykParser('S', rules)
        assertTrue(grammar.isStringInGrammar("ab"))
        assertTrue(grammar.isStringInGrammar("baaba"))
        assertTrue(grammar.isStringInGrammar("ba"))

        assertFalse(grammar.isStringInGrammar("aba"))
        assertFalse(grammar.isStringInGrammar("a"))
        assertFalse(grammar.isStringInGrammar("b"))
        assertFalse(grammar.isStringInGrammar("c"))
    }
}
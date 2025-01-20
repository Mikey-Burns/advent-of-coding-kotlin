package utils

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import utils.Element.NonTerminal
import utils.Element.Terminal

class CykParserTest {

    // region Simple CYK
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
    // endregion

    // region Elemental CYK, assume keys are still 1 character capitals
    @Test
    fun simpleElementalGrammarHandlesSingleLetters() {
        val rules = listOf(
            ElementalRule(NonTerminal("S"), Terminal("a"))
        )
        val grammar = ElementalCykParser("S", rules)
        assertTrue(grammar.isStringInGrammar("a"))
        assertFalse(grammar.isStringInGrammar("b"))
    }

    @Test
    fun elementalGrammarWithOneIndirect() {
        val rules = listOf(
            ElementalRule(
                NonTerminal("S"),
                listOf(
                    NonTerminal("A"), NonTerminal("B")
                )
            ),
            ElementalRule(NonTerminal("A"), Terminal("a")),
            ElementalRule(NonTerminal("B"), Terminal("b")),
        )
        val grammar = ElementalCykParser("S", rules)
        assertTrue(grammar.isStringInGrammar("ab"))
        assertFalse(grammar.isStringInGrammar("a"))
        assertFalse(grammar.isStringInGrammar("b"))
        assertFalse(grammar.isStringInGrammar("ba"))
        assertFalse(grammar.isStringInGrammar("aba"))
    }

    @Test
    fun elementalGrammarWithMultipleIndirects() {
        val rules = listOf(
            ElementalRule(
                NonTerminal("S"),
                listOf(NonTerminal("A"), NonTerminal("B"))
            ),
            ElementalRule(
                NonTerminal("S"),
                listOf(NonTerminal("B"), NonTerminal("C"))
            ),
            ElementalRule(
                NonTerminal("A"),
                listOf(NonTerminal("B"), NonTerminal("A"))
            ),
            ElementalRule(
                NonTerminal("A"),
                Terminal("a")
            ),
            ElementalRule(
                NonTerminal("B"),
                listOf(NonTerminal("C"), NonTerminal("C"))
            ),
            ElementalRule(
                NonTerminal("B"),
                Terminal("b")
            ),
            ElementalRule(
                NonTerminal("C"),
                listOf(NonTerminal("A"), NonTerminal("B"))
            ),
            ElementalRule(
                NonTerminal("C"),
                Terminal("a")
            )
        )
        val grammar = ElementalCykParser("S", rules)
        assertTrue(grammar.isStringInGrammar("ab"))
        assertTrue(grammar.isStringInGrammar("baaba"))
        assertTrue(grammar.isStringInGrammar("ba"))

        assertFalse(grammar.isStringInGrammar("aba"))
        assertFalse(grammar.isStringInGrammar("a"))
        assertFalse(grammar.isStringInGrammar("b"))
        assertFalse(grammar.isStringInGrammar("c"))
    }
    // endregion

    // region Elemental CYK, complicatedKeys
    @Test
    fun complexElementalGrammarHandlesLetters() {
        val rules = listOf(
            ElementalRule(NonTerminal("S"), Terminal("Zb"))
        )
        val grammar = ElementalCykParser("S", rules, "[A-Z][a-z]?")
        assertTrue(grammar.isStringInComplexGrammar("Zb"))
        assertFalse(grammar.isStringInComplexGrammar("A"))
        assertFalse(grammar.isStringInComplexGrammar("B"))
        assertFalse(grammar.isStringInComplexGrammar("BA"))
        assertFalse(grammar.isStringInComplexGrammar("Z"))
        assertFalse(grammar.isStringInComplexGrammar("a"))
        assertFalse(grammar.isStringInComplexGrammar("z"))
    }

    @Test
    fun complexElementalGrammarWithMultipleRules() {
        val rules = listOf(
            ElementalRule(
                NonTerminal("S"),
                listOf(NonTerminal("A"), NonTerminal("Bs"))
            ),
            ElementalRule(
                NonTerminal("A"),
                Terminal("Cd")
            ),
            ElementalRule(
                NonTerminal("Bs"),
                Terminal("Zz")
            )
        )
        val grammar = ElementalCykParser("S", rules, "[A-Z][a-z]?")
        assertTrue(grammar.isStringInComplexGrammar("CdZz"))
        assertTrue(grammar.isStringInComplexGrammar("CdBs"))
        assertFalse(grammar.isStringInComplexGrammar("B"))
        assertFalse(grammar.isStringInComplexGrammar("BA"))
        assertFalse(grammar.isStringInComplexGrammar("Z"))
        assertFalse(grammar.isStringInComplexGrammar("a"))
        assertFalse(grammar.isStringInComplexGrammar("z"))
    }
    // endregion
}
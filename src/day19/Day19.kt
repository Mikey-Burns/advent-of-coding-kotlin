package day19

import println
import readInput
import java.util.function.Predicate

private const val MIN = 1L
private const val MAX = 4000L

fun main() {
    fun part1(input: List<String>): Long {
        return PartFactory(input).runParts()
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput).also(::println) == 19114L)
    val testInput2 = readInput("Day19_test")
    check(part2(testInput2).also(::println) == 0L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}

private data class PartFactory(val workflows: List<Workflow>, val parts: List<Part>) {
    val workflowMap = workflows.associateBy { it.name }

    fun runParts(): Long = parts.filter(::runPart).sumOf(Part::sum)

    fun runPart(part: Part): Boolean {
        var workflow = workflowMap.getValue("in")
        while (workflow.name !in listOf("A", "R")) {
            val newName = workflow.checkPart(part)
            if (newName == "A") return true
            if (newName == "R") return false
            workflow = workflowMap.getValue(newName)
        }
        error("Never get here")
    }
}

private data class WorkflowRanges(
    val xRange: LongRange = 1..4000L,
    val mRange: LongRange = 1..4000L,
    val aRange: LongRange = 1..4000L,
    val sRange: LongRange = 1..4000L
)

private fun PartFactory(input: List<String>): PartFactory {
    val split = input.indexOfFirst(String::isEmpty)
    val workflows = input.subList(0, split).map(::Workflow)
    val parts = input.subList(split + 1, input.size).map(::Part)
    return PartFactory(workflows, parts)
}

private data class Workflow(val name: String, val rules: List<Rule>) {

    fun checkPart(part: Part): String = rules.first { it.test(part) }.destination
}

private fun Workflow(line: String): Workflow {
    val name = line.substringBefore("{")
    val rules = line.substringAfter("{")
        .substringBefore("}")
        .split(",")
        .map(::Rule)
    return Workflow(name, rules)
}

private data class Rule(
    val field: Char?,
    val range: LongRange,
    val destination: String
) {
    fun test(part: Part): Boolean {
        return when (field) {
            null -> true
            'x' -> part.x in range
            'm' -> part.m in range
            'a' -> part.a in range
            's' -> part.s in range
            else -> error("Bad part")
        }
    }
}

private enum class Compare {
    LESS, MORE
}

private fun Rule(s: String): Rule {
    val field = if (s.contains(":")) s.first() else null
    val range = if (s.contains(":")) {
        val compare = when (s[1]) {
            '<' -> Compare.LESS
            '>' -> Compare.MORE
            else -> error("Bad symbol")
        }
        val target = s.drop(2).substringBefore(":").toLong()
        when (s.first()) {
            'x' -> if (compare == Compare.LESS) MIN..<target else target..MAX
            'm' -> if (compare == Compare.LESS) MIN..<target else target..MAX
            'a' -> if (compare == Compare.LESS) MIN..<target else target..MAX
            's' -> if (compare == Compare.LESS) MIN..<target else target..MAX
            else -> error("Bad field")
        }
    } else MIN..MAX

    val destination = s.substringAfter(":")
    return Rule(field, range, destination)
}

private data class Part(val x: Long, val m: Long, val a: Long, val s: Long) {

    fun sum(): Long = x + m + a + s
}

private fun Part(line: String): Part = line.split(",")
    .map { it.filter(Char::isDigit) }
    .map(String::toLong)
    .let { (x, m, a, s) -> Part(x, m, a, s) }
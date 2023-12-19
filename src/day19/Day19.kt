package day19

import println
import readInput

private const val MIN = 1L
private const val MAX = 4000L

val validValues = List(MAX.toInt()) { it + 1L }.toSet()

fun main() {
    fun part1(input: List<String>): Long {
        return PartFactory(input).runParts()
    }

    fun part2(input: List<String>): Long {
        return PartFactory(input).findCombos()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput).also(::println) == 19114L)
    val testInput2 = readInput("Day19_test")
    check(part2(testInput2).also(::println) == 167409079868000L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}

private data class PartFactory(val workflows: List<Workflow>, val parts: List<Part>) {
    val workflowMap = workflows.associateBy { it.name }
        .toMutableMap()
        .apply {
            this["A"] = Workflow("A{A}")
            this["R"] = Workflow("R{R}")
        }

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

    fun findCombos(
        workflow: Workflow = workflowMap.getValue("in"),
        workflowValues: WorkflowValues = WorkflowValues()
    ): Long {
        var values = workflowValues
        var sum = 0L
        for (rule in workflow.rules) {
            if (workflow.name == "R") return 0
            if (workflow.name == "A") return values.combos()
            val (included, excluded) = rule.updateValidValues(values)
            values = excluded
            sum += findCombos(workflowMap.getValue(rule.destination), included)
        }
        return sum
    }
}

private fun PartFactory(input: List<String>): PartFactory {
    val split = input.indexOfFirst(String::isEmpty)
    val workflows = input.subList(0, split).map(::Workflow)
    val parts = input.subList(split + 1, input.size).map(::Part)
    return PartFactory(workflows, parts)
}

private data class WorkflowValues(
    val xValues: Set<Long> = validValues,
    val mValues: Set<Long> = validValues,
    val aValues: Set<Long> = validValues,
    val sValues: Set<Long> = validValues
) {
    fun combos(): Long = xValues.size.toLong() * mValues.size * aValues.size * sValues.size

    override fun toString(): String = "xValues: ${xValues.size}, " +
            "mValues: ${mValues.size}, " +
            "aValues: ${aValues.size}, " +
            "sValues: ${sValues.size}"
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
    fun test(part: Part): Boolean = when (field) {
        null -> true
        'x' -> part.x in range
        'm' -> part.m in range
        'a' -> part.a in range
        's' -> part.s in range
        else -> error("Bad part")
    }

    fun updateValidValues(workflowValues: WorkflowValues): Pair<WorkflowValues, WorkflowValues> = when (field) {
        null -> workflowValues to WorkflowValues(emptySet(), emptySet(), emptySet(), emptySet())
        'x' -> workflowValues.copy(xValues = workflowValues.xValues.intersect(range)) to
                workflowValues.copy(xValues = workflowValues.xValues.filterNot { it in range }.toSet())

        'm' -> workflowValues.copy(mValues = workflowValues.mValues.intersect(range)) to
                workflowValues.copy(mValues = workflowValues.mValues.filterNot { it in range }.toSet())

        'a' -> workflowValues.copy(aValues = workflowValues.aValues.intersect(range)) to
                workflowValues.copy(aValues = workflowValues.aValues.filterNot { it in range }.toSet())

        's' -> workflowValues.copy(sValues = workflowValues.sValues.intersect(range)) to
                workflowValues.copy(sValues = workflowValues.sValues.filterNot { it in range }.toSet())

        else -> error("Bad part")
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
            'x' -> if (compare == Compare.LESS) MIN..<target else (target + 1)..MAX
            'm' -> if (compare == Compare.LESS) MIN..<target else (target + 1)..MAX
            'a' -> if (compare == Compare.LESS) MIN..<target else (target + 1)..MAX
            's' -> if (compare == Compare.LESS) MIN..<target else (target + 1)..MAX
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
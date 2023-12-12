fun main() {
    fun part1(input: List<String>): Int {
        return input.map(::Line)
            .sumOf(Line::possibleMatchCount)
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)
    val testInput2 = readInput("Day12_test")
    check(part2(testInput2) == 0L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

data class Line(val rawSprings: String, val damagedList: List<Int>) {
    private val regex = damagedList.springRegex()
    fun possibleMatchCount(): Int {
        return rawSprings.replaceUnknowns()
            .count { it.matches(regex) }
    }
}

fun Line(line: String): Line {
    val (rawSprings, list) = line.split(" ")
    val damagedList = list.split(",").map { it.toInt() }
    return Line(rawSprings, damagedList)
}

fun expandedLine(line: String): Line {
    val (unexpandedSprings, list) = line.split(" ")
    val expandedSprings = List(5) { unexpandedSprings }.joinToString("?")
    val unexpandedList = List(5) { list }.joinToString(",")
    val expandedList = unexpandedList.split(",").map { it.toInt() }
    return Line(expandedSprings, expandedList)
}

fun List<Int>.springRegex(): Regex = joinToString(
    separator = "\\.+",
    prefix = "\\.*",
    postfix = "\\.*"
) { numSprings -> "#{$numSprings}" }.toRegex()

fun String.replaceUnknowns(): List<String> {
    return if (!this.contains("?")) listOf(this)
    else listOf(
        this.replaceFirst("?", "."),
        this.replaceFirst("?", "#")
    )
        .flatMap { it.replaceUnknowns() }
}
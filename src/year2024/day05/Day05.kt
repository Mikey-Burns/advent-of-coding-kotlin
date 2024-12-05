package year2024.day05

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = Manual(input).validPages().sumOf(Page::middlePage).toLong()

    fun part2(input: List<String>): Long =
        Manual(input).invalidPages().map(Page::fixPage).sumOf(Page::middlePage).toLong()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test", "2024")
    check(part1(testInput) == 143L)
    val testInput2 = readInput("Day05_test", "2024")
    check(part2(testInput2) == 123L)

    val input = readInput("Day05", "2024")
    part1(input).println()
    part2(input).println()
}

private fun Manual(input: List<String>): Manual {
    val rules = input.takeWhile { it.isNotBlank() }
        .map { line -> line.substringBefore('|').toInt() to line.substringAfter('|').toInt() }
    val pages = input.takeLastWhile { it.isNotBlank() }.map { pageNumbers -> Page(pageNumbers, rules) }
    return Manual(rules, pages)
}

private data class Manual(private val rules: List<Pair<Int, Int>>, private val pages: List<Page>) {
    fun validPages(): List<Page> = pages.filter { page -> page.isValid() }

    fun invalidPages(): List<Page> = pages.filterNot { page -> page.isValid() }
}

private fun Page(line: String, rules: List<Pair<Int, Int>>): Page =
    line.split(',').map(String::toInt).let { pageNumbers -> Page(pageNumbers, rules) }

private data class Page(val pageNumbers: List<Int>, val rules: List<Pair<Int, Int>>) {
    fun middlePage(): Int = pageNumbers[pageNumbers.lastIndex / 2]

    fun isValid(): Boolean {
        return pageNumbers.withIndex()
            .none { (index, pageNumber) ->
                pageNumbers.drop(index)
                    .map { numberAfter -> numberAfter to pageNumber }
                    .any { rules.contains(it) }
            }
    }

    fun fixPage(): Page {
        val (after, before) = pageNumbers.withIndex()
            .firstNotNullOf { (index, pageNumber) ->
                pageNumbers.drop(index)
                    .map { numberAfter -> numberAfter to pageNumber }
                    .firstOrNull { rules.contains(it) }
            }
        val afterIndex = pageNumbers.indexOf(after)
        val beforeIndex = pageNumbers.indexOf(before)
        val updatedList = pageNumbers.toMutableList()
            .apply {
                this[afterIndex] = before
                this[beforeIndex] = after
            }
        val page = Page(updatedList, rules)
        return if (page.isValid()) page else page.fixPage()
    }
}
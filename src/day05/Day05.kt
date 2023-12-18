package day05

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        val seeds: List<Long> = input[0].split(" ").mapNotNull { it.toLongOrNull() }
        return makeAlmanac(input)
            .getLocations(seeds)
            .min()
    }

    fun part2(input: List<String>): Long {
        val almanac = makeAlmanac(input)
        var min = Long.MAX_VALUE
        var minSeed = 0L
        input[0].split(" ")
            .mapNotNull { it.toLongOrNull() }
            .windowed(2, 2)
            .also { it.println() }
            .forEach { (start, length) ->
                LongRange(start, start + length - 1)
                    .also(::println)
                    .forEach { seed ->
                        val location = almanac.getLocation(seed)
                        if (location < min) {
                            min = location
                            minSeed = seed
                        }
                    }
                println("Seed: $minSeed, Location: $min" )
            }
        return min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    val testInput2 = readInput("Day05_test")
    check(part2(testInput2) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

fun makeAlmanac(input: List<String>): Almanac {
    val sectionStartIndexes = (input.withIndex()
        .filter { (_, line) -> line.contains(":") }
        .map(IndexedValue<String>::index)
            ) + (input.size + 1)
    val categories = IntRange(1, 7)
        .map { input.subList(sectionStartIndexes[it], sectionStartIndexes[it + 1] - 1) }
        .map { section -> Category(section.drop(1)) }
    return Almanac(categories)
}

data class Almanac(val categories: List<Category>) {

    fun getLocations(seeds: List<Long>): List<Long> {
        return seeds.map(::getLocation)
    }

    fun getLocation(seed: Long) = categories.fold(seed) { acc, category ->
        category.transform(acc)
    }
}

data class Category(val transformations: List<Transformation>) {

    fun transform(input: Long): Long {
        val offset = transformations.firstOrNull { input in it.source }?.offset ?: 0
        return input + offset
    }
}

fun Category(lines: List<String>): Category = Category(lines.map(::Transformation))

data class Transformation(val source: LongRange, val offset: Long)

fun Transformation(line: String): Transformation {
    val numbers = line.split(" ").map(String::toLong)
    val source = numbers[1]..<(numbers[1] + numbers[2])
    val offset = numbers[0] - numbers[1]
    return Transformation(source, offset)
        .also { println("$it: $line") }
}
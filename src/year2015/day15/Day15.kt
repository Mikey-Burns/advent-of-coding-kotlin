package year2015.day15

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long = input.map { Ingredient.of(it) }
        .findMaxScore { list -> list.sumOf { (_, quantity) -> quantity } == 100 }

    fun part2(input: List<String>): Long = input.map { Ingredient.of(it) }
        .findMaxScore { list ->
            list.sumOf { (_, quantity) -> quantity } == 100
                    && list.sumOf { (ingredient, quantity) -> ingredient.calories * quantity } == 500
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test", "2015")
    check(part1(testInput) == 62842880L)
    val testInput2 = readInput("Day15_test", "2015")
    check(part2(testInput2) == 57600000L)

    val input = readInput("Day15", "2015")
    part1(input).println()
    part2(input).println()
}

private fun List<Ingredient>.findMaxScore(filter: (List<Pair<Ingredient, Int>>) -> Boolean): Long {
    fun makeComboAndGetScore(quantities: List<Int>): Long {
        if (quantities.size == this.size) {
            return this.zip(quantities).filter(filter).score()
        }
        return (0..(100 - quantities.sum()))
            .maxOf { quantity -> makeComboAndGetScore(quantities + quantity) }
    }

    return makeComboAndGetScore(emptyList())
}

private fun List<Pair<Ingredient, Int>>.filter(filter: (List<Pair<Ingredient, Int>>) -> Boolean): List<Pair<Ingredient, Int>> =
    if (filter(this)) this else emptyList()

private fun List<Pair<Ingredient, Int>>.score(): Long {
    val capacity = this.sumOf { (ingredient, quantity) -> ingredient.capacity * quantity }.coerceAtLeast(0).toLong()
    val durability = this.sumOf { (ingredient, quantity) -> ingredient.durability * quantity }.coerceAtLeast(0).toLong()
    val flavor = this.sumOf { (ingredient, quantity) -> ingredient.flavor * quantity }.coerceAtLeast(0).toLong()
    val texture = this.sumOf { (ingredient, quantity) -> ingredient.texture * quantity }.coerceAtLeast(0).toLong()

    return capacity * durability * flavor * texture
}

private data class Ingredient(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int
) {

    companion object {
        fun of(line: String): Ingredient {
            val name = line.substringBefore(":")
            val capacity = line.substringAfter("capacity ").substringBefore(",").toInt()
            val durability = line.substringAfter("durability ").substringBefore(",").toInt()
            val flavor = line.substringAfter("flavor ").substringBefore(",").toInt()
            val texture = line.substringAfter("texture ").substringBefore(",").toInt()
            val calories = line.substringAfter("calories ").toInt()
            return Ingredient(name, capacity, durability, flavor, texture, calories)
        }
    }
}
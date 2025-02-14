package year2016.day21

import utils.println
import utils.readInput
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>, start: String = "abcdefgh"): String = scramble(input, start)

    fun part2(input: List<String>, scrambled: String = "fbgdceah"): String = reverseScramble(input, scrambled)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test", "2016")
    check(part1(testInput, "abcde") == "decab")

    val input = readInput("Day21", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun scramble(input: List<String>, start: String): String {
    val scrambled = start.toMutableList()
    for (instruction in input) {
        val split = instruction.split(" ")
        when {
            instruction.startsWith("swap position") -> scrambled.swap(split[2].toInt(), split[5].toInt())
            instruction.startsWith("swap letter") -> scrambled.swap(split[2][0], split[5][0])
            instruction.startsWith("rotate left") -> scrambled.rotateLeft(split[2].toInt())
            instruction.startsWith("rotate right") -> scrambled.rotateRight(split[2].toInt())
            instruction.startsWith("rotate based on") -> scrambled.rotateRightAround(split[6][0])
            instruction.startsWith("reverse") -> scrambled.reverse(split[2].toInt(), split[4].toInt())
            instruction.startsWith("move") -> scrambled.move(split[2].toInt(), split[5].toInt())
        }
    }
    return scrambled.joinToString("")
}

private fun reverseScramble(input: List<String>, scrambled: String): String {
    val toUnscramble = scrambled.toMutableList()

    for (instruction in input.reversed()) {
        val split = instruction.split(" ")
        when {
            instruction.startsWith("swap position") -> toUnscramble.swap(split[2].toInt(), split[5].toInt())
            instruction.startsWith("swap letter") -> toUnscramble.swap(split[2][0], split[5][0])
            instruction.startsWith("rotate left") -> toUnscramble.rotateRight(split[2].toInt())
            instruction.startsWith("rotate right") -> toUnscramble.rotateLeft(split[2].toInt())
            instruction.startsWith("rotate based on") -> toUnscramble.rotateLeftAround(split[6][0])
            instruction.startsWith("reverse") -> toUnscramble.reverse(split[2].toInt(), split[4].toInt())
            instruction.startsWith("move") -> toUnscramble.move(split[2].toInt(), split[5].toInt())
        }
    }
    return toUnscramble.joinToString("")
}

@Suppress("unused")
private fun hacker(input: List<String>, scrambled: String): String {
    val attemptedPasswords = mutableSetOf<String>()
    while (true) {
        val password = scrambled.toList().shuffled().joinToString("")
        if (password !in attemptedPasswords) {
            val result = scramble(input, password)
            if (result == scrambled) return password
            attemptedPasswords.add(password)
        }
    }
}

private fun MutableList<Char>.swap(first: Int, second: Int): MutableList<Char> = apply {
    val letter = this[first]
    this[first] = this[second]
    this[second] = letter
}

private fun MutableList<Char>.swap(first: Char, second: Char): MutableList<Char> = apply {
    val firstIndex = this.indexOf(first)
    val secondIndex = this.indexOf(second)

    this[firstIndex] = second
    this[secondIndex] = first
}

private fun MutableList<Char>.rotateLeft(steps: Int): MutableList<Char> = apply {
    repeat(steps) {
        this.add(this.removeFirst())
    }
}

private fun MutableList<Char>.rotateRight(steps: Int): MutableList<Char> = apply {
    repeat(steps) {
        this.addFirst(this.removeLast())
    }
}

private fun MutableList<Char>.rotateRightAround(letter: Char): MutableList<Char> = apply {
    val index = this.indexOf(letter)
    val rotations = (if (index >= 4) index + 2 else index + 1) % size
    rotateRight(rotations)
}

val newToOld = mapOf(
    1 to 0,
    3 to 1,
    5 to 2,
    7 to 3,
    10 % 8 to 4,
    12 % 8 to 5,
    14 % 8 to 6,
    16 % 8 to 7,
)

private fun MutableList<Char>.rotateLeftAround(letter: Char): MutableList<Char> = apply {
    val index = indexOf(letter)
    val originalIndex = newToOld.getValue(index)
    while (this[originalIndex] != letter) {
        this.add(this.removeFirst())
    }
}

private fun MutableList<Char>.reverse(first: Int, second: Int): MutableList<Char> = apply {
    subList(first, second + 1)
        .reversed()
        .forEachIndexed { offset, letter ->
            this[first + offset] = letter
        }
}

private fun MutableList<Char>.move(first: Int, second: Int): MutableList<Char> = apply {
    add(second, removeAt(first))
}
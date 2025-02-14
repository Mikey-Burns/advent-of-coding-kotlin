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
            instruction.startsWith("swap position") -> {
                val firstIndex = split[2].toInt()
                val secondIndex = split[5].toInt()
                val first = scrambled[firstIndex]

                scrambled[firstIndex] = scrambled[secondIndex]
                scrambled[secondIndex] = first
            }

            instruction.startsWith("swap letter") -> {
                val first = split[2].first()
                val second = split[5].first()

                scrambled.indices.forEach { index ->
                    scrambled[index] = when (scrambled[index]) {
                        first -> second
                        second -> first
                        else -> scrambled[index]
                    }
                }
            }

            instruction.startsWith("rotate left") -> {
                val steps = split[2].toInt()
                val toMove = scrambled.take(steps)
                repeat(steps) {
                    scrambled.removeFirst()
                }
                scrambled.addAll(toMove)
            }

            instruction.startsWith("rotate right") -> {
                val steps = split[2].toInt()
                val toMove = scrambled.takeLast(steps)
                repeat(steps) {
                    scrambled.removeLast()
                }
                scrambled.addAll(0, toMove)
            }

            instruction.startsWith("rotate based on") -> {
                val letter = split[6].first()
                val index = scrambled.indexOf(letter)
                val rotations = (if (index >= 4) index + 2 else index + 1) % scrambled.size
                val toMove = scrambled.takeLast(rotations)
                repeat(rotations) {
                    scrambled.removeLast()
                }
                scrambled.addAll(0, toMove)
            }

            instruction.startsWith("reverse") -> {
                val first = split[2].toInt()
                val second = split[4].toInt()
                val sublist = scrambled.subList(first, second + 1).reversed()
                sublist.forEachIndexed { offset, letter ->
                    scrambled[first + offset] = letter
                }
            }

            instruction.startsWith("move") -> {
                val first = split[2].toInt()
                val second = split[5].toInt()
                val letter = scrambled.removeAt(first)
                scrambled.add(second, letter)
            }
        }
    }
    return scrambled.joinToString("")
}

private fun reverseScramble(input: List<String>, scrambled: String): String {
    val toUnscramble = scrambled.toMutableList()
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

    for (instruction in input.reversed()) {
        val split = instruction.split(" ")
        when {
            instruction.startsWith("swap position") -> {
                val firstIndex = split[2].toInt()
                val secondIndex = split[5].toInt()
                val first = toUnscramble[firstIndex]

                toUnscramble[firstIndex] = toUnscramble[secondIndex]
                toUnscramble[secondIndex] = first
            }

            instruction.startsWith("swap letter") -> {
                val first = split[2].first()
                val second = split[5].first()

                toUnscramble.indices.forEach { index ->
                    toUnscramble[index] = when (toUnscramble[index]) {
                        first -> second
                        second -> first
                        else -> toUnscramble[index]
                    }
                }
            }

            instruction.startsWith("rotate left") -> {
                val steps = split[2].toInt()
                val toMove = toUnscramble.takeLast(steps)
                repeat(steps) {
                    toUnscramble.removeLast()
                }
                toUnscramble.addAll(0, toMove)
            }

            instruction.startsWith("rotate right") -> {
                val steps = split[2].toInt()
                val toMove = toUnscramble.take(steps)
                repeat(steps) {
                    toUnscramble.removeFirst()
                }
                toUnscramble.addAll(toMove)
            }

            instruction.startsWith("rotate based on") -> {
                val letter = split[6].first()
                val index = toUnscramble.indexOf(letter)
                val originalIndex = newToOld.getValue(index)
                while (toUnscramble[originalIndex] != letter) {
                    toUnscramble.add(toUnscramble.removeFirst())
                }
            }

            instruction.startsWith("reverse") -> {
                val first = split[2].toInt()
                val second = split[4].toInt()
                val sublist = toUnscramble.subList(first, second + 1).reversed()
                sublist.forEachIndexed { offset, letter ->
                    toUnscramble[first + offset] = letter
                }
            }

            instruction.startsWith("move") -> {
                val first = split[5].toInt()
                val second = split[2].toInt()
                val letter = toUnscramble.removeAt(first)
                toUnscramble.add(second, letter)
            }
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
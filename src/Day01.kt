import java.lang.StringBuilder

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line -> "${line.first(Char::isDigit)}${line.last(Char::isDigit)}".toInt() }
    }

    fun part2(input: List<String>): Int {
        return input.map(::transformToDigits)
            .sumOf { line -> "${line.first(Char::isDigit)}${line.last(Char::isDigit)}".toInt() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 293)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

private val numberPairs = listOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

fun transformToDigits(line: String): String = buildString {
    for (index in line.indices) {
        if (line[index].isDigit()) append(line[index])
        for ((number, digit) in numberPairs) {
            if (line.substring(index).startsWith(number)) append(digit)
        }
    }
}
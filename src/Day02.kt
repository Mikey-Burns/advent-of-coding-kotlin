fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { validGameValue(it) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { powerOfGame(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day02_test")
    check(part2(testInput2) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

fun validGameValue(input: String): Int {
    val idSplit = input.split(":")
    val id = idSplit[0].split(" ")[1]
    val (red, green, blue) = findMinimumNumberOfCubes(idSplit[1])
    return if (red <= 12 && green <= 13 && blue <= 14) id.toInt() else 0
}

fun powerOfGame(input: String): Int {
    val idSplit = input.split(":")
    val (red, green, blue) = findMinimumNumberOfCubes(idSplit[1])
    return red * green * blue
}

private fun findMinimumNumberOfCubes(cubeString: String): Triple<Int, Int, Int> {
    val splitContainingRounds = cubeString.split(";")
    val red = splitContainingRounds.maxOf { round ->
        val splitContainingColors = round.replace(",", "").split(" ")
        val redIndex = splitContainingColors.indexOf("red")
        val red = if (redIndex != -1) splitContainingColors[redIndex - 1].toInt() else 0
        red
    }
    val green = splitContainingRounds.maxOf { round ->
        val splitContainingColors = round.replace(",", "").split(" ")
        val greenIndex = splitContainingColors.indexOf("green")
        val green = if (greenIndex != -1) splitContainingColors[greenIndex - 1].toInt() else 0
        green
    }
    val blue = splitContainingRounds.maxOf { round ->
        val splitContainingColors = round.replace(",", "").split(" ")
        val blueIndex = splitContainingColors.indexOf("blue")
        val blue = if (blueIndex != -1) splitContainingColors[blueIndex - 1].toInt() else 0
        blue
    }
    return Triple(red, green, blue)
}

data class Game(val id: Int, val maxRed: Int, val maxGreen: Int, val maxBlue: Int)
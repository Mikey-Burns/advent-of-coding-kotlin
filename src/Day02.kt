fun main() {
    fun part1(input: List<String>): Int {
        return input.map(::stringToGame)
            .filter(Game::isValid)
            .sumOf(Game::id)
    }

    fun part2(input: List<String>): Int {
        return input.map(::stringToGame)
            .map(Game::maxCubesUsed)
            .sumOf { it.red * it.green * it.blue }
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

fun stringToGame(input: String): Game {
    val idSplit = input.split(":")
    val id = idSplit[0].split(" ")[1].toInt()
    val rounds = idSplit[1].split(";")
        .map { round -> round.replace(",", "")}
        .map { round ->
            val roundSplit = round.split(" ")
            listOf("red", "green", "blue")
                .map { color ->
                    val index = roundSplit.indexOf(color)
                    if (index != -1) roundSplit[index - 1].toInt() else 0
                }
                .let { Round(it[0], it[1], it[2]) }
        }
    return Game(id, rounds)
}

data class Game(val id: Int, val rounds: List<Round>) {

    fun isValid(): Boolean = rounds.all(Round::isValid)

    fun maxCubesUsed(): Round = rounds.reduce { r1, r2 -> Round(
        maxOf(r1.red, r2.red),
        maxOf(r1.green, r2.green),
        maxOf(r1.blue, r2.blue)
    )}
}

data class Round(val red: Int, val green: Int, val blue: Int) {

    fun isValid(): Boolean = red <= 12 && green <= 13 && blue <= 14
}
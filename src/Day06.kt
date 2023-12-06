fun main() {
    fun part1(input: List<String>): Int {
        return parseRaces(input)
            .map { race -> race.numberOfWinners() }
            .reduce { acc, winners -> acc * winners }
    }

    fun part2(input: List<String>): Int {
        return parseRace(input)
            .numberOfWinners()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    val testInput2 = readInput("Day06_test")
    check(part2(testInput2) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

data class Race(val time: Long, val distance: Long) {

    fun calculateDistance(holdTime: Long): Long = holdTime * (time - holdTime)

    fun calculateAllTimes(): List<Long> = (1..<time).map(::calculateDistance)

    fun numberOfWinners(): Int = calculateAllTimes().count { it > distance }
}

fun parseRaces(input: List<String>): List<Race> {
    val times = input.first().split(" ").mapNotNull(String::toLongOrNull)
    val distances = input.last().split(" ").mapNotNull(String::toLongOrNull)
    return times.zip(distances).map { (time, distance) ->
        Race(time, distance)
            .also(::println)
    }
}

fun parseRace(input: List<String>): Race {
    val time = input.first().filter(Char::isDigit).toLong()
    val distance = input.last().filter(Char::isDigit).toLong()
    return Race(time, distance)
}
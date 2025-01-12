package year2015.day13

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = SeatingArrangements.of(input).findHappiestArrangement()

    fun part2(input: List<String>): Int = SeatingArrangements.withMe(input).findHappiestArrangement()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test", "2015")
    check(part1(testInput) == 330)

    val input = readInput("Day13", "2015")
    part1(input).println()
    part2(input).println()
}

private data class SeatingArrangements(
    val seatingPairs: Map<String, List<String>>,
    val happinessMap: Map<Pair<String, String>, Int>
) {
    private val people = seatingPairs.keys.toSet()

    private fun findArrangements(): MutableList<List<String>> {
        val fullArrangements = mutableListOf<List<String>>()

        fun dfsForArrangement(seats: List<String>) {
            if (seats.size == people.size) {
                fullArrangements.add(seats + people.first())
                return
            }
            for (nextSeat in seatingPairs.getValue(seats.last())) {
                if (nextSeat !in seats) dfsForArrangement(seats + nextSeat)
            }
        }

        dfsForArrangement(listOf(people.first()))

        return fullArrangements
    }

    fun findHappiestArrangement(): Int = findArrangements()
        .maxOf { seats ->
            seats.zipWithNext()
                .sumOf { (first, second) ->
                    happinessMap.getValue(first to second) + happinessMap.getValue(second to first)
                }
        }

    companion object {
        fun of(input: List<String>): SeatingArrangements {
            val seatingPairs = mutableMapOf<String, List<String>>()
            val happinessMap = mutableMapOf<Pair<String, String>, Int>()
            input.forEach { line ->
                val first = line.substringBefore(" ")
                val second = line.substringAfterLast(" ").substringBefore(".")
                val isGain = line.contains("gain")
                val happiness = line.substringBefore(" happiness").substringAfterLast(" ").toInt()
                seatingPairs[first] = (seatingPairs[first] ?: emptyList()) + second
                happinessMap[first to second] = if (isGain) happiness else happiness * -1
            }

            return SeatingArrangements(seatingPairs, happinessMap)
        }

        fun withMe(input: List<String>): SeatingArrangements {
            with(of(input)) {
                val mySeatingPairs = seatingPairs.toMutableMap()
                val myHappinessMap = happinessMap.toMutableMap()
                people.forEach { person ->
                    mySeatingPairs[person] = mySeatingPairs.getValue(person) + "me"
                    mySeatingPairs["me"] = (mySeatingPairs["me"] ?: emptyList()) + person
                    myHappinessMap[person to "me"] = 0
                    myHappinessMap["me" to person] = 0
                }
                return SeatingArrangements(mySeatingPairs, myHappinessMap)
            }

        }
    }
}
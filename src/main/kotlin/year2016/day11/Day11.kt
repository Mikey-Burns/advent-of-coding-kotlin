package year2016.day11

import utils.println
import utils.readInput
import utils.uniquePairs
import year2016.day11.Science.Generator
import year2016.day11.Science.Microchip
import java.util.*
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = Facility.of(input).minimumStepsToFourthFloor()

    fun part2(input: List<String>): Int = Facility.withEleriumAndDilithium(input).minimumStepsToFourthFloor()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test", "2016")
    check(part1(testInput) == 11)

    val input = readInput("Day11", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private data class Facility(
    val elevatorFloor: Int = 1,
    val firstFloor: Set<Science>,
    val secondFloor: Set<Science>,
    val thirdFloor: Set<Science>,
    val fourthFloor: Set<Science>
) {

    fun minimumStepsToFourthFloor(): Int {
        val visited = mutableSetOf<Facility>()
        val queue =
            PriorityQueue<Pair<Int, Facility>>(compareBy<Pair<Int, Facility>> { (steps, _) -> steps })
                .apply { add(0 to this@Facility) }
        while (queue.isNotEmpty()) {
            val (steps, facility) = queue.poll()
            visited.add(facility)

            // Check for exit
            with(facility) {
                if (elevatorFloor == 4 && (firstFloor + secondFloor + thirdFloor).isEmpty()) {
                    return steps
                }
            }
            // Or check the next moves
            facility.validMoves()
                .filter { it !in visited }
                .also(visited::addAll)
                .map { (steps + 1) to it }
                .also(queue::addAll)
        }
        error("No valid solution")
    }

    private fun validMoves(): List<Facility> = when (elevatorFloor) {
        1 -> (firstFloor.map { listOf(it) } + firstFloor.uniquePairs().map { it.toList() })
            .map { scienceToMove ->
                // Move up a floor
                this.copy(
                    elevatorFloor = 2,
                    firstFloor = firstFloor - scienceToMove,
                    secondFloor = secondFloor + scienceToMove
                )
            }

        2 -> (secondFloor.map { listOf(it) } + secondFloor.uniquePairs().map { it.toList() })
            .flatMap { scienceToMove ->
                listOf(
                    // Move down a floor
                    this.copy(
                        elevatorFloor = 1,
                        firstFloor = firstFloor + scienceToMove,
                        secondFloor = secondFloor - scienceToMove
                    ),
                    // Move up a floor
                    this.copy(
                        elevatorFloor = 3,
                        secondFloor = secondFloor - scienceToMove,
                        thirdFloor = thirdFloor + scienceToMove
                    )
                )
            }

        3 -> (thirdFloor.map { listOf(it) } + thirdFloor.uniquePairs().map { it.toList() })
            .flatMap { scienceToMove ->
                listOf(
                    // Move down a floor
                    this.copy(
                        elevatorFloor = 2,
                        secondFloor = secondFloor + scienceToMove,
                        thirdFloor = thirdFloor - scienceToMove
                    ),
                    // Move up a floor
                    this.copy(
                        elevatorFloor = 4,
                        thirdFloor = thirdFloor - scienceToMove,
                        fourthFloor = fourthFloor + scienceToMove
                    )
                )
            }

        4 -> (fourthFloor.map { listOf(it) } + fourthFloor.uniquePairs().map { it.toList() })
            .map { scienceToMove ->
                // Move down a floor
                this.copy(
                    elevatorFloor = 3,
                    thirdFloor = thirdFloor + scienceToMove,
                    fourthFloor = fourthFloor - scienceToMove
                )
            }

        else -> error("Invalid floor!")
    }
        .filter(Facility::isValid)

    private fun isValid(): Boolean {
        fun Set<Science>.isValid(): Boolean {
            val generators = filterIsInstance<Generator>().map(Science::name)
            val microchips = filterIsInstance<Microchip>().map(Science::name)
            // For every floor, an unmatched microchip cannot be in the presence of another generator
            val unmatchedMicrochips = microchips - generators
            return unmatchedMicrochips.isEmpty() || generators.isEmpty()
        }

        return firstFloor.isValid() && secondFloor.isValid() && thirdFloor.isValid() && fourthFloor.isValid()
    }

    companion object {
        fun of(input: List<String>): Facility {

            fun String.extractMicrochips(): List<Microchip> = this.split(" ")
                .filter { it.contains("-") }
                .map { it.split("-")[0] }
                .map { Microchip(it) }

            fun String.extractGenerators(): List<Generator> = this.split(" ")
                .windowed(2)
                .filter { (_, generator) -> generator.startsWith("generator") }
                .map { (name, _) -> Generator(name) }

            val firstFloorMicrochips = input[0].extractMicrochips()
            val secondFloorMicrochips = input[1].extractMicrochips()
            val thirdFloorMicrochips = input[2].extractMicrochips()
            val fourthFloorMicrochips = input[3].extractMicrochips()

            val firstFloorGenerators = input[0].extractGenerators()
            val secondFloorGenerators = input[1].extractGenerators()
            val thirdFloorGenerators = input[2].extractGenerators()
            val fourthFloorGenerators = input[3].extractGenerators()

            return Facility(
                1,
                (firstFloorMicrochips + firstFloorGenerators).toSet(),
                (secondFloorMicrochips + secondFloorGenerators).toSet(),
                (thirdFloorMicrochips + thirdFloorGenerators).toSet(),
                (fourthFloorMicrochips + fourthFloorGenerators).toSet(),
            )
        }

        fun withEleriumAndDilithium(input: List<String>): Facility {
            val original = of(input)
            return original.copy(
                firstFloor =  original.firstFloor + listOf(
                    Microchip("elerium"),
                    Generator("elerium"),
                    Microchip("dilithium"),
                    Generator("dilithium"),
                )
            )
        }
    }
}

private sealed class Science(open val name: String) {
    data class Microchip(override val name: String) : Science(name) {
        override fun toString(): String = name[0].uppercase() + "M"
    }

    data class Generator(override val name: String) : Science(name) {
        override fun toString(): String = name[0].uppercase() + "G"
    }
}
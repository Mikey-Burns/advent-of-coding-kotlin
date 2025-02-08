package year2016.day17

import utils.*
import java.util.*
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): String = shortestPath(input[0])

    fun part2(input: List<String>): Int = longestPath(input[0])

    // test if implementation meets criteria from the description, like:
    check(shortestPath("ihgpwlah") == "DDRRRD")
    check(shortestPath("kglvqrro") == "DDUDRLRRUDRD")
    check(shortestPath("ulqzkmiv") == "DRURDRUDDLLDLUURRDULRLDUUDDDRR")
    check(longestPath("ihgpwlah") == 370)
    check(longestPath("kglvqrro") == 492)
    check(longestPath("ulqzkmiv") == 830)

    val input = readInput("Day17", "2016")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private fun shortestPath(passcode: String): String = findAllPaths(passcode).minBy(String::length)

private fun longestPath(passcode: String): Int = findAllPaths(passcode).maxOf(String::length)

private fun findAllPaths(passcode: String): List<String> {
    val exit = Location(3, 3)
    val openDoors = "bcdef"
    val paths = mutableListOf<String>()

    val queue = PriorityQueue<Pair<Location, String>>(compareBy<Pair<Location, String>> { (_, steps) -> steps.length })
        .apply { add(Location(0, 0) to "") }

    while (queue.isNotEmpty()) {
        val (location, steps) = queue.poll()

        if (location == exit) {
            paths.add(steps)
        } else {

            val md5 = (passcode + steps).md5().take(4)
            if (openDoors.contains(md5[0]) && location.first > 0) {
                queue.add(location.up() to steps + "U")
            }
            if (openDoors.contains(md5[1]) && location.first < 3) {
                queue.add(location.down() to steps + "D")
            }
            if (openDoors.contains(md5[2]) && location.second > 0) {
                queue.add(location.left() to steps + "L")
            }
            if (openDoors.contains(md5[3]) && location.second < 3) {
                queue.add(location.right() to steps + "R")
            }
        }
    }

    return paths
}
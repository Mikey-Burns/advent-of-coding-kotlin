package year2015.day21

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int {
        val enemy = input.toEnemy()
        return allCharacters()
            .filter { it.canDefeat(enemy) }
            .minOf { it.gold }
    }

    fun part2(input: List<String>): Int {
        val enemy = input.toEnemy()
        return allCharacters()
            .filterNot { it.canDefeat(enemy) }
            .maxOf { it.gold }
    }

    // test if implementation meets criteria from the description, like:
    check(Character(8, 5, 5).canDefeat(Character(12, 7, 2)))
    check(Character(8, 10, 5).canDefeat(Character(12, 7, 2)))
    check(!Character(8, 5, 4).canDefeat(Character(12, 7, 2)))

    val input = readInput("Day21", "2015")
    part1(input).println()
    part2(input).println()
}

private data class Character(val hitPoints: Int, val damage: Int, val armor: Int, val gold: Int = 0)

private fun Character.canDefeat(enemy: Character): Boolean {
    val damageToEnemy = (damage - enemy.armor).coerceAtLeast(1)
    val damageToMe = (enemy.damage - armor).coerceAtLeast(1)
    val turnsToWin = enemy.hitPoints / damageToEnemy + (enemy.hitPoints % damageToEnemy).coerceAtMost(1)
    val turnsToLose = hitPoints / damageToMe + (hitPoints % damageToMe).coerceAtMost(1)
    return turnsToWin <= turnsToLose
}

private fun List<String>.toEnemy(): Character {
    val (hitPoints, damage, armor) = this.map { line -> line.split(" ").last().toInt() }
    return Character(hitPoints, damage, armor)
}

private fun allCharacters(): List<Character> {
    val weapons = listOf(
        8 to 4,
        10 to 5,
        25 to 6,
        40 to 7,
        74 to 8
    )
    val armors = listOf(
        0 to 0,
        13 to 1,
        31 to 2,
        53 to 3,
        75 to 4,
        102 to 5
    )
    val rings = listOf(
        Triple(0, 0, 0),
        Triple(25, 1, 0),
        Triple(50, 2, 0),
        Triple(100, 3, 0),
        Triple(20, 0, 1),
        Triple(40, 0, 2),
        Triple(80, 0, 3),
    )

    val ringCombos = rings.flatMapIndexed { index, firstRing ->
        rings.drop(index + 1)
            .map { secondRing ->
                Triple(
                    firstRing.first + secondRing.first,
                    firstRing.second + secondRing.second,
                    firstRing.third + secondRing.third
                )
            }
    } + Triple(0, 0, 0)

    return weapons.flatMap { (weaponGold, weaponDamage) ->
        armors.flatMap { (armorGold, armorArmor) ->
            ringCombos.map { (ringGold, ringDamage, ringArmor) ->
                Character(
                    100,
                    weaponDamage + ringDamage,
                    armorArmor + ringArmor,
                    weaponGold + armorGold + ringGold
                )
            }
        }
    }
}
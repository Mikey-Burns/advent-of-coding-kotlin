package year2015.day22

import utils.println
import utils.readInput
import year2015.day22.Spell.*
import java.util.*
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int = prioritySearch(Wizard(), input.toEnemy())

    fun part2(input: List<String>): Int = prioritySearch(Wizard(), input.toEnemy(), 1)

    val input = readInput("Day22", "2015")
    measureTime { part1(input).println() }.println()
    measureTime { part2(input).println() }.println()
}

private data class Wizard(val hitPoints: Int = 50, val mana: Int = 500)
private data class Enemy(val hitPoints: Int, val damage: Int)

private fun List<String>.toEnemy(): Enemy {
    val (hitPoints, damage) = this.map { line -> line.split(" ").last().toInt() }
    return Enemy(hitPoints, damage)
}

private val spells = listOf(MAGIC_MISSILE, DRAIN, SHIELD, POISON, RECHARGE)

@Suppress("Unused")
private fun leastManaBattle(wizard: Wizard, enemy: Enemy, difficultyDamage: Int = 0): Int {
    var minimumManaSpent = Int.MAX_VALUE
    var minimumSpellSet = emptyList<Spell>()


    fun battle(
        wizardHp: Int = wizard.hitPoints,
        enemyHp: Int = enemy.hitPoints,
        manaRemaining: Int = wizard.mana,
        manaSpent: Int = 0,
        shieldTurns: Int = 0,
        poisonTurns: Int = 0,
        rechargeTurns: Int = 0,
        spellsCast: List<Spell> = emptyList()
    ) {
        // If we just struck a killing blow
        val effectiveEnemyHp = enemyHp - (if (poisonTurns > 0) 3 else 0)
        if (effectiveEnemyHp <= 0) {
            if (manaSpent < minimumManaSpent) {
                minimumManaSpent = manaSpent
                minimumSpellSet = spellsCast
            }
            return
        }
        // If we're actually dead
        val effectiveHp = wizardHp - difficultyDamage
        if (effectiveHp <= 0) return

        val armor = if (shieldTurns > 0) 7 else 0
        val damageTaken = (enemy.damage - armor).coerceAtLeast(1)
        // This ignores an edge case of poison killing after the boss turn, but they actually kill us first
        val dot = if (poisonTurns > 0) 6 else 0
        val manaGain = when {
            rechargeTurns >= 2 -> 202
            rechargeTurns == 1 -> 101
            else -> 0
        }
        val effectiveMana = manaRemaining + manaGain

        spells.filter { spellsCast.manaCost() < 2000 }
            .filter { spell -> spell.mana <= effectiveMana }
            .filter { spell ->
                when (spell) {
                    SHIELD -> (shieldTurns - 2).coerceAtLeast(0) == 0
                    POISON -> (poisonTurns - 2).coerceAtLeast(0) == 0
                    RECHARGE -> (rechargeTurns - 2).coerceAtLeast(0) == 0
                    else -> true
                }
            }
            .forEach { spell ->

                when (spell) {
                    MAGIC_MISSILE -> battle(
                        effectiveHp - damageTaken,
                        enemyHp - (4 + dot),
                        effectiveMana - spell.mana,
                        manaSpent + spell.mana,
                        (shieldTurns - 2).coerceAtLeast(0),
                        (poisonTurns - 2).coerceAtLeast(0),
                        (rechargeTurns - 2).coerceAtLeast(0),
                        spellsCast + spell
                    )

                    DRAIN -> battle(
                        (effectiveHp + 2) - damageTaken,
                        enemyHp - (2 + dot),
                        manaRemaining - spell.mana + manaGain,
                        manaSpent + spell.mana,
                        (shieldTurns - 2).coerceAtLeast(0),
                        (poisonTurns - 2).coerceAtLeast(0),
                        (rechargeTurns - 2).coerceAtLeast(0),
                        spellsCast + spell
                    )

                    SHIELD -> battle(
                        effectiveHp - damageTaken,
                        enemyHp - dot,
                        manaRemaining - spell.mana + manaGain,
                        manaSpent + spell.mana,
                        6,
                        (poisonTurns - 2).coerceAtLeast(0),
                        (rechargeTurns - 2).coerceAtLeast(0),
                        spellsCast + spell
                    )

                    POISON -> battle(
                        effectiveHp - damageTaken,
                        enemyHp - dot,
                        manaRemaining - spell.mana + manaGain,
                        manaSpent + spell.mana,
                        (shieldTurns - 2).coerceAtLeast(0),
                        6,
                        (rechargeTurns - 2).coerceAtLeast(0),
                        spellsCast + spell
                    )

                    RECHARGE -> battle(
                        effectiveHp - damageTaken,
                        enemyHp - dot,
                        manaRemaining - spell.mana + manaGain,
                        manaSpent + spell.mana,
                        (shieldTurns - 2).coerceAtLeast(0),
                        (poisonTurns - 2).coerceAtLeast(0),
                        5,
                        spellsCast + spell
                    )
                }
            }
    }

    battle()
    println(minimumSpellSet)
    return minimumManaSpent
}

private typealias SpellList = List<Spell>

private fun SpellList.manaCost(): Int = this.sumOf { it.mana }

private enum class Spell(val mana: Int) {
    MAGIC_MISSILE(53),
    DRAIN(73),
    SHIELD(113),
    POISON(173),
    RECHARGE(229)
}

@Suppress("unused")
private fun turnBasedBattle(wizard: Wizard, enemy: Enemy, difficultyDamage: Int = 0): Int {
    var minimumManaSpent = Int.MAX_VALUE
    var minimumSpellSet = emptyList<Spell>()

    fun playerTurn(battleState: BattleState) {
        fun enemyTurn(enemyBattleState: BattleState) {
            // Status effects
            with(enemyBattleState) {
                val currentMana = manaRemaining + (if (rechargeTurns > 0) 101 else 0)
                val effectiveEnemyHp = enemyHp - (if (poisonTurns > 0) 3 else 0)
                // Check for poison kill
                if (effectiveEnemyHp <= 0) {
                    if (spellsCast.manaCost() < minimumManaSpent) {
                        minimumManaSpent = spellsCast.manaCost()
                        minimumSpellSet = spellsCast
                        return
                    }
                }
                // Hit the player
                val damageDealt = (enemy.damage - (if (shieldTurns > 0) 7 else 0)).coerceAtLeast(1)
                return playerTurn(
                    BattleState(
                        wizardHp - damageDealt,
                        effectiveEnemyHp,
                        currentMana,
                        manaSpent,
                        shieldTurns.decrementToZero(),
                        poisonTurns.decrementToZero(),
                        rechargeTurns.decrementToZero(),
                        spellsCast
                    )
                )
            }
        }

        with(battleState) {
            // Difficulty Damage
            val effectiveHp = wizardHp - difficultyDamage
            if (effectiveHp <= 0) return
            // Status effects
            val currentMana = manaRemaining + (if (rechargeTurns > 0) 101 else 0)
            val effectiveEnemyHp = enemyHp - (if (poisonTurns > 0) 3 else 0)
            // Check for poison kill
            if (effectiveEnemyHp <= 0) {
                if (spellsCast.manaCost() < minimumManaSpent) {
                    minimumManaSpent = spellsCast.manaCost()
                    minimumSpellSet = spellsCast
                    return
                }
            }
            // Cast spell
            spells.filter { spellsCast.manaCost() < 2000 }
                .filter { spell -> spell.mana <= currentMana }
                .filter { spell ->
                    when (spell) {
                        SHIELD -> shieldTurns.decrementToZero() == 0
                        POISON -> poisonTurns.decrementToZero() == 0
                        RECHARGE -> rechargeTurns.decrementToZero() == 0
                        else -> true
                    }
                }
                .forEach { spell ->
                    val updatedMana = currentMana - spell.mana
                    val updatedManaSpent = manaSpent + spell.mana
                    val updatedSpells = spellsCast + spell
                    when (spell) {
                        MAGIC_MISSILE -> enemyTurn(
                            BattleState(
                                effectiveHp,
                                effectiveEnemyHp - 4,
                                updatedMana,
                                updatedManaSpent,
                                shieldTurns.decrementToZero(),
                                poisonTurns.decrementToZero(),
                                rechargeTurns.decrementToZero(),
                                updatedSpells
                            )
                        )

                        DRAIN -> enemyTurn(
                            BattleState(
                                effectiveHp + 2,
                                effectiveEnemyHp - 2,
                                updatedMana,
                                updatedManaSpent,
                                shieldTurns.decrementToZero(),
                                poisonTurns.decrementToZero(),
                                rechargeTurns.decrementToZero(),
                                updatedSpells
                            )
                        )

                        SHIELD -> enemyTurn(
                            BattleState(
                                effectiveHp,
                                effectiveEnemyHp,
                                updatedMana,
                                updatedManaSpent,
                                6,
                                poisonTurns.decrementToZero(),
                                rechargeTurns.decrementToZero(),
                                updatedSpells
                            )
                        )

                        POISON -> enemyTurn(
                            BattleState(
                                effectiveHp,
                                effectiveEnemyHp,
                                updatedMana,
                                updatedManaSpent,
                                shieldTurns.decrementToZero(),
                                6,
                                rechargeTurns.decrementToZero(),
                                updatedSpells
                            )
                        )

                        RECHARGE -> enemyTurn(
                            BattleState(
                                effectiveHp,
                                effectiveEnemyHp,
                                updatedMana,
                                updatedManaSpent,
                                shieldTurns.decrementToZero(),
                                poisonTurns.decrementToZero(),
                                5,
                                updatedSpells
                            )
                        )
                    }
                }
        }
    }

    playerTurn(
        BattleState(
            wizard.hitPoints,
            enemy.hitPoints,
            wizard.mana,
            0,
            0,
            0,
            0,
            emptyList()
        )
    )
    println(minimumSpellSet)
    return minimumManaSpent
}

private fun prioritySearch(wizard: Wizard, enemy: Enemy, difficultyDamage: Int = 0): Int {
    val toExplore = PriorityQueue<BattleState>(compareBy<BattleState> { it.manaSpent })
        .apply {
            add(
                BattleState(
                    wizard.hitPoints,
                    enemy.hitPoints,
                    wizard.mana
                )
            )
        }
    while (toExplore.isNotEmpty()) {
        // Player turn
        val current = toExplore.poll()

        // If we just struck a killing blow
        val effectiveEnemyHp = current.enemyHp - (if (current.poisonTurns > 0) 3 else 0)
        if (effectiveEnemyHp <= 0) {
            println(current.spellsCast)
            return current.manaSpent
        }
        // If we're actually dead
        val effectiveHp = current.wizardHp - difficultyDamage
        if (effectiveHp <= 0) continue
        val manaGain = if (current.rechargeTurns.decrementToZero() > 0) 101 else 0
        val effectiveMana = current.manaRemaining + manaGain

        spells
            .filter { spell -> spell.mana <= effectiveMana }
            .filter { spell ->
                when (spell) {
                    // All effects can be cast every 3 turns, so if it isn't one of the two most recent
                    // spells, then we're good
                    SHIELD, POISON, RECHARGE -> spell !in current.spellsCast.takeLast(2)
                    else -> true
                }
            }
            .map { spell ->
                // Helper to hide common attributes
                fun next(
                    wizardHp: Int = effectiveHp,
                    enemyHp: Int = effectiveEnemyHp,
                    manaRemaining: Int = effectiveMana - spell.mana,
                    manaSpent: Int = current.manaSpent + spell.mana,
                    shieldTurns: Int = current.shieldTurns.decrementToZero(),
                    poisonTurns: Int = current.poisonTurns.decrementToZero(),
                    rechargeTurns: Int = current.rechargeTurns.decrementToZero(),
                    spellsCast: List<Spell> = current.spellsCast + spell
                ): BattleState =
                    BattleState(
                        wizardHp,
                        enemyHp,
                        manaRemaining,
                        manaSpent,
                        shieldTurns,
                        poisonTurns,
                        rechargeTurns,
                        spellsCast
                    )

                when (spell) {
                    MAGIC_MISSILE -> next(
                        enemyHp = effectiveEnemyHp - 4
                    )

                    DRAIN -> next(
                        wizardHp = effectiveHp + 2,
                        enemyHp = effectiveEnemyHp - 2
                    )

                    SHIELD -> next(
                        shieldTurns = 6
                    )

                    POISON -> next(
                        poisonTurns = 6
                    )

                    RECHARGE -> next(
                        rechargeTurns = 6,
                    )
                }
            }
            .forEach { beforeBoss ->
                // Boss turn
                with(beforeBoss) {
                    val armor = if (current.shieldTurns > 0) 7 else 0
                    val damageTaken = (enemy.damage - armor).coerceAtLeast(1)
                    val dot = if (poisonTurns > 0) 3 else 0
                    val manaGain = if (rechargeTurns.decrementToZero() > 0) 101 else 0

                    val afterBoss = copy(
                        wizardHp = wizardHp - damageTaken,
                        enemyHp = enemyHp - dot,
                        manaRemaining = manaRemaining + manaGain,
                        shieldTurns = shieldTurns.decrementToZero(),
                        poisonTurns = poisonTurns.decrementToZero(),
                        rechargeTurns = rechargeTurns.decrementToZero(),
                    )
                    // Check if the boss is dead
                    if (afterBoss.enemyHp <= 0) {
                        println(spellsCast)
                        return manaSpent
                    }
                    // Continue if we aren't dead yet
                    if (afterBoss.wizardHp > 0) toExplore.add(afterBoss)
                }
            }
    }
    error("The player never won!")
}

private data class BattleState(
    val wizardHp: Int,
    val enemyHp: Int,
    val manaRemaining: Int,
    val manaSpent: Int = 0,
    val shieldTurns: Int = 0,
    val poisonTurns: Int = 0,
    val rechargeTurns: Int = 0,
    val spellsCast: List<Spell> = emptyList()
)

private fun Int.decrementToZero(): Int = (this - 1).coerceAtLeast(0)
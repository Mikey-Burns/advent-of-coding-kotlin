package day20

import day20.Propagator.Module.*
import day20.Propagator.Module.FlipFlop.FlipFlopState.OFF
import day20.Propagator.Module.FlipFlop.FlipFlopState.ON
import day20.Pulse.HIGH
import day20.Pulse.LOW
import println
import readInput
import java.util.*

fun main() {
    fun part1(input: List<String>): Long {
        val propagator = Propagator(input)
        return List(1000) { propagator.pushTheButton() }
            .reduce { acc, pair -> (acc.first + pair.first) to (acc.second + pair.second) }
            .let { it.first * it.second }
            .also(::println)
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput).also(::println) == 32000000L)
    val testInput2 = readInput("Day20_test")
    check(part2(testInput2).also(::println) == 0L)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}

private enum class Pulse {
    LOW, HIGH
}

private data class PulseWrapper(val pulse: Pulse, val source: String, val destination: String)

private val pulses: Queue<PulseWrapper> = LinkedList()
private fun Queue<PulseWrapper>.add(pulse: Pulse, source: String, destination: String): Boolean =
    this.add(PulseWrapper(pulse, source, destination))

private const val BROADCASTER = "broadcaster"

private data class Propagator(val modules: Map<String, Module>) {


    fun pushTheButton(): Pair<Long, Long> {
        var low = 0L
        var high = 0L
        pulses.add(LOW, "button", BROADCASTER)
        while (pulses.isNotEmpty()) {
            val (pulse, source, target) = pulses.poll()
            if (pulse == LOW) low++ else high++
            modules[target]?.handlePulse(pulse, source)
        }
        return low to high
    }

    sealed class Module(val name: String, val downstream: List<String>) {
        val upstream: MutableList<String> = mutableListOf()

        open fun addUpstreams(upstreams: Collection<String>) = upstream.addAll(upstreams)
        abstract fun handlePulse(pulse: Pulse, source: String)

        class Broadcaster(downstream: List<String>) : Module(BROADCASTER, downstream) {
            override fun handlePulse(pulse: Pulse, source: String) {
                downstream.forEach { pulses.add(pulse, source, it) }
            }

        }

        class FlipFlop(name: String, downstream: List<String>) : Module(name, downstream) {
            enum class FlipFlopState {
                OFF, ON;

                operator fun not() = when (this) {
                    OFF -> ON
                    ON -> OFF
                }
            }

            var state = OFF
            override fun handlePulse(pulse: Pulse, source: String) {
                when (pulse) {
                    LOW -> {
                        when (state) {
                            OFF -> HIGH
                            ON -> LOW
                        }
                            .also { downstream.forEach { target -> pulses.add(it, name, target) } }
                        state = !state
                    }

                    HIGH -> {
                        // Do Nothing
                    }
                }
            }

        }

        class Conjunction(name: String, downstream: List<String>) : Module(name, downstream) {
            val previousPulses: MutableMap<String, Pulse> = mutableMapOf()

            override fun addUpstreams(upstreams: Collection<String>) = super.addUpstreams(upstreams)
                .also { upstreams.forEach { previousPulses[it] = LOW } }

            override fun handlePulse(pulse: Pulse, source: String) {
                previousPulses[source] = pulse
                val outPulse = if (previousPulses.values.all { it == HIGH }) LOW else HIGH
                downstream.forEach { target -> pulses.add(outPulse, name, target) }
            }

        }
    }
}

private fun Propagator(input: List<String>): Propagator {
    val modules = input.map { line ->
        val downstream = line.substringAfter("> ").split(", ")
        when (line.first()) {
            'b' -> Broadcaster(downstream)
            '%' -> FlipFlop(line.drop(1).substringBefore(" "), downstream)
            '&' -> Conjunction(line.drop(1).substringBefore(" "), downstream)
            else -> error("Invalid module type")
        }
    }
    modules.forEach { module ->
        module.addUpstreams(modules.filter { it.downstream.contains(module.name) }.map { it.name })
    }
    return Propagator(modules.associateBy { it.name })
}
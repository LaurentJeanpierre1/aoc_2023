package day20

import Day
import java.io.File

class Day20Manual(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    data class Pulse(val from: Gate, val to: Gate, val value:Boolean)
    private val gates = mutableMapOf<String,Gate>()
    private val forwardConnexions = mutableListOf<Pair<Gate, String>>()
    val queue = ArrayDeque<Pulse>()
    private lateinit var broadcaster : Broadcast
    open inner class Gate(val name: String) {
        private val destinations = mutableListOf<Gate>()
        var lastState : Boolean = false
        fun connect(next : Gate) {
            destinations += next
            next.newInput(this)
        }
        fun send(high: Boolean) {
            lastState = high
            if (isTest) println("  $name -> sends $high to ${destinations.joinToString { it.name }}")
            destinations.forEach { queue.addLast( Pulse(this, it, high) ) }
        }
        open fun newInput(from: Gate) {}
        open fun process(high: Boolean, from: Gate) {
            if (isTest) println("$name processes $high")
        }
    }
    inner class FlipFlop(name: String) : Gate(name) {
        private var state = false
        override fun process(high: Boolean, from: Gate) {
            super.process(high, from)
            if (!high) {
                state = ! state
                send(state)
            }
        }
    }

    inner class Conjunction(name: String) : Gate(name) {
        private var state = mutableMapOf<Gate,Boolean>()
        override fun newInput(from: Gate) {
            state[from] = false
        }

        override fun process(high: Boolean, from: Gate) {
            super.process(high, from)
            state[from] = high
            send (! state.all{ it.value })
        }
    }

    inner class Broadcast(name: String) : Gate(name) {
        override fun process(high: Boolean, from: Gate) {
            super.process(high, from)
            send(high)
        }
    }
    inner class Output(name: String) : Gate(name)

    override fun part1(data: Sequence<String>): Long {
        readGates(data)

        queue.clear()
        var nbHigh = 0L
        var nbLow = 0L
        repeat(1000) {
            queue.addLast(Pulse(broadcaster, broadcaster, false))
            while(queue.isNotEmpty()) {
                val pulse = queue.removeFirst()
                if (pulse.value)
                    nbHigh++
                else
                    nbLow++
                pulse.to.process(pulse.value, pulse.from)
            }
        }
        return nbHigh * nbLow
    }

    private fun readGates(data: Sequence<String>) {
        data.forEach { line ->
            val parts = line.split("->", ",", " ").filter { it.isNotBlank() }
            var name = parts[0]
            val gate = if (name[0] == '%') {
                name = name.drop(1)
                FlipFlop(name)
            } else if (name[0] == '&') {
                name = name.drop(1)
                Conjunction(name)
            } else {
                Broadcast(name).also { this.broadcaster = it }
            }
            gates[name] = gate
            parts.drop(1).forEach {
                forwardConnexions.add(gate to it)
            }
        }
        forwardConnexions.forEach { (from, to) ->
            var gate = gates[to]
            if (gate == null) {
                gate = Output(to)
                gates[to] = gate
            }
            from.connect(gate)
        }
    }

    override fun part2(data: Sequence<String>): Long {
        readGates(data)

        queue.clear()
        var count = 0L
        val writer = File("gates.csv").printWriter()
        writer.print("0,")
        writer.println(gates.map { it.key }.joinToString())
        do {
            count++
            var nbToRx = 0L
            queue.addLast(Pulse(broadcaster, broadcaster, false))
            while(queue.isNotEmpty()) {
                val pulse = queue.removeFirst()
                if (pulse.to.name == "rx") {
                    if (! pulse.value) {
                        nbToRx++
                        println("Low Pulse to rx at $count")
                    }/* else
                        println("High Pulse to rx at $count")*/
                }
                pulse.to.process(pulse.value, pulse.from)
            }
            writer.print("$count,")
            writer.println(gates.map { if (it.value.lastState) "1" else "0" }.joinToString())
            writer.flush()
        } while (nbToRx != 1L)
        return count
    }
}

fun main() {
//    val dayTest = Day20(20, isTest=true)
//    println("Test part1")
//    check(dayTest.runPart1().also { println("-> $it") } == 32000000L)
    //println("Test part2")
    //check(dayTest.runPart2().also { println("-> $it") } == 2286L)

    val day = Day20Manual(20, isTest=false)
//    println("Run part1")
//    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
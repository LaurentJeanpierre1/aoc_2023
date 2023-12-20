package day20

import Day

class Day20SemiManual(fileName: String, isTest: Boolean, private val targetModule: String): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean, targetModule: String) : this (makeFileName(day, isTest), isTest, targetModule)

    data class Pulse(val from: Gate, val to: Gate, val value:Boolean)
    private val gates = mutableMapOf<String,Gate>()
    private val forwardConnexions = mutableListOf<Pair<Gate, String>>()
    val queue = ArrayDeque<Pulse>()
    private lateinit var broadcaster : Broadcast
    open inner class Gate(val name: String) {
        private val destinations = mutableListOf<Gate>()
        fun connect(next : Gate) {
            destinations += next
            next.newInput(this)
        }
        fun send(high: Boolean) {
            if (isTest) println("  $name -> sends $high to ${destinations.joinToString { it.name }}")
            destinations.forEach { queue.addLast( Pulse(this, it, high) ) }
        }
        open fun newInput(from: Gate) {}
        open fun process(high: Boolean, from: Gate) {
            if (isTest) println("$name processes $high")
        }

        open fun removeDestinations(toDrop: MutableSet<Gate>) {
            destinations.removeAll(toDrop)
        }

        override fun toString(): String {
            return name
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

        override fun removeDestinations(toDrop: MutableSet<Gate>) {
            super.removeDestinations(toDrop)
            toDrop.forEach { state.remove(it) }
        }
    }

    inner class Broadcast(name: String) : Gate(name) {
        override fun process(high: Boolean, from: Gate) {
            super.process(high, from)
            send(high)
        }
    }
    inner class Output(name: String) : Gate(name)

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

    override fun part1(data: Sequence<String>): Any {
        TODO("See Day20.kt")
    }

    override fun part2(data: Sequence<String>): Long {
        readGates(data)

        queue.clear()
        var count = 0L
        do {
            count++
            var nbToRx = 0L
            queue.addLast(Pulse(broadcaster, broadcaster, false))
            while(queue.isNotEmpty()) {
                val pulse = queue.removeFirst()
                if (pulse.to.name == targetModule) {
                    if (! pulse.value) {
                        nbToRx++
                        println("Low Pulse to $targetModule at $count")
                    } /*else
                        println("High Pulse to $targetModule at $count")*/
                }
                pulse.to.process(pulse.value, pulse.from)
            }
        } while (nbToRx != 1L)
        return count
    }

}

fun main() {
    for (target in listOf("mp", "fz", "hn", "xf")) {
        //thread(start = true, name = "Thread-$target") {
            val dayBranch = Day20SemiManual(20, isTest = false, target)
            println("Run part2 for $target")
            println("$target : ${dayBranch.runPart2()}")
        //}
    }
    println(" Now compute PPCM of all these... Thanks LibreOffice")
}
package day12

import Day
import java.util.StringJoiner

class Day12(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        return data.sumOf {line->
            val parts = line.split(' ').filter { it.isNotBlank() }
            val unk = parts[0]
            val packets = parts[1].split(',').filter { it.isNotBlank() }.map{it.toInt()}

            //nbArangements(unk, packets, 0, 0, packets.sum()+packets.size-1)
            nbArangementsRec(unk, packets).also { if (isTest) println(it) }
        }
    }
    class Process {
        val packets = mutableListOf<Int>()
        private var inPacket = false
        private var count = 0
        var nbOK = 0
        var nbKO = 0
        fun process(elt: Char, delta : Int) {
            if (elt == '.') {
                nbOK-=delta
                if (inPacket) {
                    packets.add(count)
                    count = 0
                    inPacket = false
                }
            } else {
                nbKO-=delta
                inPacket = true
                count++
            }
        }
        fun copy() : Process {
            val other = Process()
            other.packets.addAll(packets)
            other.inPacket = inPacket
            other.count = count
            other.nbKO = nbKO
            other.nbOK = nbOK
            return other
        }
        fun end(){
            if (inPacket) {
                packets.add(count)
                inPacket = false
                count = 0
            }
        }
    }
    private fun recurse(unk: String, idxUnk: Int, packets: List<Int>, idxPacket: Int,
                        inPacket: Boolean, count: Int, nbOK: Int, nbKO:Int): Long{
        if (idxPacket >= packets.size) return 1L // success if packet list is drained

        var next = idxUnk
        var nextCount = count
        var nextPacket = idxPacket
        var nextInPacket = inPacket

        while (next<unk.length && unk[next] != '?') {
            if (unk[next] == '.') {
                if (nextInPacket) { // end of packet
                    if (nextCount != packets[nextPacket]) return 0L // not compatible with packet list
                    nextCount = 0
                    nextInPacket = false
                    nextPacket++
                }
            } else {
                if (nextPacket >= packets.size) return 0L // no more packet
                nextInPacket = true
                nextCount++
                if (nextCount > packets[nextPacket]) return 0L // packet already too big
            }
            next++
        }
        if (next >= unk.length) { // end of string
            if (nextInPacket)
                if (nextCount != packets[nextPacket++]) return 0L // not compatible with packet list
            return if (nextPacket>=packets.size) 1L // all packets are drained
            else 0L // some packets have not been drained
        }
        // element is '?'
        var sum = 0L
        if (nbOK>0) { // test '.'
            if (nextInPacket) { // end of packet
                if (nextCount == packets[nextPacket]) {// compatible with packet list
                    sum += recurse(unk, next+1, packets, nextPacket + 1, false, 0, nbOK - 1, nbKO)
                } else {
                    // not compatible with next packet, don't keep on this way
                }
            } else { // not in a packet
                sum += recurse(unk, next+1, packets, nextPacket, false, 0, nbOK - 1, nbKO)
            }
        }
        if (nbKO>0) { // test '#'
            if (nextInPacket || nextPacket<packets.size ) // if in packet or can begin a new packet
                sum += recurse(unk, next+1, packets, nextPacket, true, nextCount+1, nbOK, nbKO - 1)
        }
        return sum
    }
    private fun nbArangementsRec(unk: String, packets:List<Int>) : Long {
        var nbKO = packets.sum()
        var nbOK = unk.length - nbKO
        // remove fixed ones
        nbOK -= unk.count { it == '.' }
        nbKO -= unk.count { it == '#' }
        return recurse(unk, 0, packets, 0, false, 0, nbOK, nbKO)
    }
    fun nbArangements(unk: String, packets:List<Int>) : Long{ //Fails even with 20Go RAM in part2
        var procs = mutableListOf(Process().apply {
            nbKO = packets.sum()
            nbOK = unk.length - nbKO
            // remove fixed ones
            nbOK -= unk.count { it=='.' }
            nbKO -= unk.count { it=='#' }
        })
        for (elt in unk) {
            if (elt == '?') {
                val copy = procs.map { it.copy().apply { process('#', 1) } }
                procs.forEach{ it.process('.', 1) }
                procs.addAll(copy)
            } else
                procs.forEach{ it.process(elt, 0) } // fixed ones are already counted
            procs = procs.asSequence().filter { it.nbOK>=0 && it.nbKO>=0 }.filter {
                it.packets.zip(packets).all { (a,b) -> a==b }
            }.toMutableList()
        }
        procs.forEach{ it.end() }
        return procs.count { it.packets == packets }.toLong()
    }
//    fun nbArangements(unk: String, packets:List<Int>, idxUnk : Int, idxPackets: Int, remaining: Int): Long {
//        if (idxUnk >= unk.length)
//    }
//
    override fun part2(data: Sequence<String>): Long {
        return data.sumOf {line->
            val parts = line.split(' ').filter { it.isNotBlank() }
            val unk = parts[0]
            val packets = parts[1].split(',').filter { it.isNotBlank() }.map{it.toInt()}

            val unk2 = StringJoiner("?")
            val packs2 = mutableListOf<Int>()
            repeat(5) {
                unk2.add(unk)
                packs2.addAll(packets)
            }
            nbArangementsRec(unk2.toString(), packs2).also { /*if (isTest)*/ println(it) }
        }
    }
}

fun main() {
    val dayTest = Day12(12, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 21L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 525152L)

    val day = Day12(12, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
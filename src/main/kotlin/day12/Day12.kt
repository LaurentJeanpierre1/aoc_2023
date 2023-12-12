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
            nbArangements(unk, packets).also { if (isTest) println(it) }
        }
    }
    class Process {
        val packets = mutableListOf<Int>()
        var inPacket = false
        var count = 0
        fun process(elt: Char) {
            if (elt == '.') {
                if (inPacket) {
                    packets.add(count)
                    count = 0
                    inPacket = false
                }
            } else {
                inPacket = true
                count++
            }
        }
        fun copy() : Process {
            val other = Process()
            other.packets.addAll(packets)
            other.inPacket = inPacket
            other.count = count
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
    fun nbArangements(unk: String, packets:List<Int>) : Long{
        val procs = mutableListOf(Process())
        for (elt in unk) {
            if (elt == '?') {
                val copy = procs.map { it.copy().apply { process('#') } }
                procs.forEach{ it.process('.') }
                procs.addAll(copy)
            } else
                procs.forEach{ it.process(elt) }
            procs.filter { it->
                it.packets.zip(packets).all { (a,b) -> a==b }
            }
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
            nbArangements(unk2.toString(), packs2).also { if (isTest) println(it) }
        }
    }
}

fun main() {
    val dayTest = Day12(12, isTest=true)
    //println("Test part1")
    //check(dayTest.runPart1().also { println("-> $it") } == 21L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 525152L)

    val day = Day12(12, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
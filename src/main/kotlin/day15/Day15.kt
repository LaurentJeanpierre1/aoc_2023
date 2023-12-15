package day15

import Day

class Day15(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        val sequences = data.first().splitToSequence(',').filter { it.isNotBlank() }

        return sequences.map{hash(it)}.sum()
    }

    fun hash(seq : String) : Long {
        var res = 0
        for (c in seq) {
            res = ((res + c.code) * 17) and 255
        }
        return res.toLong()//.also { if (isTest) println(it) }
    }
    override fun part2(data: Sequence<String>): Long {
        val sequences = data.first().splitToSequence(',').filter { it.isNotBlank() }
        val boxes = Array<LinkedHashMap<String, Int>>(256) { LinkedHashMap() }
        sequences.forEach {instr ->
            val idx = instr.indexOfFirst { it=='=' }
            if (idx != -1) {
                val label = instr.substring(0..<idx)
                val focal = instr.substring(idx+1).toInt()
                val box = hash(label).toInt()
                if (isTest) print("$instr -> +[$label $focal] in $box")
                boxes[box][label] = focal
                if (isTest) println(" ==> ${boxes[box]}")
            } else {
                val label = instr.dropLast(1) // drop '-'
                val box = hash(label).toInt()
                if (isTest) print("$instr -> -[$label ?] in $box")
                boxes[box].remove(label)
                if (isTest) println(" ==> ${boxes[box]}")
            }
        }
        return boxes.mapIndexed { index: Int, box: LinkedHashMap<String, Int> ->
            (1+index) * box.toList().mapIndexed{ rank, focal -> (rank+1L)*focal.second }.sum()
        }.sum()
    }
}

fun main() {
    val dayTest = Day15(15, isTest=true)
    check(dayTest.hash("HASH") == 52L)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 1320L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 145L)

    val day = Day15(15, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
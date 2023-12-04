package day04

import Day
import kotlin.math.pow

class Day04(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        return data
            .map {line->
                val parts = line.split(':','|')
                val winning = parts[1].split(' ').filter { it.isNotBlank() }.map{it.toLong()}.toSet()
                val have = parts[2].split(' ').filter { it.isNotBlank() }.map{it.toLong()}.toSet()
                val isec = winning.intersect(have)
                2.0.pow((isec.size - 1).toDouble()).toLong()
            }.sum()
    }

    override fun part2(data: Sequence<String>): Long {
        val nbCards = IntArray(1000)
        for((idx,line) in data.withIndex()) {
            nbCards[idx]++
            val parts = line.split(':','|')
            val winning = parts[1].split(' ').filter { it.isNotBlank() }.map{it.toLong()}.toSet()
            val have = parts[2].split(' ').filter { it.isNotBlank() }.map{it.toLong()}.toSet()
            val isec = winning.intersect(have).size
            if (isTest) println("Card ${idx+1} (${nbCards[idx]} copies) win $isec cards each")
            for (i in 1..isec)
                nbCards[idx+i] += nbCards[idx]
        }
        return nbCards.sum().toLong()
    }
}

fun main() {
    val dayTest = Day04(4, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 13L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 30L)

    val day = Day04(4, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
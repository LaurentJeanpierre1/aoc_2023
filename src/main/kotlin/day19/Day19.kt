package day19

import Day

class Day19(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        TODO("Not yet implemented")
    }

    override fun part2(data: Sequence<String>): Long {
        TODO("Not yet implemented")
    }
}

fun main() {
    val dayTest = Day19(19, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 8L)
    //println("Test part2")
    //check(dayTest.runPart2().also { println("-> $it") } == 2286L)

    val day = Day19(19, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
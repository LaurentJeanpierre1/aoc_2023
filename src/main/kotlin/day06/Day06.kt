package day06

import Day

class Day06(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        val lines = data.toList()
        val times = lines[0].drop(10)
            .split(' ').filter { it.isNotBlank() }.map { it.toInt() }
        val distances = lines[1].drop(10)
            .split(' ').filter { it.isNotBlank() }.map { it.toInt() }
        var product = 1L
        for (race in times.indices) {
            var max = 0
            for (press in 1..<times[race]) {
                val dist = press * (times[race]-press)
                if (isTest) println("Press $press -> dist $dist")
                if (dist>distances[race]) max++
            }
            product *= max
        }
        return product
    }

    override fun part2(data: Sequence<String>): Long {
        val lines = data.toList()
        val times = lines[0].drop(10).replace(" ","")
            .split(' ').filter { it.isNotBlank() }.map { it.toLong() }
        val distances = lines[1].drop(10).replace(" ","")
            .split(' ').filter { it.isNotBlank() }.map { it.toLong() }
        var product = 1L
        for (race in times.indices) {
            var max = 0
            for (press in 1..<times[race]) {
                val dist = press * (times[race]-press)
                if (isTest) println("Press $press -> dist $dist")
                if (dist>distances[race]) max++
            }
            product *= max
        }
        return product
    }
}

fun main() {
    val dayTest = Day06(6, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 288L)
    //println("Test part2")
    //check(dayTest.runPart2().also { println("-> $it") } == 2286L)

    val day = Day06(6, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
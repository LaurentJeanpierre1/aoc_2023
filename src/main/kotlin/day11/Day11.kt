package day11

import Day

class Day11(day: Int, isTest: Boolean) : Day(day, isTest) {
    override fun part1(data: Sequence<String>): Long {
        TODO("Not yet implemented")
    }

    override fun part2(data: Sequence<String>): Long {
        TODO("Not yet implemented")
    }
}

fun main() {
    val dayTest = Day11(11, isTest=true)
    check(dayTest.runPart1() == 0L)
    //check(dayTest.runPart2() == 0L)

    val day = Day11(11, false)
    //println(day.runPart1())
    //println(day.runPart2())
}
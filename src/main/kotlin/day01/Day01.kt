package day01

import Day
import kotlin.time.measureTime

class Day01(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    override fun part1(data: Sequence<String>): Long {
        val digits = data.map {
            val list = it.toCharArray()
                .filter { it.isDigit() }
            ("" + list.first() + list.last()).toLong()
        }.toList()
        return digits.sum()
    }


    //nineninesixskjkbhx6nineoneightj ---> attention au eigth final
    /*override*/ fun part2_mine(data: Sequence<String>): Long {
        val digits = data
            .map { line ->
                val first = Regex("(one)|(two)|(three)|(four)|(five)|(six)|(seven)|(eight)|(nine)|[0-9]")
                    .find(line)!!
                val d1 = when (first.value) {
                    "one" -> "1"
                    "two" -> "2"
                    "three" -> "3"
                    "four" -> "4"
                    "five" -> "5"
                    "six" -> "6"
                    "seven" -> "7"
                    "eight" -> "8"
                    "nine" -> "9"
                    else -> first.value
                }.first()
                val second = Regex("(eno)|(owt)|(eerht)|(ruof)|(evif)|(xis)|(neves)|(thgie)|(enin)|[0-9]")
                    .find(line.reversed())!!
                val d2 = when (second.value) {
                    "eno" -> "1"
                    "owt" -> "2"
                    "eerht" -> "3"
                    "ruof" -> "4"
                    "evif" -> "5"
                    "xis" -> "6"
                    "neves" -> "7"
                    "thgie" -> "8"
                    "enin" -> "9"
                    else -> second.value
                }.first()
                listOf(d1, d2)//.also { println(it) }
            }
            .map { digits ->
                //println(digits)
                val list = digits.toCharArray()
                    .filter { it.isDigit() }
                ("" + list.first() + list.last())
                    //.also { println(digits) }
                    .toLong()
            }.toList()
        return digits.sum()
    }

    override fun part2(data: Sequence<String>): Long {
        return part2_mine(data) // 21ms
        //return part2_daniela(data) // 7.5ms
    }

    fun part2_daniela(data: Sequence<String>): Long { // Idea from Daniela Oliveira
        val values = mapOf(
            "one" to 1, "two" to 2, "three" to 3, "four" to 4,
            "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9,
            "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7,
            "8" to 8, "9" to 9
        )
        return data.map { line ->
            val first = values[line.findAnyOf(values.keys)!!.second]
            val second = values[line.findLastAnyOf(values.keys)!!.second]
            "$first$second"
                //.also { println(it) }
                .toLong()
        }.sum()
    }
}
fun main() {
    println("Test part1")
    val dayTest1 = Day01("Day01_test1.txt",true)
    check(dayTest1.runPart1().also { println("-> $it") } == 142L)
    println("Test part2")
    val dayTest = Day01(1, isTest=true)
    check(dayTest.runPart2().also { println("-> $it") } == 281L)

    val day = Day01(1, false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
    println("Bench part2")
    println(measureTime { day.runPart2() })
}
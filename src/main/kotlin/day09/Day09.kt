package day09

import Day

class Day09(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    private fun predictNext(values : List<Long>) : Long {
        val differences = values.windowed(2).map{(v1, v2)-> v2-v1}
        if (differences.all { it == 0L })
            return values.last()
        else {
            val p = predictNext(differences)
            return values.last() + p
        }
    }
    private fun predictPrevious(values : List<Long>) : Long {
        val differences = values.windowed(2).map{(v1, v2)-> v2-v1}
        if (differences.all { it == 0L })
            return values.first()
        else {
            val p = predictPrevious(differences)
            return values.first() - p
        }
    }

    override fun part1(data: Sequence<String>): Long {
        return data.map { line->
            predictNext(line.split(' ')
                .filter { it.isNotBlank() }
                .map { it.toLong() }).also { if (isTest) println(it) }
        }.sum()
    }

    override fun part2(data: Sequence<String>): Long {
        return data.map { line->
            predictPrevious(line.split(' ')
                .filter { it.isNotBlank() }
                .map { it.toLong() }).also { if (isTest) println(it) }
        }.sum()
    }
}

fun main() {
    val dayTest = Day09(9, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 114L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 2L)

    val day = Day09(9, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
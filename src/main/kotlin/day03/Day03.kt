package day03

import Day

class Day03(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        val lines = data.toList()
        var sum = 0L
        for ((idx,line) in lines.withIndex()) {
            val matches = Regex("\\d+").findAll(line)
            for (match in matches) {
                val range = match.range
                var added = false
                if (range.first != 0 && line[range.first-1] != '.') {
                    sum += match.value.toLong()
                    if (isTest) println("${match.value} added because ${line[range.first-1]} < ")
                    continue
                }
                if (range.last != line.lastIndex && line[range.last+1] != '.') {
                    sum += match.value.toLong()
                    if (isTest) println("${match.value} added because ${line[range.last+1]} > ")
                    continue
                }
                if (idx != 0) {
                    val prev = lines[idx - 1]
                    for (i in (range.first-1..range.last+1).intersect(prev.indices)) {
                        if (prev[i] != '.' && !prev[i].isDigit()) {
                            added = true
                            if (isTest) println("${match.value} added because ${prev[i]} ^[$i] ")
                            sum += match.value.toLong()
                            break
                        }
                    }
                }
                if (! added && idx != lines.lastIndex) {
                    val next = lines[idx + 1]
                    for (i in (range.first-1..range.last+1).intersect(next.indices)) {
                        if (next[i] != '.' && !next[i].isDigit()) {
                            if (isTest) println("${match.value} added because ${next[i]} v[$i] ")
                            sum += match.value.toLong()
                            break
                        }
                    }
                }
            }
        }
        return sum
    }
    data class Point(val x: Int, val y:Int)

    override fun part2(data: Sequence<String>): Long {
        val gears = mutableMapOf<Point, List<Int>>()
        val lines = data.toList()
        var sum = 0L
        for ((idx,line) in lines.withIndex()) {
            val matches = Regex("\\d+").findAll(line)
            for (match in matches) {
                val range = match.range
                var added = false
                if (range.first != 0 && line[range.first-1] == '*') {
                    addGear(idx, gears, range.first - 1, match.value.toInt())
                    if (isTest) println("${match.value} added because ${line[range.first-1]} < ")
                    continue
                }
                if (range.last != line.lastIndex && line[range.last+1] == '*') {
                    addGear(idx, gears, range.last + 1, match.value.toInt())
                    if (isTest) println("${match.value} added because ${line[range.last+1]} > ")
                    continue
                }
                if (idx != 0) {
                    val prev = lines[idx - 1]
                    for (i in (range.first-1..range.last+1).intersect(prev.indices)) {
                        if (prev[i] == '*') {
                            added = true
                            if (isTest) println("${match.value} added because ${prev[i]} ^[$i] ")
                            addGear(idx-1, gears, i, match.value.toInt())
                            break
                        }
                    }
                }
                if (! added && idx != lines.lastIndex) {
                    val next = lines[idx + 1]
                    for (i in (range.first-1..range.last+1).intersect(next.indices)) {
                        if (next[i] != '.' && !next[i].isDigit()) {
                            if (isTest) println("${match.value} added because ${next[i]} v[$i] ")
                            addGear(idx+1, gears, i, match.value.toInt())
                            break
                        }
                    }
                }
            }
        }
        for (entry in gears) {
            if (entry.value.size == 2) {
                if(isTest) println("Found gear @${entry.key} with ${entry.value}")
                sum += entry.value[0] * entry.value[1]
            }
        }
        return sum
    }

    private fun addGear(
        idx: Int,
        gears: MutableMap<Point, List<Int>>,
        x: Int,
        value: Int
    ) {
        val p = Point(x, idx)
        gears.compute(p) { _, l ->
            if (l != null) {
                l.addLast(value)
                l
            } else {
                mutableListOf(value)
            }
        }
    }
}

fun main() {
    val dayTest = Day03(3, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 4361L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 467835L)

    val day = Day03(3, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
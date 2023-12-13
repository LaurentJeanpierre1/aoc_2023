package day13

import Day

class Day13(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    override fun part1(data: Sequence<String>): Any {
        return 0L
    }
    override fun part2(data: Sequence<String>): Long {
        return 0L
    }
    override fun runPart1() : Any {
        val patterns = readText(fileName).split("\n\n")
        return patterns.sumOf { pattern ->
            val lines = pattern.lines()
            findHorizontal(lines).also { println("H:$it") } * 100L + findVertical(lines).also { println("V:$it\n") }
        }
    }
    override fun runPart2() : Any {
        val patterns = readText(fileName).split("${System.lineSeparator()}${System.lineSeparator()}").filter { it.isNotBlank() }
        return patterns.sumOf { pattern ->
            val lines = pattern.lines().toMutableList()
            val h = findHorizontal(lines).also { println("\nwas H:$it") }
            val v = findVertical(lines).also { println("was V:$it") }
            fixMirror(lines, h, v)
        }
    }

    private fun fixMirror(lines: MutableList<String>, h: Long, v: Long): Long {
        for (y in lines.indices) {
            val line = lines[y]
            for (x in line.indices) {
                val c = line[x]
                val newLine = line.replaceRange(x..x, if (c == '.') "#" else ".")
                lines[y] = newLine
                val h2 = findHorizontal(lines, h)
                val v2 = findVertical(lines, v)
                if (h2 != h && h2 != 0L) {
                    println("Fix col $x in $line -> H2=$h2")
                    return h2 * 100L
                } else if (v2 != v && v2 != 0L) {
                    println("Fix col $x in $line -> V2=$v2")
                    return v2
                }
                lines[y] = line
            }
        }
        return Long.MAX_VALUE
    }

    private fun findHorizontal(lines: List<String>, but: Long=-1): Long{
        for (i in 0..<lines.lastIndex) {
            val j = i + 1
            if (lines[j] == lines[i]) {
                val mirror = i + j
                var ok = true
                if (mirror < lines.size) {
                    for (k in 0..mirror)
                        if (lines[k] != lines[mirror - k]) {
                            ok = false
                            break
                        }
                } else {
                    for (k in mirror - lines.lastIndex..lines.lastIndex)
                        if (lines[k] != lines[mirror - k]) {
                            ok = false
                            break
                        }
                }
                if (ok) {
                    val res = 1 + mirror / 2L
                    if (res != but)
                        return res
                }
            }
        }
        return 0L
    }
    private fun colEqual(pattern: List<String>, col1: Int, col2: Int):Boolean {
        return pattern.all { line-> line[col1] == line[col2] }
    }
    private fun findVertical(lines: List<String>, but: Long=-1): Long {
        val width = lines.first().indices
        for (i in 0..< width.last) {
            val j = i + 1
            if (colEqual(lines, i, j)) {
                val mirror = i + j
                var ok = true
                if (mirror < width.last) {
                    for (k in 0..mirror)
                        if (!colEqual(lines, k, mirror - k)) {
                            ok = false
                            break
                        }
                } else {
                    for (k in mirror - width.last..width.last)
                        if (!colEqual(lines, k, mirror - k)) {
                            ok = false
                            break
                        }
                }
                if (ok){
                    val res = 1 + mirror / 2L
                    if (res != but)
                        return res
                }
            }
        }
        return 0L
    }
}

fun main() {
    //val dayTest = Day13("Day13_test2.txt", isTest=true)
    val dayTest = Day13(13, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 405L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 400L)

    val day = Day13(13, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
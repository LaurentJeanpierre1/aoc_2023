package day11

import Day
import kotlin.math.abs


class Day11(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    private var expansion = 1_000_000L
    constructor(day: Int, isTest: Boolean, expansion: Long = 1_000_000L) : this (makeFileName(day, isTest), isTest) {
        this.expansion = expansion
    }
    
    override fun part1(data: Sequence<String>): Long {
        val universe = data.filter { it.isNotBlank() }.map { it.toMutableList() }.toMutableList()
        expand(universe)
        if (isTest) universe.forEach{println(it)}
        val galaxies = findGalaxies(universe)
        var sum = 0L
        galaxies.forEachIndexed { first, firstGalaxy ->
            for (second in first+1 .. galaxies.lastIndex) {
                val secondGalaxy = galaxies[second]
                val dist = abs(secondGalaxy.x-firstGalaxy.x) + abs(secondGalaxy.y-firstGalaxy.y)
                if (isTest) println("From $first to $second dist = $dist")
                sum += dist
            }
        }
        return sum
    }

    data class Galaxy(val x : Int, val y : Int)
    private fun findGalaxies(universe: MutableList<MutableList<Char>>): MutableList<Galaxy> {
        val res = mutableListOf<Galaxy>()
        universe.forEachIndexed { index, line ->
            line.forEachIndexed { col, space -> if (space == '#') res.add(Galaxy(col, index)) }
        }
        return res
    }
    private fun expand(universe : MutableList<MutableList<Char>>) {
        var col = 0
        while (col < universe.first().size) {
            if (universe.all { it[col] == '.' }) {
                universe.forEach{ it.add(col, '.')}
                col += 2
            } else
                col ++
        }
        val ite = universe.listIterator()
        while (ite.hasNext()) {
            val line = ite.next()
            if (line.all { it == '.' })
                ite.add(line)
        }
    }
    override fun part2(data: Sequence<String>): Long {
        val universe = data.filter { it.isNotBlank() }.map { it.toMutableList() }.toMutableList()
        val columnLength = LongArray(universe.first().size) { 1L }
        var col = 0
        while (col < universe.first().size) {
            if (universe.all { it[col] == '.' }) {
                columnLength[col] = expansion
            }
            col ++
        }
        val lineLength = universe.map {line->
            if (line.all { it == '.' }) expansion else 1L
        }
        val galaxies = findGalaxies(universe)
        var sum = 0L
        galaxies.forEachIndexed { first, firstGalaxy ->
            for (second in first+1 .. galaxies.lastIndex) {
                val secondGalaxy = galaxies[second]
                val dist =  columnLength.slice(getInterval(firstGalaxy.x, secondGalaxy.x)).sum() +
                        lineLength.slice(getInterval(firstGalaxy.y, secondGalaxy.y)).sum()
                if (isTest) println("From $first to $second dist = $dist")
                sum += dist
            }
        }
        return sum
    }

    private fun getInterval(x1: Int, x2: Int) =
        if (x1 <= x2)
            x1..<x2
        else
            x2..<x1
}

fun main() {
    val dayTest = Day11(11, isTest=true, 100)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 374L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 8410L)

    val day = Day11(11, isTest=false, 1_000_000)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
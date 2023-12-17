package day17

import Day
import java.util.PriorityQueue
import kotlin.math.abs

class Day17(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    private lateinit var loss : List<List<Int>>
    private var lineSize : Int = 0

    data class Point(val x: Int, val y: Int, val isVertical: Boolean?) {
        constructor(x:Int, y:Int, score: Int, isVertical:Boolean?) : this(x, y, isVertical) {
            this.score = score
        }
        var heur = 0
        var score = 0
    }

    private fun makePoint(x:Int, y:Int, score: Int, isVertical: Boolean?) : Point =
        Point(x,y, score, isVertical).apply { heur = abs(lineSize-x) + abs(loss.lastIndex-y) }

    private val visited = mutableSetOf<Point>()
    private val queue = PriorityQueue { p1: Point, p2: Point -> (p1.score+p1.heur).compareTo(p2.score+p2.heur) }

    private fun addPoint(nx: Int, ny: Int, score: Int, isVertical: Boolean?) {
        val np = makePoint(nx, ny, score, isVertical)
        if (np !in visited) queue.add(np)
    }
    private fun Day17.addTilesRight(here: Point, minDistance: Int, maxDistance: Int) {
        var scoreRight = here.score
        for (i in 1..maxDistance) {
            val nx = here.x + i
            if (nx < lineSize) {
                scoreRight += loss[here.y][nx]
                if (i >= minDistance) {
                    addPoint(nx, here.y, scoreRight, false)
                }
            }
        }
    }

    private fun Day17.addTilesLeft(here: Point, minDistance: Int, maxDistance: Int) {
        var score = here.score
        for (i in 1..maxDistance) {
            val nx = here.x - i
            if (nx >= 0) {
                score += loss[here.y][nx]
                if (i >= minDistance) {
                    addPoint(nx, here.y, score, false)
                }
            }
        }
    }

    private fun Day17.addTilesDown(here: Point, minDistance: Int, maxDistance: Int) {
        var scoreDown = here.score
        for (i in 1..maxDistance) {
            val ny = here.y + i
            if (here.y + i < loss.size) {
                scoreDown += loss[ny][here.x]
                if (i >= minDistance) {
                    addPoint(here.x, ny, scoreDown, true)
                }
            }
        }
    }

    private fun Day17.addTilesUp(here: Point, minDistance: Int, maxDistance: Int) {
        var score = here.score
        for (i in 1..maxDistance) {
            val ny = here.y - i
            if (ny >= 0) {
                score += loss[ny][here.x]
                if (i >= minDistance) {
                    addPoint(here.x, ny, score, true)
                }
            }
        }
    }

    private fun parts(data: Sequence<String>, minDistance: Int, maxDistance: Int): Long {
        loss = data.map { line -> line.map { it.digitToInt() }.toList() }.toList()
        lineSize = loss.first().size

        visited.clear()
        queue.clear()

        addPoint(0, 0, 0, null)
        while (queue.isNotEmpty()) {
            val here = queue.poll()
            if (here.x == lineSize - 1 && here.y == loss.lastIndex) return here.score.toLong()
            if (here in visited) continue
            visited.add(here)
            if (here.isVertical != true) {
                addTilesUp(here, minDistance, maxDistance)
                addTilesDown(here, minDistance, maxDistance)
            }
            if (here.isVertical != false) {
                addTilesLeft(here, minDistance, maxDistance)
                addTilesRight(here, minDistance, maxDistance)
            }
        }
        throw IllegalStateException("No path found")
    }

    override fun part1(data: Sequence<String>): Long {
        return parts(data, 1, 3)
    }

    override fun part2(data: Sequence<String>): Long {
        return parts(data, 4, 10)
    }
}

fun main() {
    val dayTest = Day17(17, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 102L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 94L)

    val day = Day17(17, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
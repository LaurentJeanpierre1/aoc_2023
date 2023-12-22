package day22

import Day
import java.util.Scanner
import kotlin.math.max
import kotlin.math.min

class Day22(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    data class Brick(val x0: Int, val y0: Int, val z0: Int,
                     val x1: Int, val y1: Int, val z1: Int
        )
    private fun makeBrick(scanner: Scanner) : Brick {
        scanner.useDelimiter("[,~]")
        val x0: Int = scanner.nextInt()
        val y0: Int = scanner.nextInt()
        val z0: Int = scanner.nextInt()
        val x1: Int = scanner.nextInt()
        val y1: Int = scanner.nextInt()
        val z1: Int = scanner.nextInt()
        return Brick(min(x0,x1), min(y0,y1), min(z0,z1),
            max(x0,x1), max(y0,y1), max(z0,z1))
    }
    private val over = mutableMapOf<Brick, MutableList<Brick>>()
    private val under = mutableMapOf<Brick, MutableList<Brick>>()
    override fun part1(data: Sequence<String>): Long {
        val freeBricks = computeFreeBricks(data)
        return freeBricks.size.toLong()
    }

    private fun computeFreeBricks(data: Sequence<String>): MutableSet<Brick> {
        val bricks = data.map { line ->
            val scanner = Scanner(line)
            makeBrick(scanner)
        }.sortedBy { it.z0 }.toMutableList()
        if (isTest) bricks.forEach { println(it) }
        val minX = bricks.minOf { min(it.x0, it.x1) }
        val minY = bricks.minOf { min(it.y0, it.y1) }
        val maxX = bricks.maxOf { max(it.x0, it.x1) }
        val maxY = bricks.maxOf { max(it.y0, it.y1) }
        val heights = Array(maxY + 1 - minY) { Array<Brick?>(maxX + 1 - minX) { null } }
        freeFall(bricks, heights)
        val freeBricks = over.filter {
            it.value.none { above -> // keep bricks that have only one brick below
                val aside = under[above]!!.size
                aside == 1
            }
        }.map { it.key }.toMutableSet()
        return freeBricks
    }

    private fun freeFall(
        bricks: MutableList<Brick>,
        heights: Array<Array<Brick?>>
    ) {
        over.clear()
        under.clear()
        val iterator = bricks.listIterator()
        while (iterator.hasNext()) {
            val brick = iterator.next()
            var zMin = 0
            val on = mutableSetOf<Brick>()
            for (y in brick.y0..brick.y1) {
                for (x in brick.x0..brick.x1) {
                    val brickLaid = heights[y][x]
                    if (brickLaid != null) {
                        if (zMin < brickLaid.z0) {
                            zMin = brickLaid.z0
                            on.clear()
                            on += brickLaid
                        } else if (zMin == brickLaid.z0) {
                            on += brickLaid
                        }
                    }
                } // fox x
            } // for y
            zMin++
            val nBrick = brick.copy(z0 = zMin, z1 = zMin + brick.z1 - brick.z0)
            iterator.set(nBrick)
            for (y in brick.y0..brick.y1) {
                for (x in brick.x0..brick.x1) {
                    heights[y][x] = nBrick
                }
            }
            over[nBrick] = mutableListOf() // nothing on top yet
            on.forEach { below -> over[below]!! += nBrick }
            under[nBrick] = on.toMutableList()
        } // while hasNext
    }

    override fun part2(data: Sequence<String>): Long {
        val freeBricks = computeFreeBricks(data)
        var sum = 0L
        for ((b,above) in over) {
            if (above.isNotEmpty() && b !in freeBricks) {
                sum += nbFalling2(mutableListOf(b),b).also{
                    if (isTest) println("Removing $b makes $it falling")
                }
            }
        }
        return sum
    }

    private fun nbFalling(removed: MutableList<Brick>): Long {
        return under.map { (b, below) ->
            if (b.z0 == 1 || b in removed) // on ground or already removed -> not falling
                0L
            else {
                if (below.all { it in removed }) { // brick over nothing -> falling
                    removed.add(b)
                    val cascade = nbFalling(removed)
                    1L + cascade
                } else
                    0L
            }
        }.sum()
    }

    private fun nbFalling2(removed: MutableList<Brick>, falling: Brick): Long {
        val justRemoved = mutableListOf<Brick>()
        over[falling]!!.forEach { above ->
            if (above !in removed && under[above]!!.all {it in removed} ) {
                removed += above
                justRemoved += above
            }
        }
        return 1+justRemoved.sumOf { nbFalling2(removed, it) }
    }
}

fun main() {
    val dayTest = Day22(22, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 5L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 7L)

    val day = Day22(22, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2()) //58510 too low
}
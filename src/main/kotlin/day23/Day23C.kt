package day23

import Day
import kotlin.collections.LinkedHashSet

typealias Position = Pair<Int,Int>

// Version with cache
class Day23C(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        val map = data.map { it.toCharArray() }.toList()
        legMap.clear()
        return longest(map, 1 to 1, mutableSetOf(1 to 0), 0L, true, 1 to 0)
    }

    data class Leg(val entry: Position, val exit: Position, val tiles:List<Position>)
    private val legMap = mutableMapOf<Position, Leg>()
    private fun longest(
        map: List<CharArray>,
        position: Position,
        visited: MutableSet<Position>,
        soFar: Long,
        slippy: Boolean,
        from : Position
    ): Long {
        var (x,y) = position
        if (y == map.lastIndex) return soFar
        var next : MutableList<Position> = mutableListOf()
        val leg = legMap[position]?.also {
            next = mutableListOf()
            with(it.exit) {
                x = first
                y = second
            }
            if (it.tiles.isNotEmpty()) // immediate dead end, no tiles
                fillNext(next, x, y, map, slippy, it.tiles.last())
        } ?: run {
            val leg = LinkedHashSet<Position>()
            var prev = from
            do {
                fillNext(next, x, y, map, slippy, prev)
                if (next.size == 1) {
                    leg.add(x to y)
                    val nextPos = next.first()
                    prev = x to y
                    x = nextPos.first
                    y = nextPos.second
                }
            } while (next.size == 1)
            Leg(position, x to y, leg.toList()).also { legMap[position] = it }
        }
        visited += leg.tiles
        visited += leg.exit // add the choice point, which is not in the leg tiles
        val dist = soFar + leg.tiles.size + 1
        var max = if (next.isEmpty()) dist else 0L
        for (nextPos in next) {
            if (nextPos !in visited) {
                val nextLen = longest(map, nextPos, visited, dist, slippy, leg.exit)
                if (nextLen > max) max = nextLen
            }
        }
        visited.removeAll(leg.tiles.toSet())
        visited-=leg.exit
        return max
    }

    private fun fillNext(
        next: MutableList<Position>,
        x: Int,
        y: Int,
        map: List<CharArray>,
        slippy: Boolean,
        prev: Position
    ) {
        next.clear()
        if (y != map.lastIndex) {
            val tile = if (slippy) map[y][x] else '.'
            if (tile != 'v') addDirection(x + 1, y, prev, map, next)
            if (tile == '.') addDirection(x - 1, y, prev, map, next)
            if (tile == '.') addDirection(x, y - 1, prev, map, next)
            if (tile != '>') addDirection(x, y + 1, prev, map, next)
        }
    }

    private fun addDirection(
        nx: Int, ny: Int,
        prev: Position,
        map: List<CharArray>,
        next: MutableList<Position>
    ) {
        if ((nx to ny) != prev && map[ny][nx] != '#')
            next.add(nx to ny)
    }

    override fun part2(data: Sequence<String>): Long {
        val map = data.map { it.toCharArray() }.toList()
        legMap.clear()
        return longest(map, 1 to 1, mutableSetOf(1 to 0), 0L, false, 1 to 0)
    }
}

fun main() {
    val dayTest = Day23C(23, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 94L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 154L)

    val day = Day23C(23, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
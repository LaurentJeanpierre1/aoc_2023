package day23

import Day
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

// Version with coroutines
// part2 Blows up heap memory, even with -Xmx20G
class Day23P(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    override fun part1(data: Sequence<String>): Long {
        val map = data.map { it.toCharArray() }.toList()
        return runBlocking { longest(map, 1 to 1, mutableSetOf(1 to 0), 0L, true) }
    }

    private suspend fun longest(
        map: List<CharArray>,
        position: Pair<Int, Int>,
        visited: MutableSet<Pair<Int, Int>>,
        soFar: Long,
        slippy: Boolean
    ): Long = coroutineScope{
        var (x,y) = position
        if (y == map.lastIndex) return@coroutineScope soFar + 1
        var dist = soFar + 1
        val leg = mutableSetOf(position)
        visited += position
        val next = mutableSetOf<Pair<Int, Int>>()
        do {
            next.clear()
            if (y == map.lastIndex) {
                visited.removeAll(leg)
                return@coroutineScope dist
            }
            val tile = if (slippy) map[y][x] else '.'
            if (tile != 'v') addDirection(x + 1, y, visited, map, next)
            if (tile == '.') addDirection(x - 1, y, visited, map, next)
            if (tile == '.') addDirection(x, y - 1, visited, map, next)
            if (tile != '>') addDirection(x, y + 1, visited, map, next)
            if (next.size == 1) {
                dist++
                val nextPos = next.first()
                visited.add(nextPos)
                leg.add(nextPos)
                x = nextPos.first
                y = nextPos.second
            }
        } while (next.size == 1)

        val max = if (next.isEmpty()) 0L else next.map { nextPos ->
            async { longest(map, nextPos, HashSet(visited), dist, slippy) }
        }.awaitAll().max()

        visited.removeAll(leg)
        return@coroutineScope max
    }

    private fun addDirection(
        nx: Int,
        ny: Int,
        visited: MutableSet<Pair<Int, Int>>,
        map: List<CharArray>,
        next: MutableSet<Pair<Int, Int>>
    ) {
        if ((nx to ny) !in visited && map[ny][nx] != '#') next.add(nx to ny)
    }

    override fun part2(data: Sequence<String>): Long {
        val map = data.map { it.toCharArray() }.toList()
        return runBlocking { longest(map, 1 to 1, mutableSetOf(1 to 0), 0L, false) }
    }
}

fun main() {
    val dayTest = Day23P(23, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 94L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 154L)

    val day = Day23P(23, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
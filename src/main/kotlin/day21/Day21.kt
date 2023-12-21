package day21

import Day

class Day21(fileName: String, isTest: Boolean, private val steps: Int): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean, steps: Int) : this (makeFileName(day, isTest), isTest, steps)
    
    override fun part1(data: Sequence<String>): Long {
        val map = data.map { it.toCharArray() }.toList()
        val startY = map.indexOfFirst { it.contains('S') }
        val startX = map[startY].indexOfFirst { it == 'S' }
        map[startY][startX] = '.'
        var positions = setOf(startX to startY)
        val width = map.first().lastIndex
        val height = map.lastIndex
        repeat(steps) {
            val res = mutableSetOf<Pair<Int,Int>>()
            for ((x,y) in positions) {
                if (x > 0 && map[y][x-1] == '.') res += (x-1) to y
                if (y > 0 && map[y-1][x] == '.') res += x to (y-1)
                if (x < width && map[y][x+1] == '.') res += (x+1) to y
                if (y < height && map[y+1][x] == '.') res += x to (y+1)
            }
            positions = res
        }
        return positions.size.toLong()
    }
    lateinit var map: List<CharArray>

    private fun getTorusMap(y: Int, x: Int): Char {
        var ny = y
        var nx = x
        val width = map.first().size
        val height = map.size
        while (ny<0) ny += height
        while (nx<0) nx += width
        return map[ny % height][nx % width]
    }

    override fun part2(data: Sequence<String>): Long {
        map = data.map { it.toCharArray() }.toList()
        val startY = map.indexOfFirst { it.contains('S') }
        val startX = map[startY].indexOfFirst { it == 'S' }
        map[startY][startX] = '.'
        var positions = setOf(startX to startY)
        val width = map.first().size
        val height = map.size
        repeat(steps) {curStep->
            val res = mutableSetOf<Pair<Int,Int>>()
            positions.forEach { (ox,oy) ->
                var y = oy
                var x = ox
                while (y<0) y += height
                while (x<0) x += width
                if (getTorusMap(y,x-1) == '.') res += (ox-1) to oy
                if (getTorusMap(y-1,x) == '.') res += ox to (oy-1)
                if (getTorusMap(y,x+1) == '.') res += (ox+1) to oy
                if (getTorusMap(y+1,x) == '.') res += ox to (oy+1)
            }
            positions = res.also { if (curStep and 1023 == 1023) println("Steps $curStep -> ${it.size}") }
        }
        return positions.size.toLong()
    }
}

fun main() {
    val dayTest = Day21(21, isTest=true,6)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 16L)
    for (steps in listOf(6,10,50,100/*,500,1000,5000*/).zip(listOf(16, 50, 1594, 6536/*, 167004, 668697, 16733044*/))){
        val dayTestPart2 = Day21(21, isTest=true,steps.first)
        println("Test part2 ${steps.first}")
        check(dayTestPart2.runPart2().also { println("-> $it") } == steps.second.toLong())
    }

    val day = Day21(21, isTest=false, 64)
    println("Run part1")
    println(day.runPart1())
    val dayPart2 = Day21(21, isTest=false, 26501365)
    println("Run part2")
    println(dayPart2.runPart2())
}
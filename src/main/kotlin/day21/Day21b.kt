package day21

import Day

class Day21b(fileName: String, isTest: Boolean, private val steps: Int): Day(fileName, isTest) {
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

    override fun part2(data: Sequence<String>): Long {
        map = data.map { it.toCharArray() }.toList()
        val startY = map.indexOfFirst { it.contains('S') }
        val startX = map[startY].indexOfFirst { it == 'S' }
        map[startY][startX] = '.'
        var positions = mapOf((startX to startY) to listOf(0 to 0))
        val width = map.first().lastIndex
        val height = map.lastIndex
        repeat(steps) {curStep->
            val res = mutableMapOf<Pair<Int,Int>, MutableList<Pair<Int,Int>>>()
            positions.forEach { (pos,maps) ->
                val (x,y) = pos
                if (x==0) {
                    if (map[y][width] == '.') add(width,y, res, maps, 1, 0)
                    if (map[y][1] == '.') add(1,y, res, maps, 0, 0)
                } else if (x == width) {
                    if (map[y][0] == '.') add(0,y, res, maps, -1, 0)
                    if (map[y][x-1] == '.') add(x-1,y, res, maps, 0, 0)
                } else {
                    if (map[y][x+1] == '.') add(x+1,y, res, maps, 0, 0)
                    if (map[y][x-1] == '.') add(x-1,y, res, maps, 0, 0)
                }
                if (y==0) {
                    if (map[height][x] == '.') add(x,height, res, maps, 0, 1)
                    if (map[1][x] == '.') add(x,1, res, maps, 0, 0)
                } else if (y == height) {
                    if (map[0][x] == '.') add(x, 0, res, maps, 0, -1)
                    if (map[y-1][x] == '.') add(x, y-1, res, maps, 0, 0)
                } else {
                    if (map[y+1][x] == '.') add(x,y+1, res, maps, 0, 0)
                    if (map[y-1][x] == '.') add(x, y-1, res, maps, 0, 0)
                }
            }
            positions = res.also {
                if (curStep and 1023 == 1023) println("Steps $curStep -> ${it.map { (_,l) -> l.size.toLong() }.sum()}")
            }
        }
        return positions.map { (_,l) -> l.size.toLong() }.sum()
    }

    fun add(
        x: Int,
        y: Int,
        res: MutableMap<Pair<Int, Int>, MutableList<Pair<Int, Int>>>,
        maps: List<Pair<Int, Int>>,
        deltaX: Int,
        deltaY: Int
    ) {
        res.compute((x to y)) { _, list ->
            if (list == null) {
                maps.map { (x,y)-> (x+deltaX) to (y+deltaY)  }.toMutableList()
            } else {
                maps.forEach { (x, y) ->
                    val map = (x + deltaX) to (y + deltaY)
                    if (map !in list) list += map
                }
                list
            }
        }
    }
}

fun main() {
    /*for (steps in listOf(*//*6,10,50,100,*//*500,1000,5000).zip(listOf(*//*16, 50, 1594, 6536, *//*167004, 668697, 16733044))){
        val dayTestPart2 = Day21b(21, isTest=true,steps.first)
        println("Test part2 ${steps.first}")
        check(dayTestPart2.runPart2().also { println("-> $it") } == steps.second.toLong())
    }*/

    val dayPart2 = Day21b(21, isTest=false, 26501365)
    println("Run part2")
    println(dayPart2.runPart2())
}
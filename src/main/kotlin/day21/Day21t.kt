package day21

import Day

class Day21t(fileName: String, isTest: Boolean, private val steps: Int): Day(fileName, isTest) {
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
            println("$it: ${res.size}")
        }
        return positions.size.toLong()
    }
    lateinit var map: List<CharArray>

    private val converged = mutableMapOf<Pair<Int,Int>, Int>()
    override fun part2(data: Sequence<String>): Long {
        map = data.map { it.toCharArray() }.toList()
        val startY = map.indexOfFirst { it.contains('S') }
        val startX = map[startY].indexOfFirst { it == 'S' }
        map[startY][startX] = '.'
        var positions = mapOf((startX to startY) to mutableListOf(0 to 0))
        val width = map.first().lastIndex
        val height = map.lastIndex
        val justConverged = mutableSetOf<Pair<Int,Int>>()

        repeat(steps) {it->
            val curStep = 1+it
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
            positions = res
            justConverged.clear()
            res.values.asSequence().flatten().toSet().map { map->
                val count= res.values.count { it.contains(map) }
                if (count == 7627) { // alternates with 7770
                    converged[map] = curStep
                    println("$curStep: $map -> converged")
                    justConverged += map
                }
            }
            if (justConverged.isNotEmpty()) {
                val old = positions.map { (_,l) -> l.size.toLong() }.sum()
                positions.forEach { (_, list) -> list.removeIf { it in justConverged } }
                val new = positions.map { (_,l) -> l.size.toLong() }.sum()
                println("  -> prune from $old to $new")
            }
            if (curStep and 1023 == 1023 || curStep % 131 == 65) { // because 26501365%131 == 65
                val active = positions.map { (_, l) -> l.size.toLong() }.sum()
                val stable = converged.map{ if (it.value and 1 == 1) 7627L else 7770L }.sum()
                println("$curStep : $active+$stable = ${active+stable}")
            }
        } // repeat
        return positions.map { (_,l) -> l.size.toLong() }.sum()
    }

    fun add(
        x: Int,
        y: Int,
        res: MutableMap<Pair<Int, Int>, MutableList<Pair<Int, Int>>>,
        maps: MutableList<Pair<Int, Int>>,
        deltaX: Int,
        deltaY: Int
    ) {
        res.compute((x to y)) { _, list ->
            if (list == null) {
                maps.map { (x,y)-> (x+deltaX) to (y+deltaY)  }.filter { it !in converged }.toMutableList()
            } else {
                maps.forEach { (x, y) ->
                    val map = (x + deltaX) to (y + deltaY)
                    if (map !in converged && map !in list) list += map
                }
                list
            }
        }
    }

}

fun main() {
    val dayTest = Day21t(21, isTest=false,1_000_000)
    println("Test part2 eternal")
    dayTest.runPart2()
}
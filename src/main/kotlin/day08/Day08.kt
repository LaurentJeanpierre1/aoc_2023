package day08

import Day

data class Node(val label : String, val left: String, val right: String)
class Day08(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        val lines = data.iterator()
        val instructions = lines.next().toList()
        check(lines.next().isBlank())
        val mapLR = mutableMapOf<String, Node>()
        lines.forEachRemaining {line->
            val label = line.substring(0,3)
            val left = line.substring(7,10)
            val right = line.substring(12,15)
            mapLR[label] = Node(label, left, right)
        }
        var steps = 0L
        var here = "AAA"
        var current = instructions.iterator()
        while (here != "ZZZ") {
            ++steps
            val node = mapLR[here]!!
            here = if (current.next() == 'L') node.left else node.right
            if (! current.hasNext()) current = instructions.iterator()
        }
        return steps
    }

    override fun part2(data: Sequence<String>): Long {
        val lines = data.iterator()
        val instructions = lines.next().toList()
        check(lines.next().isBlank())
        val mapLR = mutableMapOf<String, Node>()
        lines.forEachRemaining {line->
            val label = line.substring(0,3)
            val left = line.substring(7,10)
            val right = line.substring(12,15)
            mapLR[label] = Node(label, left, right)
        }
        var steps = 0L
        var here = mapLR.keys.filter { it.last() == 'A' }
        var current = instructions.iterator()
        while (here.any { it.last() != 'Z' }) {
            ++steps
            val curDir = current.next() == 'L'
            if (! current.hasNext()) current = instructions.iterator()
            here = here.map { place->
                val node = mapLR[place]!!
                if (curDir) node.left else node.right
            }
        }
        return steps
    }
}

fun main() {
    val dayTest = Day08(8, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 2L)
    val dayTest2 = Day08("Day08_test2.txt", true)
    println("Test part2")
    check(dayTest2.runPart2().also { println("-> $it") } == 6L)

    val day = Day08(8, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
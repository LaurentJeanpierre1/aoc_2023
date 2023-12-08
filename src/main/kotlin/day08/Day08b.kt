package day08

import Day

class Day08b(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    data class Node(val label : String, val left: String, val right: String)
    data class Stage(val label : String, val instructionStep: Int)
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
        println("Starting points : $here")
        var current = instructions.iterator()
        var instructionStep = 0
        val stages = here.map{ mutableSetOf<Stage>() }
        val found = here.map { false }.toMutableList()
        while (here.any { it.last() != 'Z' }) {
            ++steps
            val curDir = current.next() == 'L'
            instructionStep++
            here = here.map { place->
                val node = mapLR[place]!!
                if (curDir) node.left else node.right
            }
            var path = 0
            for ((place, stage) in here.zip(stages)) {
                if (! found[path]) {
                    val currentStage = Stage(place, instructionStep)
                    if (stage.contains(currentStage)) {
                        println("Path $path - cycle found at $currentStage with $steps steps")
                        println(stage.filter { it.label.last()=='Z' })
                        found[path] = true
                        if (found.all { it })
                            return 0L
                    } else {
                        stage.add(currentStage)
                    }
                }
                path++
            }
            if (! current.hasNext()) {
                current = instructions.iterator()
                instructionStep = 0;
            }
            /*if (steps and 1023L == 0L)
                println("$steps : ${stages.map { it.size }} $found")*/
        }
        return steps
    }
}

fun main() {
    //val dayTest = Day08b(8, isTest=true)
    //println("Test part1")
    //check(dayTest.runPart1().also { println("-> $it") } == 2L)
    val dayTest2 = Day08b("Day08_test2.txt", true)
    println("Test part2")
    check(dayTest2.runPart2().also { println("-> $it") } == 6L)

    val day = Day08b(8, isTest=false)
    //println("Run part1")
    //println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
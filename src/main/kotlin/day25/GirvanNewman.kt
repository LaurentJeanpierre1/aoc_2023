package day25

import java.util.ArrayDeque

class GirvanNewman(fileName: String, isTest: Boolean): Day25(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    private val edges = mutableMapOf<Set<String>, Int>()
    override fun part1(data: Sequence<String>): Long {
        data.forEach { line->
            val parts = line.split(' ', ':').filter { it.isNotBlank() }.toList()
            val main = parts.first()
            parts.drop(1).forEach {
                add(main, it)
                add(it, main)
                edges += setOf(main, it) to 0
            }
        }
        repeat(3) {
            edges.forEach{(key,_) -> edges[key] = 0} // reset edge score
            girvanNewman()
            edges.asSequence().sortedBy { -it.value }.take(1).forEach {
                println(it)
                val ite = it.key.iterator()
                val from = ite.next()
                val to = ite.next()
                connected[from]!!.remove(to)
                connected[to]!!.remove(from)
                edges.remove(it.key)
            }
        }
        return computeResult()
    }

    private fun girvanNewman() {
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<List<String>>()
        for (node in connected.keys) {
            queue.add(listOf(node))
            visited.clear()
            visited += node
            while (queue.isNotEmpty()) {
                val path = queue.poll()
                val from = path.last()

                path.windowed(2).forEach { (stepFrom, stepTo) ->
                    edges.compute(setOf(stepFrom, stepTo)) { _, v -> v!! + 1 }
                }
                connected[from]!!.filter { it !in visited }.forEach { to ->
                    queue.add(path + to)
                    visited += to
                }
            }
        }
    }
}
fun main() {
    val dayTest = GirvanNewman(25, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 54L)

    val day = GirvanNewman(25, isTest=false)
    println("Run part1")
    println(day.runPart1())
}
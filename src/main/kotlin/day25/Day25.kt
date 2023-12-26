package day25

import Day
import java.io.File
import java.util.ArrayDeque

open class Day25(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    protected val connected = mutableMapOf<String, MutableList<String>>()
    fun add(from: String, to: String) {
        connected.compute(from){_, list ->
            if (list == null) mutableListOf(to)
            else {
                list.add(to)
                list
            }
        }
    }
    override fun part1(data: Sequence<String>): Long {
        val dot = File("$fileName.dot").printWriter()
        dot.println("strict graph{")
        data.forEach { line->
            val parts = line.split(' ', ':').filter { it.isNotBlank() }.toList()
            val main = parts.first()
            parts.drop(1).forEach {
                add(main, it)
                add(it, main)
                dot.println("$main -- $it")
            }
        }
        dot.println("}")
        dot.flush()
        dot.close()
        //connected.forEach{println(it)}
        // Run neato on $filename.dot
        //zpc-xvp
        //dhl-vfs
        //nzn-pbq
        if (!isTest) {
            connected["zpc"]!!.remove("xvp")
            connected["dhl"]!!.remove("vfs")
            connected["nzn"]!!.remove("pbq")
            connected["xvp"]!!.remove("zpc")
            connected["vfs"]!!.remove("dhl")
            connected["pbq"]!!.remove("nzn")
        } else {
            connected["hfx"]!!.remove("pzl")
            connected["bvb"]!!.remove("cmg")
            connected["nvd"]!!.remove("jqt")
            connected["pzl"]!!.remove("hfx")
            connected["cmg"]!!.remove("bvb")
            connected["jqt"]!!.remove("nvd")
        }
        return computeResult()
    }

    protected fun computeResult(): Long {
        val firstSet = mutableSetOf<String>()
        var node = connected.keys.first()
        val queue = ArrayDeque<String>()
        queue.add(node)
        while (queue.isNotEmpty()) {
            node = queue.poll()
            if (node !in firstSet) {
                firstSet += node
                queue.addAll(connected[node]!!)
                connected.remove(node)
            }
        }
        return firstSet.size.toLong().also { print("First set is $it large ") } * connected.keys.size.toLong()
            .also { print("Second set is $it large ") }
    }

    override fun part2(data: Sequence<String>): Long {
        TODO("Not yet implemented")
    }
}

fun main() {
    val dayTest = Day25(25, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 54L)
    //println("Test part2")
    //check(dayTest.runPart2().also { println("-> $it") } == 2286L)

    val day = Day25(25, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
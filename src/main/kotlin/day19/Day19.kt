package day19

import Day
import java.util.function.Predicate

class Day19(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    private val EMPTY_RANGE = 0..<0
    private val FULL_RANGE = 1..4000

    data class Item(val x: Int, val m: Int, val a:Int, val s:Int)
    data class Rule(val condition: Predicate<Item>, val destination: String)
    override fun part1(data: Sequence<String>): Long {
        val workflows = mutableMapOf<String,List<Rule>>()
        val iterator = data.iterator()
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (line.isBlank()) break
            val parts = line.split('{',',','}').filter { it.isNotBlank() }
            val name = parts[0]
            val rules = parts.drop(1).map {rule->
                val spec = rule.split(':').filter { it.isNotBlank() }
                val first = spec[0]
                if (spec.size==1) {
                    Rule( {true}, first)
                } else {
                    val carac = first[0]
                    val op = when(first[1]) {
                        '<' -> -1
                        '>' -> +1
                        '=' -> 0
                        else -> throw IllegalArgumentException("condition $rule")
                    }
                    val value = first.substring(2).toInt()
                    when(carac){
                        'x' -> Rule( {i:Item->i.x.compareTo(value) == op}, spec[1])
                        'm' -> Rule( {i:Item->i.m.compareTo(value) == op}, spec[1])
                        'a' -> Rule( {i:Item->i.a.compareTo(value) == op}, spec[1])
                        's' -> Rule( {i:Item->i.s.compareTo(value) == op}, spec[1])
                        else-> throw IllegalArgumentException("Unknown variable $carac in $rule")
                    }
                }
            }
            workflows[name] = rules
        }
        val items = mutableListOf<Item>()
        iterator.forEachRemaining() { line->
            val carac = line.split('{',',','}').filter { it.isNotBlank() }.map{it.drop(2)}.map { it.toInt() }
            items += Item(carac[0], carac[1], carac[2], carac[3])
        }

        return items.sumOf { item->
            var stage = "in"
            if (isTest) print("$item -> in ")
            while(stage != "A" && stage != "R") {
                val list = workflows[stage]
                if (list == null) throw IllegalArgumentException("No workflow $stage")
                for(rule in list)
                    if (rule.condition.test(item)) {
                        stage = rule.destination
                        break
                    }
                if (isTest) print(" -> $stage")
            }
            if (isTest) println()
            if (stage == "A")
                (item.x+item.m+item.a+item.s).toLong()
            else
                0L
        }
    }

    data class Rule2(val condition : Map<Char, Pair<Char, Int>>, val destination: String)

    private val workflows = mutableMapOf<String,List<Rule2>>()
    private val accept = mutableMapOf<String,List<Map<Char, IntRange>>>()
    override fun part2(data: Sequence<String>): Long {
        val iterator = data.iterator()
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (line.isBlank()) break
            val parts = line.split('{',',','}').filter { it.isNotBlank() }
            val name = parts[0]
            val rules = parts.drop(1).map {rule->
                val spec = rule.split(':').filter { it.isNotBlank() }
                val first = spec[0]
                if (spec.size==1) {
                    Rule2( emptyMap(), first)
                } else {
                    val carac = first[0]
                    val op = first[1]
                    val value = first.substring(2).toInt()
                    Rule2( mapOf(carac to (op to value)), spec[1])
                }
            }
            workflows[name] = rules
            if (rules.all { it.destination == "R" }) {
                accept[name] = emptyList()
            } else if (rules.all { it.destination == "A" }) {
                accept[name] = listOf( mapOf('x' to FULL_RANGE, 'm' to FULL_RANGE, 'a' to FULL_RANGE, 's' to FULL_RANGE) )
            }
        }
        val item = mapOf('x' to FULL_RANGE, 'm' to FULL_RANGE, 'a' to FULL_RANGE, 's' to FULL_RANGE)
        val accepted = getAccepted("in", item)
        return accepted.sumOf { it.map { (carac, range) -> range.last+1-range.first }. fold(1L) { prod, size-> prod*size } }
    }
    private fun getAccepted(workflow: String, item: Map<Char, IntRange>) :  List<Map<Char, IntRange>> {
        if (workflow == "A") return listOf(mapOf('x' to FULL_RANGE, 'm' to FULL_RANGE, 'a' to FULL_RANGE, 's' to FULL_RANGE))
        if (workflow == "R") return emptyList()
        if (accept.containsKey(workflow)) return accept[workflow]!!
        val list = workflows[workflow]!!
        var accepted = mutableListOf<Map<Char, IntRange>>()
        var current = item
        for (rule in list) {
            if (rule.condition.isEmpty())
                if (rule.destination == "A") return merge(accepted, current)
                else return accepted
            else {

            }
        }
    }
}


fun main() {
    val dayTest = Day19(19, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 19114L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 167409079868000L)

    val day = Day19(19, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
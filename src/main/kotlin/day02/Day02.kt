package day02

import Day

class Day02(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    override fun part1(data: Sequence<String>): Long {
        var sum = 0L
        for(line in data) {
            val elts = line.split(":",";")
            val gameId = elts[0]
                .drop(5) // remove "Game "
                .toInt()
            var ok = true
            for (draw in elts.drop(1)) {
                val dice = draw.split(","," ").filter { it.isNotBlank() }.iterator()
                while (ok && dice.hasNext()) {
                    val nb = dice.next().toInt()
                    val color = dice.next()
                    when (color){
                        "blue" -> ok = nb <= 14
                        "red" -> ok = nb <= 12
                        "green" -> ok = nb <= 13
                    }
                    if (isTest)
                        println("$gameId: $nb $color -> $ok")
                }
            }
            if (ok)
                sum += gameId
        }
        return sum
    }

    override fun part2(data: Sequence<String>): Long {
        var sum = 0L
        for(line in data) {
            val elts = line.split(":",";")
            val gameId = elts[0]
                .drop(5) // remove "Game "
                .toInt()
            var minB = 0
            var minG = 0
            var minR = 0
            for (draw in elts.drop(1)) {
                val dice = draw.split(","," ").filter { it.isNotBlank() }.iterator()
                while (dice.hasNext()) {
                    val nb = dice.next().toInt()
                    val color = dice.next()
                    when (color){
                        "blue" -> if (nb>minB) minB = nb
                        "red" -> if (nb>minR) minR = nb
                        "green" -> if (nb>minG) minG = nb
                    }
                    if (isTest)
                        println("$gameId: $nb $color -> $minR $minG $minB")
                }
            }
            sum += minB.toLong() * minG * minR
        }
        return sum
    }
}

fun main() {
    val dayTest = Day02(2, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 8L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 2286L)

    val day = Day02(2, false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}

package day02

import Day

class Day02(day: Int, isTest: Boolean) : Day(day, isTest) {
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
                }
            }
            sum += minB.toLong() * minG * minR
        }
        return sum
    }
}

fun main() {
    val dayTest = Day02(2, isTest=true)
    //check(dayTest.runPart1() == 8L)
    check(dayTest.runPart2() == 2286L)

    val day = Day02(2, false)
    //println(day.runPart1())
    println(day.runPart2())
}
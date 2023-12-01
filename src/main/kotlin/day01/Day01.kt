package day01

import Day

class Day01(day: Int, isTest: Boolean) : Day(day, isTest) {
    override fun part1(data: Sequence<String>): Long {
        val digits = data.map {
            val list = it.toCharArray()
                .filter { it.isDigit() }
            ("" + list.first() + list.last()).toLong()
        }.toList()
        return digits.sum()
    }


    //nineninesixskjkbhx6nineoneightj ---> attention au eigth final
    override fun part2(data: Sequence<String>): Long {
        val digits = data
            .map {line->
                val first = Regex("(one)|(two)|(three)|(four)|(five)|(six)|(seven)|(eight)|(nine)|[0-9]")
                    .find(line)!!
                val d1 = when(first.value){
                    "one" -> "1"
                    "two" -> "2"
                    "three" -> "3"
                    "four" -> "4"
                    "five" -> "5"
                    "six" -> "6"
                    "seven" -> "7"
                    "eight" -> "8"
                    "nine" -> "9"
                    else -> first.value
                }.first()
                val second = Regex("(eno)|(owt)|(eerht)|(ruof)|(evif)|(xis)|(neves)|(thgie)|(enin)|[0-9]")
                    .find(line.reversed())!!
                val d2 = when(second.value){
                    "eno" -> "1"
                    "owt" -> "2"
                    "eerht" -> "3"
                    "ruof" -> "4"
                    "evif" -> "5"
                    "xis" -> "6"
                    "neves" -> "7"
                    "thgie" -> "8"
                    "enin" -> "9"
                    else -> second.value
                }.first()
                listOf(d1,d2).also { println(it) }
            }
            .map {
                println(it)
            val list = it.toCharArray()
                .filter { it.isDigit() }
            ("" + list.first() + list.last()).also{println(it)}.toLong()
        }.toList()
        return digits.sum()
    }
}

fun main() {
    val dayTest = Day01(1, isTest=true)
    //check(dayTest.runPart1() == 142L)
    //check(dayTest.runPart2() == 281L)

    val day = Day01(1, false)
    //println(day.runPart1())
    println(day.runPart2())
}
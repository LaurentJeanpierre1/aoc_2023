package day07

import Day
import java.lang.IllegalStateException

val cardsOrder = mapOf('A' to 1, 'K' to 2, 'Q' to 3, 'J' to 4, 'T' to 5, '9' to 6, '8' to 7,
    '7' to 8, '6' to 9, '5' to 10, '4' to 11, '3' to 12, '2' to 13)
val cardsOrder2 = mapOf('A' to 1, 'K' to 2, 'Q' to 3, 'J' to 20, 'T' to 5, '9' to 6, '8' to 7,
    '7' to 8, '6' to 9, '5' to 10, '4' to 11, '3' to 12, '2' to 13)

enum class Kind { FIVE, FOUR, FULL, THREE, TWO_PAIRS, TWO, HIGH}

data class Hand(val kind : Kind, val cards: CharSequence, val comment: String = "")

class Day07(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        val hands = data.map { line ->
            val parts = line.split(" ").filter { it.isNotBlank() }
            makeHand(parts[0],"") to parts[1].toInt()
        }.sortedWith { o1, o2 -> compareHands(o1!!.first, o2!!.first, cardsOrder) }.toList()
        if (isTest) println(hands)
        var sum = 0L
        for ((idx, hand) in hands.withIndex())
            sum += (idx+1) * hand.second
        return sum
    }

    private fun compareHands(hand1: Hand, hand2: Hand, order: Map<Char, Int>): Int {
        val comp = hand2.kind.compareTo(hand1.kind)
        if (comp == 0) {
            val cards2 = hand2.cards.map { order[it]!! }
            val cards1 = hand1.cards.map { order[it]!! }
            for (pair in cards1.zip(cards2)) {
                val res = pair.second - pair.first
                if (res != 0) return res
            }
        }
        return comp
    }

    private fun makeHand(cardsSequence: String, comment: String): Hand {
        val cards = cardsSequence.toSet()
        val kind = when (cards.size) {
            1 -> Kind.FIVE
            2 -> when (cardsSequence.count { it == cards.first() }) {
                1 -> Kind.FOUR // 1 + 4
                2 -> Kind.FULL // 2 + 3
                3 -> Kind.FULL // 3 + 2
                4 -> Kind.FOUR // 4 + 1
                else -> throw IllegalStateException()
            }

            3 -> when (cardsSequence.count { it == cards.first() }) {
                1 -> when (cardsSequence.count { it == cards.last() }) {
                    1 -> Kind.THREE // 1 + 3 + 1
                    2 -> Kind.TWO_PAIRS // 1 + 2 + 2
                    3 -> Kind.THREE // 1 + 1 + 3
                    else -> throw IllegalStateException()
                }

                2 -> Kind.TWO_PAIRS // 2 + 1 + 2 or 2 + 2 + 1
                3 -> Kind.THREE // 3 + 1 + 1
                else -> throw IllegalStateException()
            }

            4 -> Kind.TWO // 1 + 1 + 1 + 2
            5 -> Kind.HIGH
            else -> throw IllegalStateException()
        }

        return Hand(kind, cardsSequence, comment)
    }

    override fun part2(data: Sequence<String>): Long {
        val hands = data.map { line ->
            val parts = line.split(" ").filter { it.isNotBlank() }
            if (parts[0].contains('J')) {
                cardsOrder2.keys.map { makeHand(parts[0].replace('J', it), "$it") }
                    .map{it.copy(cards = parts[0])}
                    .sortedWith{h1,h2 -> compareHands(h2, h1, cardsOrder2) }
                    .also { if (isTest) println(it) }
                    .first() to parts[1].toInt()
            } else
                makeHand(parts[0],"") to parts[1].toInt()
        }.sortedWith { o1, o2 -> compareHands(o1!!.first, o2!!.first, cardsOrder2) }.toList()
        if (isTest) println(hands)
        var sum = 0L
        for ((idx, hand) in hands.withIndex())
            sum += (idx+1) * hand.second
        return sum
    }
}

fun main() {
    val dayTest = Day07(7, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 6440L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 5905L)

    val day = Day07(7, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
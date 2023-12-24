package day24

import Day

class Day24(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    private val testRange = if (isTest)
        7L..27L
    else
        200000000000000L .. 400000000000000L

    override fun part1(data: Sequence<String>): Long {
        // test : 7->27
        // !test : 200000000000000 -> 400000000000000
        val stones = data.map {
            line-> line.split(" ",",","@")
                .filter { it.isNotBlank() }
                .map{it.toLong()}
                .toList()
        }.toList()
        var count = 0L
        for (a in stones.indices) {
            val (xa,ya,za,vxa,vya,vza) = stones[a]
            for (b in a+1..stones.lastIndex) {
                val (xb,yb,zb,vxb,vyb,vzb) = stones[b]
                println("Hailstone A : ${stones[a]}")
                println("Hailstone B : ${stones[b]}")
                // xa + Na*vxa == xb + Nb*vxb
                // ya + Na*vya == yb + Nb*vyb
                // N>=0
                // (xa,xb,ya,yb) in (min..max)^4
                if (vxa == 0L) {
                    check(vxb != 0L)
                    val nB = (xa-xb)/vxb.toDouble()
                    check(vya != 0L)
                    val nA = (yb-ya + nB*vyb) / vya
                    if (valid(xa,xb,ya,yb,nA,nB,vxa,vya))
                        count++
                } else if (vxb == 0L) {
                    check(vxa != 0L)
                    val nA = (xb-xa)/vxa.toDouble()
                    check(vyb != 0L)
                    val nB = (ya-yb + nA*vya) / vyb
                    if (valid(xa, xb, ya, yb, nA, nB, vxa, vya))
                        count++
                } else {
                    val divider = vyb - (vya * vxb).toDouble() / vxa
                    if (divider != 0.0) {
                        val nB = ((ya - yb) + vya * (xb - xa).toDouble() / vxa) / divider
                        val nA = (xb - xa + vxb * nB) / vxa
                        if (valid(xa, xb, ya, yb, nA, nB, vxa, vya))
                            count++
                    } else {
                        // vyb*vxb == vya*vxa
                        // vy/vx are equal -> parallel lines
                        println("parallel lines")
                    }
                }
            }
        }
        return count
    }

    private fun valid(xa: Long, xb: Long, ya: Long, yb: Long, nA: Double, nB: Double, vxa: Long, vya: Long): Boolean {
        if (nA<0) {
            if (isTest) println("in past for A")
            return false
        } else if (nB<0) {
            if (isTest) println("in past for B")
            return false
        } else {
            val nxa = xa + nA*vxa
            val nya = ya + nA*vya
            if (nxa.toLong() in testRange && nya.toLong() in testRange) {
                if (isTest) println("intersect @ ($nxa, $nya)")
                return true
            } else {
                if (isTest) println("outside")
                return false
            }
        }
    }
    override fun part2(data: Sequence<String>): Long {
        TODO("Not yet implemented")
    }
}

private operator fun <E> List<E>.component6(): E = this[5]

fun main() {
    val dayTest = Day24(24, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 2L)
    //println("Test part2")
    //check(dayTest.runPart2().also { println("-> $it") } == 2286L)

    val day = Day24(24, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
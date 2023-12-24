package day24

import Day
import io.ksmt.KContext
import io.ksmt.expr.KApp
import io.ksmt.solver.z3.KZ3Solver
import io.ksmt.sort.KIntSort
import io.ksmt.utils.getValue
import io.ksmt.utils.mkConst
import kotlin.time.Duration.Companion.seconds

class Day24(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    private val testRange = if (isTest)
        7L..27L
    else
        200000000000000L .. 400000000000000L

    override fun part1(data: Sequence<String>): Long {
        // test : 7 → 27
        // !test : 200000000000000 → 400000000000000
        val stones = getStones(data)
        var count = 0L
        for (a in stones.indices) {
            val (xa,ya,_,vxa,vya,_) = stones[a]
            for (b in a+1..stones.lastIndex) {
                val (xb,yb,_,vxb,vyb,_) = stones[b]
                if(isTest) {
                    println("Hailstone A : ${stones[a]}")
                    println("Hailstone B : ${stones[b]}")
                }
                // xa + Na*vxa == xb + Nb*vxb
                // ya + Na*vya == yb + Nb*vyb
                // N>=0
                // (xa,xb,ya,yb) in (min..max)^4
                if (vxa == 0L) {
                    check(vxb != 0L)
                    val nB = (xa-xb)/vxb.toDouble()
                    check(vya != 0L)
                    val nA = (yb-ya + nB*vyb) / vya
                    if (valid(xa, ya, nA, nB, vxa, vya))
                        count++
                } else if (vxb == 0L) {
                    check(vxa != 0L)
                    val nA = (xb-xa)/vxa.toDouble()
                    check(vyb != 0L)
                    val nB = (ya-yb + nA*vya) / vyb
                    if (valid(xa, ya, nA, nB, vxa, vya))
                        count++
                } else {
                    val divider = vyb - (vya * vxb).toDouble() / vxa
                    if (divider != 0.0) {
                        val nB = ((ya - yb) + vya * (xb - xa).toDouble() / vxa) / divider
                        val nA = (xb - xa + vxb * nB) / vxa
                        if (valid(xa, ya, nA, nB, vxa, vya))
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

    private fun getStones(data: Sequence<String>): List<List<Long>> {
        val stones = data.map { line ->
            line.split(" ", ",", "@")
                .filter { it.isNotBlank() }
                .map { it.toLong() }
                .toList()
        }.toList()
        return stones
    }

    private fun valid(xa: Long, ya: Long, nA: Double, nB: Double, vxa: Long, vya: Long): Boolean {
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
        val stones = getStones(data)
        val ctx = KContext()
        with(ctx) {
            // create symbolic variables
            val x0 by intSort
            val y0 by intSort
            val z0 by intSort
            val vx by intSort
            val vy by intSort
            val vz by intSort
            val vts = mutableListOf< KApp<KIntSort, *> >()

            return KZ3Solver(this).use { solver -> // create a Z3 SMT solver instance
                // assert expressions
                var cpt = 1
                for((xa,ya,za,vxa,vya,vza) in stones.take(5)) {
                    val t = intSort.mkConst("t$cpt")
                    vts +=  t
                    cpt++

                    val expr1 = (x0 + t * vx) eq (xa.expr + t * vxa.expr)
                    solver.assert(expr1)
                    val expr2 = (y0 + t * vy) eq (ya.expr + t * vya.expr)
                    solver.assert(expr2)
                    val expr3 = (z0 + t * vz) eq (za.expr + t * vza.expr)
                    solver.assert(expr3)
                    println(expr1)
                    println(expr2)
                    println(expr3)
                }

                // check assertions satisfiability with timeout
                val satisfiability = solver.check(timeout = 100.seconds)
                println(satisfiability) // SAT or UNSAT

                // obtain model
                val model = solver.model()
                var sum = 0L
                for (v in listOf(x0, y0, z0)) {
                    val value = model.eval(v)
                    sum += value.toString().toLong()
                    println("$v = $value")
                }
                for (v in listOf(vx, vy, vz))
                    println("$v = ${model.eval(v)}")
                for (v in vts)
                    println("$v = ${model.eval(v)}")
                sum
            }
        }
    }
}

private operator fun <E> List<E>.component6(): E = this[5]

fun main() {
    val dayTest = Day24(24, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 2L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 47L)

    val day = Day24(24, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
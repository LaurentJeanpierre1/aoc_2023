package day14

import Day
import java.util.BitSet

private const val VALUE = (1024*1024)-1

class Day14b(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        val platform = data.map { line -> line.toCharArray() }.toList()

        rollNorth(platform)
        if (isTest) platform.forEach { println(it) }

        return platform.mapIndexed{ no,line ->
            (platform.size-no)*line.count { it=='O' }.toLong()
        }.sum()
    }

    private fun rollNorth(rocks:List<CharArray>) {
        rocks.forEachIndexed{row, line->
            line.forEachIndexed { col, rock ->
                if (rock == 'O') {
                    var to = row
                    while(to>0 && rocks[to-1][col] == '.')
                        to--
                    if (to != row) {
                        rocks[to][col] = 'O'
                        line[col] = '.'
                    }
                }
            }
        }
    }
    private fun rollWest(rocks:List<CharArray>) {
        rocks.forEachIndexed{ _, line->
            line.forEachIndexed { col, rock ->
                if (rock == 'O') {
                    var to = col
                    while(to>0 && line[to-1] == '.')
                        to--
                    if (to != col) {
                        line[to] = 'O'
                        line[col] = '.'
                    }
                }
            }
        }
    }
    private fun rollSouth(rocks:List<CharArray>) {
        rocks.indices.reversed().forEach{row->
            val line = rocks[row]
            line.forEachIndexed { col, rock ->
                if (rock == 'O') {
                    var to = row
                    while(to<rocks.lastIndex && rocks[to+1][col] == '.')
                        to++
                    if (to != row) {
                        rocks[to][col] = 'O'
                        line[col] = '.'
                    }
                }
            }
        }
    }
    private fun rollEast(rocks:List<CharArray>) {
        rocks.forEachIndexed{ _, line->
            line.indices.reversed().forEach { col ->
                if (line[col] == 'O') {
                    var to = col
                    while(to<line.lastIndex && line[to+1] == '.')
                        to++
                    if (to != col) {
                        line[to] = 'O'
                        line[col] = '.'
                    }
                }
            }
        }
    }
    override fun part2(data: Sequence<String>): Long {
        val platform = data.map { line -> line.toCharArray() }.toList()
        val copies = mutableListOf<BitSet>()
        val realCopies = mutableListOf<List<CharArray>>()
        var cpt = 1
        var ultimate = BitSet()
        val lineSize = platform.first().size
        for(cycle in 0..<1000000000) {
            rollNorth(platform)
            rollWest(platform)
            rollSouth(platform)
            rollEast(platform)
            if((cycle and VALUE == VALUE)){
                println("After $cpt cycle(s):")
                cpt++
                //platform.forEach { println(cycle) }
            }
            val copy = platform.joinToString(separator = "") { it.joinToString("") }
            val bitSet = BitSet(platform.size* lineSize)
            copy.forEachIndexed { index, c -> if (c=='O') bitSet.set(index) }

            realCopies.add(platform.map { it.clone() })
            val indexOfCopy = copies.indexOf(bitSet)
            if (indexOfCopy != -1) {
                println("Stability achieved $indexOfCopy == $cycle")
                val rem = (999_999_999L-indexOfCopy) % (cycle-indexOfCopy)
                ultimate = copies[rem.toInt()+indexOfCopy]
                if (isTest) realCopies[rem.toInt()+indexOfCopy].forEach { println(it) }
                println()
                break
            } else
                copies.add(bitSet)
        }

        cpt=0
        platform.forEach { line ->
            line.forEachIndexed { index, _ -> line[index] = if (ultimate[cpt++]) 'O' else '.' }
            println(line)
        }
        return platform.mapIndexed{ no,line ->
            (platform.size-no)*line.count { it=='O' }.toLong()
        }.sum()
    }
}

fun main() {
    val dayTest = Day14b(14, isTest=true)
//    println("Test part1")
//    check(dayTest.runPart1().also { println("-> $it") } == 136L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 64L)

    val day = Day14b(14, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
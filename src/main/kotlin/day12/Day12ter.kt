package day12

/** Variant of Day12bis, replacing parallel processing with a cache -- much, much quicker! */
class Day12ter(fileName: String, isTest: Boolean): Day12bis(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
         return data.sumOf { line->
            cache.clear()
            processPart1(line)
        }
    }
    data class Point(val idxUnk: Int, val idxPacket: Int, val nbKO: Int)
    private val cache = mutableMapOf<Point,Long>()
    override fun recurse(unk: String, idxUnk: Int, packets: List<Int>, idxPacket: Int,
                        inPacket: Boolean, count: Int, nbOK: Int, nbKO:Int): Long {
        val p = Point(idxUnk, idxPacket, nbKO)
        if (cache.containsKey(p)) {
//            val res = super.recurse(unk, idxUnk, packets, idxPacket, inPacket, count, nbOK, nbKO)
//            if (res != cache[p])
//                throw IllegalStateException()
            return cache[p]!!
        } else {
            val res = super.recurse(unk, idxUnk, packets, idxPacket, inPacket, count, nbOK, nbKO)
            cache[p] = res
            return res
        }
    }

    override fun part2(data: Sequence<String>): Long {
        return data.mapIndexed { idx,line->
            cache.clear()
            processPart2(line).also { if (isTest) println("$idx : $it") }
        }.sum()
    }
}

fun main() {
    val dayTest = Day12ter(12, isTest=true)
    //val dayTest = Day12bis("Day12_test2.txt", isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 21L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 525152L)

    val day = Day12ter(12, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
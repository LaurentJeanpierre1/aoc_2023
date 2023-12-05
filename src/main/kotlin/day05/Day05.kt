package day05

import Day

class Day05(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        val lines = data.iterator()
        val seeds = lines.next()
            .drop(7)
            .split(' ').filter{it.isNotEmpty()}
            .map{it.toLong()}
            .toList()
        check(lines.next().isBlank())
        check(lines.next() == "seed-to-soil map:")
        val seed2soil = readMap(lines)
        if (isTest) println(seed2soil)
        check(lines.next() == "soil-to-fertilizer map:")
        val soil2fertilizer = readMap(lines)
        if (isTest) println(soil2fertilizer)
        check(lines.next() == "fertilizer-to-water map:")
        val fertilizer2water = readMap(lines)
        if (isTest) println(fertilizer2water)
        check(lines.next() == "water-to-light map:")
        val water2light = readMap(lines)
        if (isTest) println(water2light)
        check(lines.next() == "light-to-temperature map:")
        val light2temp = readMap(lines)
        if (isTest) println(light2temp)
        check(lines.next() == "temperature-to-humidity map:")
        val temp2humidity = readMap(lines)
        if (isTest) println(temp2humidity)
        check(lines.next() == "humidity-to-location map:")
        val humidity2location = readMap(lines)
        if (isTest) println(humidity2location)

        return seeds.asSequence().map{ seed->
            var next = seed
            for (mapping in seed2soil)
                if (mapping.key.contains(seed)) {
                    val res = mapping.value.first + seed - mapping.key.first
                    if (isTest) println("$seed in $mapping -> soil=$res")
                    next = res
                    break
                }
            next
        }.map{ soil->
            var next = soil
            for (mapping in soil2fertilizer)
                if (mapping.key.contains(soil)) {
                    val res = mapping.value.first + soil - mapping.key.first
                    if (isTest) println("$soil in $mapping -> fertilizer=$res")
                    next = res
                    break                }
            next
        }.map{ fertilizer->
            var next = fertilizer
            for (mapping in fertilizer2water)
                if (mapping.key.contains(fertilizer)) {
                    val res = mapping.value.first + fertilizer - mapping.key.first
                    if (isTest) println("$fertilizer in $mapping -> water=$res")
                    next = res
                    break                }
            next
        }.map{ water->
            var next = water
            for (mapping in water2light)
                if (mapping.key.contains(water)) {
                    val res = mapping.value.first + water - mapping.key.first
                    if (isTest) println("$water in $mapping -> light=$res")
                    next = res
                    break                }
            next
        }.map{ light->
            var next = light
            for (mapping in light2temp)
                if (mapping.key.contains(light)) {
                    val res = mapping.value.first + light - mapping.key.first
                    if (isTest) println("$light in $mapping -> temp=$res")
                    next = res
                    break                }
            next
        }.map{ temp->
            var next = temp
            for (mapping in temp2humidity)
                if (mapping.key.contains(temp)) {
                    val res = mapping.value.first + temp - mapping.key.first
                    if (isTest) println("$temp in $mapping -> humidity=$res")
                    next = res
                    break                }
            next
        }.map{ humidity->
            var next = humidity
            for (mapping in humidity2location)
                if (mapping.key.contains(humidity)) {
                    val res = mapping.value.first + humidity - mapping.key.first
                    if (isTest) println("$humidity in $mapping -> location=$res")
                    next = res
                    break                }
            next
        }.toList().min()
    }

    private fun readMap(lines: Iterator<String>): MutableMap<LongRange, LongRange> {
        val seed2soil = mutableMapOf<LongRange, LongRange>()
        do {
            val line = lines.next()
            if (line.isNotBlank()) {
                val parts = line.split(' ').filter { it.isNotBlank() }
                    .map { it.toLong() }.toList()
                seed2soil[LongRange(parts[1], parts[1] + parts[2] - 1)] = LongRange(parts[0], parts[0] + parts[2] - 1)
            }
        } while (line.isNotBlank() && lines.hasNext())
        return seed2soil
    }

    override fun part2(data: Sequence<String>): Long {
        val lines = data.iterator()
        val seeds = lines.next()
            .drop(7)
            .split(' ').filter{it.isNotEmpty()}
            .map{it.toLong()}
            .toList()
        check(lines.next().isBlank())
        check(lines.next() == "seed-to-soil map:")
        val seed2soil = readMap(lines)
        check(lines.next() == "soil-to-fertilizer map:")
        val soil2fertilizer = readMap(lines)
        check(lines.next() == "fertilizer-to-water map:")
        val fertilizer2water = readMap(lines)
        check(lines.next() == "water-to-light map:")
        val water2light = readMap(lines)
        check(lines.next() == "light-to-temperature map:")
        val light2temp = readMap(lines)
        check(lines.next() == "temperature-to-humidity map:")
        val temp2humidity = readMap(lines)
        check(lines.next() == "humidity-to-location map:")
        val humidity2location = readMap(lines)

        return seeds.chunked(2).map { (from,size)->from..from+size }
            .flatMap { seed-> if (isTest) println("seed $seed"); transform(seed, seed2soil) }.also { if (isTest) println("\n$it\n") }
            .flatMap { soil-> if (isTest) println("soil $soil");  transform(soil, soil2fertilizer) }.also { if (isTest) println("\n$it\n") }
            .flatMap { fertilizer-> if (isTest) println("fert $fertilizer");  transform(fertilizer, fertilizer2water) }.also { if (isTest) println("\n$it\n") }
            .flatMap { water -> if (isTest) println("water $water");  transform(water, water2light) }.also { if (isTest) println("\n$it\n") }
            .flatMap { light-> if (isTest) println("light $light");  transform(light, light2temp) }.also { if (isTest) println("\n$it\n") }
            .flatMap { temp-> if (isTest) println("temp $temp");  transform(temp, temp2humidity) }.also { if (isTest) println("\n$it\n") }
            .flatMap { humidity-> if (isTest) println("humid $humidity");  transform(humidity, humidity2location) }.also { if (isTest) println("\n$it\n") }
            .map { range->if (range.isEmpty()) Long.MAX_VALUE else range.first }.also { if (isTest) println("\n$it\n") }
            .min()
    }

    private fun transform(
        seed: LongRange,
        seed2soil: MutableMap<LongRange, LongRange>
    ): MutableList<LongRange> {
        val next = mutableListOf<LongRange>()
        var list = mutableListOf(seed)
        for (mapping in seed2soil) {
            val remaining = mutableListOf<LongRange>()
            for (range in list) {
                if (range.isEmpty()) continue
                if (isTest) println("$range vs $mapping")
                if (mapping.key.first < range.first)
                    if (mapping.key.last < range.first)
                        remaining += range // mapping before range
                    else if (mapping.key.last < range.last) { // mapping overlap start
                        remaining += mapping.key.last + 1..range.last
                        next += (mapping.value.first + range.first - mapping.key.first)..mapping.value.last
                    } else { // mapping include range
                        next += (mapping.value.first + range.first - mapping.key.first)..(mapping.value.first + range.last - mapping.key.first)
                    }
                else if (mapping.key.first > range.last) // mapping after range
                    remaining += range
                else if (mapping.key.last > range.last) {// mapping overlap end
                    remaining += range.first..<mapping.key.first
                    next += (mapping.value.first)..(mapping.value.first + range.last - mapping.key.first)
                } else { // mapping included in range
                    remaining += range.first..<mapping.key.first
                    remaining += mapping.key.last + 1..range.last
                    next += mapping.value
                }
            }
            list = remaining
            if (isTest) println("-> $list + $next")
        }
        next += list
        if (isTest) println("==> $next")
        return next
    }
}

fun main() {
    val dayTest = Day05(5, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 35L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 46L)

    val day = Day05(5, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
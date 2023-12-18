package day18

import Day
import day10.Day10.State
import day16.Day16.Direction
import java.util.PriorityQueue

class Day18(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    
    override fun part1(data: Sequence<String>): Long {
        val instructions = data.map { line ->
            val parts = line.split(" ").filter { it.isNotBlank() }
            Pair(when(parts[0]) {
                "R" -> Direction.RIGHT
                "L" -> Direction.LEFT
                "U" -> Direction.UP
                "D" -> Direction.DOWN
                else -> throw IllegalArgumentException()
            }, parts[1].toInt())
        }.toList()
        var minX=0
        var maxX=0
        var minY=0
        var maxY=0
        var x=0
        var y=0
        for(inst in instructions) {
            when(inst.first){
                Direction.UP -> y-=inst.second
                Direction.DOWN -> y+=inst.second
                Direction.LEFT -> x-=inst.second
                Direction.RIGHT -> x+=inst.second
            }
            if (x<minX) minX = x
            if (x>maxX) maxX = x
            if (y<minY) minY = y
            if (y>maxY) maxY = y
        }
        minX--
        maxX++
        minY--
        maxY++
        val map = Array(maxY+1-minY) { BooleanArray(maxX+1-minX) }
        for(inst in instructions) {
            when(inst.first){
                Direction.UP -> {
                    val ny = y-inst.second
                    for (row in ny..y)
                        map[row-minY][x-minX] = true
                    y = ny
                }
                Direction.DOWN -> {
                    val ny = y+inst.second
                    for (row in y..ny)
                        map[row-minY][x-minX] = true
                    y = ny
                }
                Direction.LEFT -> {
                    val nx = x-inst.second
                    for (col in nx..x)
                        map[y-minY][col-minX] = true
                    x = nx
                }
                Direction.RIGHT -> {
                    val nx = x+inst.second
                    for (col in x..nx)
                        map[y-minY][col-minX] = true
                    x = nx
                }
            }
        }
        if (isTest) map.forEach { line -> println(line.joinToString(""){ if (it) "#" else "." }) }

        map.forEachIndexed { row, line ->
            var state = State.OUT
            line.forEachIndexed { col, tile ->
                if (!tile)
                    when (state) {
                        State.IN -> line[col] = true
                        State.OUT -> line[col] = false
                        State.UP -> if (map[row + 1][col - 1]) {
                            // in
                            // ###.in
                            // ..#.
                            state = State.IN
                            line[col] = true
                        } else {
                            // in#.
                            // ###.out
                            state = State.OUT
                            line[col] = false
                        }
                        State.DOWN -> if (map[row + 1][col - 1]) {
                            // ###.out
                            // in#.
                            state = State.OUT
                            line[col] = false
                        } else {
                            // out#.
                            // ####.in
                            state = State.IN
                            line[col] = true
                        }

                    }
                else when (state) {
                    State.IN ->  if (!map[row][col + 1]) {
                        // in # out
                        state = State.OUT
                        line[col] = true // on the vertical edge
                    } else if (map[row + 1][col]) {
                        //      in
                        // in  ##
                        //     # out
                        state = State.UP
                        line[col] = true
                    } else {
                        //     #out
                        // in  ##
                        //    ..in
                        state = State.DOWN
                        line[col] = true
                    }
                    State.OUT -> if (!map[row][col + 1]) {
                        // out # in
                        state = State.IN
                        line[col] = true
                    } else if (map[row + 1][col]) {
                        // out ##
                        //     # in
                        state = State.DOWN
                        line[col] = true
                    } else {
                        //     #in
                        // out ##
                        //    ..out
                        state = State.UP
                        line[col] = true
                    }

                    State.UP -> line[col] = true
                    State.DOWN -> line[col] = true
                }
            }
        }
        if (isTest) map.forEach { line -> println(line.joinToString(""){ if (it) "#" else "." }) }
        return map.sumOf { line->line.count{it}.toLong() }
    }

    override fun part2(data: Sequence<String>): Long {
        val instructions = data.map { line ->
            val parts = line.split(" ").filter { it.isNotBlank() }
            val color = parts[2].drop(2).dropLast(1)
            Pair(when(color.last()) {
                '0' -> Direction.RIGHT
                '2' -> Direction.LEFT
                '3' -> Direction.UP
                '1' -> Direction.DOWN
                else -> throw IllegalArgumentException()
            }, color.dropLast(1).toInt(16)).also {
                if (isTest) println(it)
            }
        }.toList()
        var x = 0L
        var y = 0L
        data class Line(val y: Long, val x1: Long, val x2: Long)
        val horizontals = PriorityQueue<Line>{ l1, l2 -> l1.y.compareTo(l2.y)}
        for(inst in instructions) {
            when(inst.first){
                Direction.UP -> {
                    y-=inst.second
                }
                Direction.DOWN -> {
                    y+=inst.second
                }
                Direction.LEFT -> {
                    val nx = x-inst.second
                    horizontals += Line(y, nx, x)
                    x = nx
                }
                Direction.RIGHT -> {
                    val nx = x+inst.second
                    horizontals += Line(y, x, nx)
                    x = nx
                }
            }
        }

        val openLines= mutableListOf(horizontals.poll()!!) // horizontals to be included in next area computation
        y = openLines.first().y

        var area = openLines.sumOf { l-> (l.x2+1-l.x1) } // area of 1st line
        while (horizontals.isNotEmpty()) {
            val line = horizontals.poll()
            val ny = line.y
            if (y < ny) area += openLines.sumOf { l-> (ny-y) * (l.x2+1L-l.x1) } // Account for area between open lines and current (exclusive - inclusive)
            y = ny
            if (line.x2 < openLines.first().x1) {
                // new line at leftmost
                openLines.add(0, line)
                area += line.x2 + 1 - line.x1
            } else if (line.x1 > openLines.last().x2) { // new line at rightmost
                openLines.add(line)
                area += line.x2 + 1 - line.x1
            } else {
                val i = openLines.listIterator()
                while (i.hasNext()) {
                    val candidate = i.next()
                    if (line.x2 < candidate.x1) {
                        // x2 before x1 -> insert left
                        openLines.add(i.previousIndex(), line)
                        area += line.x2 + 1 - line.x1
                        break
                    } else if (line.x1 <= candidate.x2) { // if after candidate.x2, no need to check every possibility
                        if (line.x2 == candidate.x1) {
                            // left extension of candidate
                            area += candidate.x1 - line.x1 // candidate.x1 already included in area -> no +1
                            i.set(candidate.copy(y = ny, x1 = line.x1)) // new line is line.x1..candidate.x2
                            break
                        } else if (line.x2 < candidate.x2) {
                            if(line.x1 == candidate.x1) {
                                // left shrink of candidate
                                i.set(candidate.copy(y = ny, x1 = line.x2)) // new line is line.x2..candidate.x2
                                break
                            } else {
                                // split line in 2 discontinuous
                                i.set(candidate.copy(y = ny, x2 = line.x1)) // new line is candidate.x1..line.x1
                                i.add(candidate.copy(y = ny, x1 = line.x2)) // new line is line.x2..candidate.x2
                                break
                            }
                        } else if (line.x2 == candidate.x2) {
                            // right shrink of candidate
                            if (line.x1 == candidate.x1) {
                                // shrink to 0
                                i.remove()
                            } else {
                                // shrink to non-empty line
                                i.set(candidate.copy(y = ny, x2 = line.x1)) // new line is candidate.x1..line.x1
                            }
                            break
                        } else if (line.x1 == candidate.x2) {
                            // right extension of candidate
                            area += line.x2 - candidate.x2 // candidate.x2 already included in area -> no +1
                            if (i.hasNext()) {
                                val next = i.next()
                                if (next.x1 == line.x2) { // merge of two horizontals
                                    area -= 1 // line.x2 included twice in area -> remove it
                                    i.remove()
                                    i.previous()
                                    i.set(candidate.copy(y = ny, x2 = next.x2)) // new line is candidate.x1..next.x2)
                                } else {
                                    i.previous() // stay at the same place
                                    i.previous() // return to real previous
                                    i.set(candidate.copy(y = ny, x2 = line.x2)) // new line is candidate.x1..line.x2
                                }
                            } else {
                                i.set(candidate.copy(y = ny, x2 = line.x2)) // new line is candidate.x1..line.x2
                            }
                            break
                        }
                    } // if not after candidate
                } // while i.hasNext()
                if (isTest) println(openLines)
            } // if somewhere inside openLines
        } // while horizontals != empty
        return area
    }
}

fun main() {
    val dayTest = Day18(18, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 62L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 952408144115L)

    val day = Day18(18, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
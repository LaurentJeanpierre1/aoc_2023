package day10

import Day

class Day10(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
    enum class Direction {UP, DOWN, LEFT, RIGHT, NONE}
    data class Node(val x: Int, val y:Int, val dist:Int, val from:Direction, val to:Direction) {
        override fun equals(other: Any?): Boolean {
            if (other is Node)
                return x == other.x && y == other.y
            else
                return false
        }
        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }
    }
    override fun part1(data: Sequence<String>): Long {
        val lines = data.filter { it.isNotBlank() }.map { it.toCharArray() }.toList()
        val nodes = mutableSetOf<Node>()
        var node1 = Node(-1,-1,-1,Direction.NONE, Direction.NONE)
        var nodeS = node1
        var node2 = node1
        nodes.add(nodeS)
        for ((idx, line) in lines.withIndex()) {
            val col = line.indexOf('S')
            if (col != -1) {
                node1 = Node(col, idx, 0, Direction.NONE, Direction.NONE)
                node2 = node1
                nodeS = node1
                if (col>0)
                    node2 = seekLeft(col, idx, line[col - 1],1) ?: node2
                if (col<line.lastIndex) {
                    if (node2 == node1)
                        node2 = seekRight(col, idx, line[col + 1],1) ?: node2
                    else
                        node1 = seekRight(col, idx, line[col + 1],1) ?: node1
                }
                break
            }
        }
        if (node2 == node1) {
            // found no extremity yet
            node2 = seekUp(node1.x, node1.y, lines[node1.y-1][node1.x], 1)!!
            node1 = seekDown(node1.x, node1.y, lines[node1.y+1][node1.x], 1)!!
        } else if (node1.dist == 0) {
            // found one extremity yet
            if (node1.y>0)
                node1 = seekUp(node1.x, node1.y, lines[node1.y-1][node1.x], 1) ?: node1
            if (node1.dist == 0)
                node1 = seekDown(node1.x, node1.y, lines[node1.y+1][node1.x], 1)!!
        }
        if (isTest) println("First nodes are $node1 and $node2")
        nodes.add(node1)
        nodes.add(node2)
        replaceS(nodeS, node1, node2, lines)
        if (isTest) println("S is ${lines[nodeS.y][nodeS.x]}")
        while (node1.x != node2.x || node1.y != node2.y) {
            node1 = follow(node1, lines)
            nodes.add(node1)
            node2 = follow(node2, lines)
            nodes.add(node2)
            if (isTest) println("Next nodes are $node1 and $node2")
        }
        return node1.dist.toLong()
    }
    private fun follow(node1: Node, lines: List<CharArray>) = when (node1.to) {
        Direction.UP -> seekUp(node1.x, node1.y, lines[node1.y - 1][node1.x], node1.dist + 1)!!
        Direction.DOWN -> seekDown(node1.x, node1.y, lines[node1.y + 1][node1.x], node1.dist + 1)!!
        Direction.LEFT -> seekLeft(node1.x, node1.y, lines[node1.y][node1.x - 1], node1.dist + 1)!!
        Direction.RIGHT -> seekRight(node1.x, node1.y, lines[node1.y][node1.x + 1], node1.dist + 1)!!
        Direction.NONE -> throw IllegalStateException()
    }

    private fun seekRight(col: Int, idx: Int, square: Char, dist: Int) = when (square) {
        '-' -> Node(col + 1, idx, dist, Direction.LEFT, Direction.RIGHT)
        'J' -> Node(col + 1, idx, dist, Direction.LEFT, Direction.UP)
        '7' -> Node(col + 1, idx, dist, Direction.LEFT, Direction.DOWN)
        else -> null
    }
    private fun seekLeft(col: Int, idx: Int, square: Char, dist : Int) = when (square) {
        '-' -> Node(col - 1, idx, dist, Direction.RIGHT, Direction.LEFT)
        'L' -> Node(col - 1, idx, dist, Direction.RIGHT, Direction.UP)
        'F' -> Node(col - 1, idx, dist, Direction.RIGHT, Direction.DOWN)
        else -> null
    }
    private fun seekUp(col: Int, idx: Int, square: Char, dist: Int) = when (square) {
        '|' -> Node(col, idx - 1, dist, Direction.DOWN, Direction.UP)
        'F' -> Node(col, idx - 1, dist, Direction.DOWN, Direction.RIGHT)
        '7' -> Node(col, idx - 1, dist, Direction.DOWN, Direction.LEFT)
        else -> null
    }
    private fun seekDown(col: Int, idx: Int, square: Char, dist : Int) = when (square) {
        '|' -> Node(col, idx + 1, dist, Direction.UP, Direction.DOWN)
        'L' -> Node(col, idx + 1, dist, Direction.UP, Direction.RIGHT)
        'J' -> Node(col, idx + 1, dist, Direction.UP, Direction.LEFT)
        else -> null
    }

    fun replaceS(nodeS: Node, node1: Node, node2: Node, lines: List<CharArray>) {
        lines[nodeS.y][nodeS.x] = when(node1.from){
            Direction.UP -> when(node2.from) {
                Direction.DOWN -> '|'
                Direction.LEFT -> 'F'
                Direction.RIGHT -> '7'
                else -> throw IllegalStateException()
            }
            Direction.DOWN -> when(node2.from) {
                Direction.UP -> '|'
                Direction.LEFT -> 'L'
                Direction.RIGHT -> 'J'
                else -> throw IllegalStateException()
            }
            Direction.LEFT -> when(node2.from) {
                Direction.UP -> 'F'
                Direction.DOWN -> 'L'
                Direction.RIGHT -> '-'
                else -> throw IllegalStateException()
            }
            Direction.RIGHT -> when(node2.from) {
                Direction.UP -> '7'
                Direction.DOWN -> 'J'
                Direction.LEFT -> '-'
                else -> throw IllegalStateException()
            }
            else -> throw IllegalStateException()
        }
    }

    override fun part2(data: Sequence<String>): Long {
        val lines = data.filter { it.isNotBlank() }.map { it.toCharArray() }.toList()
        val nodes = mutableSetOf<Node>()
        var node1 = Node(-1,-1,-1,Direction.NONE, Direction.NONE)
        var nodeS = node1
        var node2 = node1
        for ((idx, line) in lines.withIndex()) {
            val col = line.indexOf('S')
            if (col != -1) {
                node1 = Node(col, idx, 0, Direction.NONE, Direction.NONE)
                node2 = node1
                nodeS = node1
                nodes.add(nodeS)
                if (col>0)
                    node2 = seekLeft(col, idx, line[col - 1],1) ?: node2
                if (col<line.lastIndex) {
                    if (node2 == node1)
                        node2 = seekRight(col, idx, line[col + 1],1) ?: node2
                    else
                        node1 = seekRight(col, idx, line[col + 1],1) ?: node1
                }
                break
            }
        }
        if (node2 == node1) {
            // found no extremity yet
            node2 = seekUp(node1.x, node1.y, lines[node1.y-1][node1.x], 1)!!
            node1 = seekDown(node1.x, node1.y, lines[node1.y+1][node1.x], 1)!!
        } else if (node1.dist == 0) {
            // found one extremity yet
            if (node1.y>0)
                node1 = seekUp(node1.x, node1.y, lines[node1.y-1][node1.x], 1) ?: node1
            if (node1.dist == 0)
                node1 = seekDown(node1.x, node1.y, lines[node1.y+1][node1.x], 1)!!
        }
        if (isTest) println("First nodes are $node1 and $node2")
        nodes.add(node1)
        nodes.add(node2)
        replaceS(nodeS, node1, node2, lines)
        check(lines[nodeS.y][nodeS.x] != 'S')
        if (isTest) println("S is ${lines[nodeS.y][nodeS.x]}")
        while (node1.x != node2.x || node1.y != node2.y) {
            node1 = follow(node1, lines)
            nodes.add(node1)
            node2 = follow(node2, lines)
            nodes.add(node2)
            if (isTest) println("Next nodes are $node1 and $node2")
        }

        return lines.mapIndexed { lineNo: Int, line: CharArray ->
            var state = State.OUT
            var count = 0L
            for ((col,tile) in line.withIndex()) {
                val pair = if (nodes.contains(Node(col,lineNo,0,Direction.NONE, Direction.NONE)))
                    nextState(tile, state, count) else nextState('.', state, count)
                count = pair.second
                state = pair.first
            }
            if (isTest) println()
            count
        }.sum()
    }

    private fun nextState(
        tile: Char,
        state: State,
        count: Long
    ): Pair<State, Long> {
        var count1 = count
        val state1 = when (tile) {
            '.' -> {
                if (state == State.IN) {
                    count1++
                    if (isTest) print('1')
                } else
                    if (isTest) print('0')
                state
            }

            '|' -> {
                if (isTest) print('|')
                when (state) {
                    State.IN -> State.OUT
                    State.OUT -> State.IN
                    else -> throw IllegalStateException()
                }
            }

            '-' -> {
                if (isTest) print('-')
                state
            }

            'F' -> {
                if (isTest) print('F')
                when (state) {
                    State.IN -> State.UP
                    State.OUT -> State.DOWN
                    State.UP -> throw IllegalStateException()
                    State.DOWN -> throw IllegalStateException()
                }
            }

            'L' -> {
                if (isTest) print('L')
                when (state) {
                    State.IN -> State.DOWN
                    State.OUT -> State.UP
                    State.UP -> throw IllegalStateException()
                    State.DOWN -> throw IllegalStateException()
                }
            }

            '7' -> {
                if (isTest) print('7')
                when (state) {
                    State.IN -> throw IllegalStateException()
                    State.OUT -> throw IllegalStateException()
                    State.UP -> State.IN
                    State.DOWN -> State.OUT
                }
            }

            'J' -> {
                if (isTest) print('J')
                when (state) {
                    State.IN -> throw IllegalStateException()
                    State.OUT -> throw IllegalStateException()
                    State.UP -> State.OUT
                    State.DOWN -> State.IN
                }
            }

            else -> throw IllegalStateException()
        }
        return Pair(state1, count1)
    }

    enum class State{IN, OUT, UP, DOWN}
}

fun main() {
    val dayTest = Day10(10, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 8L)
    val dayTest2 = Day10("Day10_test2.txt", true)
    println("Test part2")
    check(dayTest2.runPart2().also { println("-> $it") } == 10L)

    val day = Day10(10, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
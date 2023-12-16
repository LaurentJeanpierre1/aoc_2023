package day16

import Day

class Day16(fileName: String, isTest: Boolean): Day(fileName, isTest) {
    constructor(day: Int, isTest: Boolean) : this(makeFileName(day, isTest), isTest)

    enum class Direction { UP, DOWN, LEFT, RIGHT }
    enum class Mirror(val char: Char) {
        UP_RIGHT('/'),
        UP_LEFT('\\'),
        Vertical('|'),
        Horizontal('-'),
        Empty('.');
    }

    data class Tile(val mirror: Mirror) {
        val beams: BooleanArray = BooleanArray(4) { false }
        override fun toString(): String {
            return mirror.char.toString()
        }

        fun isEnergized(): Boolean = beams.any { it }
    }

    override fun part1(data: Sequence<String>): Long {
        val wall = readWall(data)
        beamRight(wall, 0, 0)
        return wall.sumOf { it.count { tile -> tile.isEnergized() }.toLong() }
    }

    private fun readWall(data: Sequence<String>) = data.map { line ->
        line.map { char ->
            Tile(
                when (char) {
                    '.' -> Mirror.Empty
                    '-' -> Mirror.Horizontal
                    '|' -> Mirror.Vertical
                    '\\' -> Mirror.UP_LEFT
                    '/' -> Mirror.UP_RIGHT
                    else -> throw IllegalArgumentException("$char is unknown")
                }
            )
        }.toList()
    }.toList()

    private fun beamRight(wall: List<List<Tile>>, y: Int, x: Int) {
        if (y in wall.indices) {
            val line = wall[y]
            var col = x
            while (col < line.size) {
                if (line[col].beams[Direction.RIGHT.ordinal])
                    break // already this beam here
                else
                    line[col].beams[Direction.RIGHT.ordinal] = true
                when (line[col].mirror) {
                    Mirror.UP_RIGHT -> {
                        beamUp(wall, y - 1, col); break
                    }

                    Mirror.UP_LEFT -> {
                        beamDown(wall, y + 1, col); break
                    }

                    Mirror.Vertical -> {
                        beamUp(wall, y - 1, col); beamDown(wall, y + 1, col); break
                    }

                    Mirror.Horizontal,
                    Mirror.Empty -> ++col
                }
            }
        }
    }

    private fun beamLeft(wall: List<List<Tile>>, y: Int, x: Int) {
        if (y in wall.indices) {
            val line = wall[y]
            var col = x
            while (col >= 0) {
                if (line[col].beams[Direction.LEFT.ordinal])
                    break // already this beam here
                else
                    line[col].beams[Direction.LEFT.ordinal] = true
                when (line[col].mirror) {
                    Mirror.UP_RIGHT -> {
                        beamDown(wall, y + 1, col); break
                    }

                    Mirror.UP_LEFT -> {
                        beamUp(wall, y - 1, col); break
                    }

                    Mirror.Vertical -> {
                        beamUp(wall, y - 1, col); beamDown(wall, y + 1, col); break
                    }

                    Mirror.Horizontal,
                    Mirror.Empty -> --col
                }
            }
        }
    }

    private fun beamDown(wall: List<List<Tile>>, y: Int, x: Int) {
        if (y in wall.indices && x in wall.first().indices) {
            var row = y
            while (row < wall.size) {
                if (wall[row][x].beams[Direction.DOWN.ordinal])
                    break // already this beam here
                else
                    wall[row][x].beams[Direction.DOWN.ordinal] = true
                when (wall[row][x].mirror) {
                    Mirror.UP_RIGHT -> {
                        beamLeft(wall, row, x - 1); break
                    }

                    Mirror.UP_LEFT -> {
                        beamRight(wall, row, x + 1); break
                    }

                    Mirror.Horizontal -> {
                        beamLeft(wall, row, x - 1); beamRight(wall, row, x + 1); break
                    }

                    Mirror.Vertical,
                    Mirror.Empty -> ++row
                }
            }
        }
    }

    private fun beamUp(wall: List<List<Tile>>, y: Int, x: Int) {
        if (y in wall.indices && x in wall.first().indices) {
            var row = y
            while (row >= 0) {
                if (wall[row][x].beams[Direction.UP.ordinal])
                    break // already this beam here
                else
                    wall[row][x].beams[Direction.UP.ordinal] = true
                when (wall[row][x].mirror) {
                    Mirror.UP_RIGHT -> {
                        beamRight(wall, row, x + 1); break
                    }

                    Mirror.UP_LEFT -> {
                        beamLeft(wall, row, x - 1); break
                    }

                    Mirror.Horizontal -> {
                        beamLeft(wall, row, x - 1); beamRight(wall, row, x + 1); break
                    }

                    Mirror.Vertical,
                    Mirror.Empty -> --row
                }
            }
        }
    }

    override fun part2(data: Sequence<String>): Long {
        val wall = readWall(data)
        var max = 0L
        for (x in wall.first().indices) {
            beamDown(wall, 0, x)
            val energyDown = wall.sumOf { it.count { tile -> tile.isEnergized() }.toLong() }
            if (energyDown > max) max = energyDown
            wall.forEach { line -> line.forEach { tile -> for (i in 0..3) tile.beams[i] = false } }
            beamUp(wall, wall.lastIndex, x)
            val energyUp = wall.sumOf { it.count { tile -> tile.isEnergized() }.toLong() }
            if (energyUp > max) max = energyUp
            wall.forEach { line -> line.forEach { tile -> for (i in 0..3) tile.beams[i] = false } }
        }
        for (y in wall.indices) {
            beamRight(wall, y, 0)
            val energyRight = wall.sumOf { it.count { tile -> tile.isEnergized() }.toLong() }
            if (energyRight > max) max = energyRight
            wall.forEach { line -> line.forEach { tile -> for (i in 0..3) tile.beams[i] = false } }
            beamLeft(wall, y, wall.first().lastIndex)
            val energyLeft = wall.sumOf { it.count { tile -> tile.isEnergized() }.toLong() }
            if (energyLeft > max) max = energyLeft
            wall.forEach { line -> line.forEach { tile -> for (i in 0..3) tile.beams[i] = false } }
        }
        return max
    }
}

fun main() {
    val dayTest = Day16(16, isTest=true)
    println("Test part1")
    check(dayTest.runPart1().also { println("-> $it") } == 46L)
    println("Test part2")
    check(dayTest.runPart2().also { println("-> $it") } == 51L)

    val day = Day16(16, isTest=false)
    println("Run part1")
    println(day.runPart1())
    println("Run part2")
    println(day.runPart2())
}
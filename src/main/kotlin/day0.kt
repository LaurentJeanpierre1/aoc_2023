abstract class Day(private val fileName: String, val isTest: Boolean) {

    companion object {
        fun makeFileName(day: Int, isTest: Boolean): String =
            (if (isTest) "Day%02d_test.txt" else "Day%02d.txt").format(day)
    }
    constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)

    abstract fun part1(data: Sequence<String>): Any

    abstract fun part2(data: Sequence<String>) : Any

    open fun runPart1() : Any {
        readBuffered(fileName).useLines { return part1(it) }
    }
    open fun runPart2(): Any {
        readBuffered(fileName).useLines { return part2(it) }
    }

    /** Extract BufferedReader from ressource file */
    fun readBuffered(fileName: String) = object {}.javaClass.getResourceAsStream(fileName)!!
        .bufferedReader()

    /** Extract String from ressource file */
    fun readText(fileName: String) = object {}.javaClass.getResource(fileName)!!.readText()

}
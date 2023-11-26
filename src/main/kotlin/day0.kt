abstract class Day(private val day: Int, val isTest: Boolean) {

    protected val fileName = (if (isTest) "Day%02d_test.txt"
        else
            "Day%02d.txt").format(day)

    abstract fun part1(data: Sequence<String>): Long

    abstract fun part2(data: Sequence<String>) : Long

    open fun runPart1() : Long {
        readBuffered(fileName).useLines { return part1(it) }
    }
    open fun runPart2(): Long {
        readBuffered(fileName).useLines { return part2(it) }
    }

    /** Extract BufferedReader from ressource file */
    fun readBuffered(fileName: String) = object {}.javaClass.getResourceAsStream(fileName)!!
        .bufferedReader()

    /** Extract String from ressource file */
    fun readText(fileName: String) = object {}.javaClass.getResource(fileName)!!.readText()

}
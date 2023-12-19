package duplicator

import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.writeText

class Duplicator {
    private val path = ("/Users/Jeanpierre/IdeaProjects/AoC2023/src/main")
    private val kotlinPath: Path = Path.of(path, "kotlin")
    private val resPath: Path = Path.of(path, "resources")

    fun create(day: Int) {
        val name = "Day%02d".format(day)
        createKlass(name, day)
        createRes(name)
    }
    private fun createRes(name: String) {
        val data = resPath.resolve("$name.txt")
        data.createFile()
        val test = resPath.resolve("${name}_test.txt")
        test.createFile()
    }
    private fun createKlass(name: String, day: Int) {
        val pack = name.lowercase()
        val folder = kotlinPath.resolve(pack)
        folder.createDirectory()
        val klass = folder.resolve("$name.kt")
        val it="\$it"
        klass.writeText(
            """
    package $pack
    
    import Day
    
    class $name(fileName: String, isTest: Boolean): Day(fileName, isTest) {
        constructor(day: Int, isTest: Boolean) : this (makeFileName(day, isTest), isTest)
        
        override fun part1(data: Sequence<String>): Long {
            TODO("Not yet implemented")
        }
    
        override fun part2(data: Sequence<String>): Long {
            TODO("Not yet implemented")
        }
    }
    
    fun main() {
        val dayTest = $name($day, isTest=true)
        println("Test part1")
        check(dayTest.runPart1().also { println("-> $it") } == 8L)
        //println("Test part2")
        //check(dayTest.runPart2().also { println("-> $it") } == 2286L)
    
        val day = $name($day, isTest=false)
        println("Run part1")
        println(day.runPart1())
        println("Run part2")
        println(day.runPart2())
    }
            """.trimIndent()
        )
    }
}

fun main() {
    val dup = Duplicator()
    for (day in 4..25)
        dup.create(day)
}
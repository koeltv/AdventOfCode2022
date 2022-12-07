package day7

import java.io.File

val fileSizes = mutableMapOf<String, Long>()

fun main() {
    val input = File("./resources/day7").readLines()
    input.explore()
    println("The sum of the files under 100000 is ${fileSizes.filter { (_, value) -> value <= 100000 }.values.sum()}")
}

fun String.addFile(size: Int) {
    fileSizes[""] = fileSizes[""]!! + size
    split("/").reduce { acc, s ->
        val currentDirectory = "$acc/$s"
        fileSizes[currentDirectory] = fileSizes[currentDirectory]!! + size
        currentDirectory
    }
}

fun List<String>.explore() {
    var currentDirectory = ""
    fileSizes[currentDirectory] = 0
    for (i in indices) {
        when {
            this[i].contains("$ cd") -> {
                when(val path = Regex("^\\$ cd (.+)").find(this[i])!!.destructured.component1()) {
                    ".." -> { currentDirectory = currentDirectory.replace(Regex("/[a-z]+$"), "") }
                    "/" -> { currentDirectory = "" }
                    else -> {
                        currentDirectory += "/$path"
                        fileSizes[currentDirectory] = 0
                    }
                }
            }
            this[i].contains("$ ls") || this[i].contains("dir ") -> continue
            else -> {
                val match = Regex("(\\d+) .+").find(this[i])?.destructured?.component1()
                if (match != null) currentDirectory.addFile(match.toInt())
            }
        }
    }
}

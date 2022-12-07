package day7

import java.io.File

fun main() {
    val input = File("./resources/day7").readLines()
    input.explore()
    println("The sum of the files under 100000 is ${fileSizes.filter { (_, value) -> value <= 100000 }.values.sum()}")

    val totalAvailableSpace = 70000000
    val unusedSpace = totalAvailableSpace - fileSizes[""]!!
    val neededSpace = 30000000
    println("The smallest directory to be removed is ${fileSizes.filter { (_, size) -> size >= neededSpace - unusedSpace}.values.min()}")
}

package day1

import java.io.File

fun main() {
    val elves = mutableListOf<Int>()
    var i = 0

    File("./resources/day1")
        .useLines { lineSequence ->
            lineSequence.forEach { line ->
                if (line.isBlank()) i++
                else {
                    if (elves.lastIndex < i) elves.add(line.toInt())
                    else elves[i] += line.toInt()
                }
            }
        }


    println("Highest count is: ${elves.totalOfThreeHighest()}")
}

private fun List<Int>.totalOfThreeHighest(): Int {
    val sortedList = sortedDescending()
    return sortedList[0] + sortedList[1] + sortedList[2]
}

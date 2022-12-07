package day6

import java.io.File

fun main() {
    val input = File("./resources/day6").readText()
    var buffer = mutableListOf<Char>()
    val markerCount = 14 //Replace by 4 for the first part
    for (i in 0 until markerCount-1) buffer.add(input[i])
    for (i in markerCount-1..input.lastIndex) {
        buffer.add(input[i])
        if (buffer.none { c -> buffer.count { c2 -> c2 == c } > 1 }) {
            println("first marker after character ${i+1}")
            return
        }
        buffer = buffer.toList().drop(1).toMutableList()
    }
}
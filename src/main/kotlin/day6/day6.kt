package day6

import java.io.File

fun main() {
    val reader = File("./resources/day6").reader()

    var buffer = mutableListOf<Char>()
    val markerCount = 14 //Replace by 4 for the first part

    var i = 0
    while (reader.ready()) {
        buffer.add(reader.read().toChar())
        if (i >= markerCount - 1) {
            if (buffer.none { c -> buffer.count { c2 -> c2 == c } > 1 }) {
                println("first marker after character ${i + 1}")
                return
            }
            buffer = buffer.drop(1).toMutableList()
        }
        i++
    }
}
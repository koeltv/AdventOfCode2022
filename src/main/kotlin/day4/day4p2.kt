package day4

import java.io.File

fun main() {
    val pairs = File("./resources/day4")
        .useLines { lineSequence ->
            lineSequence.map { line ->
                Regex("(\\d+)-(\\d+),(\\d+)-(\\d+)").find(line)!!
                    .destructured
                    .let { (firstRangeStart, firstRangeEnd, secondRangeStart, secondRangeEnd) ->
                        firstRangeStart.toInt()..firstRangeEnd.toInt() to secondRangeStart.toInt()..secondRangeEnd.toInt()
                    }
            }.toList()
        }

    val nbOfContainedRanges = pairs.count { (first, second) ->
        first.partiallyContains(second) || second.partiallyContains(first)
    }
    println("The number of partially contained ranges is $nbOfContainedRanges")
}

private fun IntRange.partiallyContains(range: IntRange): Boolean {
    return range.any { integer -> integer in this }
}
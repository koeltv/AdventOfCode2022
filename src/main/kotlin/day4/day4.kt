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
        first in second || second in first
    }
    println("The number of fully contained ranges is $nbOfContainedRanges")
}

private operator fun IntRange.contains(range: IntRange): Boolean {
    return range.all { integer -> integer in this }
}
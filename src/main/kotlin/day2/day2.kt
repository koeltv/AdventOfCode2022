package day2

import java.io.File

fun main() {
    val score = File("./resources/day2")
        .useLines { lineSequence ->
            lineSequence.fold(0) { acc, line ->
                Regex("(\\w) (\\w)").find(line)?.destructured?.let { (left, right) ->
                    var temp = acc
                    temp += when (right) {
                        "X" -> 1 //Rock     A
                        "Y" -> 2 //Paper    B
                        "Z" -> 3 //Scissors C
                        else -> error("Unknown action")
                    }
                    temp += expectedActionScore(left[0], right[0])
                    println("${name(line)}: $temp")
                    temp
                } ?: 0
            }
        }

    println("Total score is $score")
}

private fun expectedActionScore(left: Char, right: Char): Int {
    val relativeRight = right - 23
    return if (relativeRight compareTo left == 0) 3
    else {
        if (relativeRight == 'C' && left == 'B') 6
        else if (relativeRight == 'B' && left == 'A') 6
        else if (relativeRight == 'A' && left == 'C') 6
        else 0
    }
}

private fun name(string: String) = string
    .replace(Regex("[AX]"), "Rock")
    .replace(Regex("[BY]"), "Paper")
    .replace(Regex("[CZ]"), "Scissors")
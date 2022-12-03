package day2

import java.io.File

fun main() {
    val score = File("./resources/day2")
        .useLines { lineSequence ->
            lineSequence.fold(0) { acc, line ->
                Regex("(\\w) (\\w)").find(line)?.destructured?.let { (left, right) ->
                    var temp = acc
                    temp += when (right) {
                        "X" -> 0
                        "Y" -> 3
                        "Z" -> 6
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
    val chosenSymbol = if (right == 'Y') left else {
        val relativeRight = if (right == 'X') left - 1 else left + 1
        if (relativeRight < 'A') 'C' else if (relativeRight > 'C') 'A' else relativeRight
    }

    return when(chosenSymbol) {
        'A' -> 1
        'B' -> 2
        'C' -> 3
        else -> error("Unknown symbol $chosenSymbol")
    }
}

private val charToText = mapOf(
    "X" to "Lose",
    "Y" to "Draw",
    "Z" to "Win",
    "A" to "Rock",
    "B" to "Paper",
    "C" to "Scissors"
)

private fun name(string: String) = charToText.toList().fold(string) { acc, (char, string) ->
    acc.replace(char, string)
}
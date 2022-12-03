package day3

import java.io.File

fun main() {
    val priorities = File("./resources/day3")
        .useLines { lineSequence ->
            lineSequence.map { line ->
                val firstPart = line.substring(0, line.length/2)
                val secondPart = line.substring(line.length/2)

                val matchingChar = firstPart.find { c -> c in secondPart }!!

                if (matchingChar in 'a'..'z') matchingChar.code - 96
                else matchingChar.code - 38
            }.toList()
        }
    println("Sum of priorities is ${priorities.sum()}")
}
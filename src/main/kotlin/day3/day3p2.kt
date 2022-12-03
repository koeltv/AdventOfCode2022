package day3

import java.io.File

fun main() {
    val lines = File("./resources/day3").readLines()
    val groups = mutableListOf<List<String>>()
    for (i in lines.indices step 3) {
        groups.add(listOf(lines[i], lines[i+1], lines[i+2]))
    }

    val badges = groups.map { (first, second, third) ->
        val matchingChar = first.find { item -> item in second && item in third }!!

        if (matchingChar in 'a'..'z') matchingChar.code - 96
        else matchingChar.code - 38
    }

    println("Sum of priorities is ${badges.sum()}")
}
package day5

import java.io.File
import java.util.*

fun main() {
    val (schema, commands) = File("./resources/day5").readLines().let {
        val schema = it.filter { line -> line.contains(Regex("\\[[A-Z]]")) }
        val commands = it - schema.toSet()
        schema to commands
    }

    val stacks = Array(commands
        .first { it.matches(Regex("[ \\d]+")) }
        .count { c -> c.digitToIntOrNull() != null }
    ) { Stack<Char>() }

    schema.forEach { line ->
        for ((columnIndex, i) in (1..line.lastIndex step 4).withIndex()) {
            if (line[i] != ' ') stacks[columnIndex].push(line[i])
        }
    }

    stacks.map { stack -> stack.reverse() }
    stacks.print()

    commands.subList(2, commands.size).forEach { command ->
        val (quantity, source, destination) = Regex("move (\\d+) from (\\d+) to (\\d+)").find(command)!!.destructured
        for (i in 1..quantity.toInt()) {
            stacks[destination.toInt()-1].push(stacks[source.toInt()-1].pop())
        }
    }

    stacks.print()
    println("The top containers are ${stacks.map{ stack -> stack.lastOrNull() ?: "§" }.joinToString("")}")
}

fun Array<Stack<Char>>.print() {
    val maxIndex = this.maxOf { stack -> stack.lastIndex }
    for (j in maxIndex downTo 0) {
        for (i in this.indices) {
            val element = this[i].elementAtOrNull(j)
            if (element != null) {
                print("[$element] ")
            } else {
                print("    ")
            }
        }
        println()
    }
    this.forEachIndexed { index, _ -> print(" ${index+1}  ") }
    println()
}
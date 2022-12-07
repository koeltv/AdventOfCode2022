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
        val middlePoint = Stack<Char>()
        for (i in 1..quantity.toInt()) {
            middlePoint.push(stacks[source.toInt()-1].pop())
        }
        for (i in 1..quantity.toInt()) {
            stacks[destination.toInt()-1].push(middlePoint.pop())
        }
    }

    stacks.print()
    println("The top containers are ${stacks.map{ stack -> stack.lastOrNull() ?: "§" }.joinToString("")}")
}
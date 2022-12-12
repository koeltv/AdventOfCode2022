package day10

import java.io.File

class CRT {
    private val pixels = MutableList(240) { ' ' }

    fun draw(cycleCount: Int, spritePosition: Int) {
        val crtPosition = cycleCount - 1

        if ((crtPosition % 40).isAroundBy(spritePosition, 1)) {
            pixels[crtPosition] = '#'
        }
    }

    override fun toString(): String {
        return pixels.subListsOfSize(40).joinToString("\n") { list -> list.joinToString("") }
    }
}

private fun Int.isAroundBy(i1: Int, margin: Int) = this in i1-margin..i1+margin

private fun <E> List<E>.subListsOfSize(subListSize: Int): List<List<E>> {
    val subLists = mutableListOf<List<E>>()
    var index = subListSize
    while (index <= size) {
        subLists += subList(index - subListSize, index)
        index += subListSize
    }
    return subLists
}

fun main() {
    val program = Program()
    val crt = CRT()
    File("./resources/day10").forEachLine { line -> program.addInstruction(line) }

    var cycleCount = 1; var register = 1
    while (program.hasNext()) {
        crt.draw(cycleCount, register)
        val currentState = program.execute()
        cycleCount = currentState.first
        register = currentState.second
    }
    println(crt)
}

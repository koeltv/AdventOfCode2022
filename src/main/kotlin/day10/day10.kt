package day10

import java.io.File

class Program {
    private val instructionBuffer = ArrayDeque<String>()

    private var cycleCount: Int = 1
    private var register: Int = 1

    private var currentInstructionCounted = false

    fun addInstruction(instruction: String) {
        instructionBuffer += instruction
    }

    fun execute(): Pair<Int, Int> {
        Regex("(noop|addx)( (-?\\d+))?").matchEntire(instructionBuffer.first())!!.destructured.let { (instruction, _, count) ->
            when(instruction) {
                "noop" -> {
                    instructionBuffer.removeFirst()
                }
                "addx" -> {
                    if (!currentInstructionCounted) {
                        currentInstructionCounted = true
                    } else {
                        currentInstructionCounted = false
                        instructionBuffer.removeFirst()
                        register += count.toInt()
                    }
                }
                else -> error("Instruction unknown")
            }
            cycleCount++
        }
        return cycleCount to register
    }

    fun hasNext() = instructionBuffer.isNotEmpty()
}

fun main() {
    val program = Program()
    File("./resources/day10").forEachLine { line -> program.addInstruction(line) }

    var count = 0
    while (program.hasNext()) {
        val (cycleCount, register) = program.execute()

        if (cycleCount in listOf(20, 60, 100, 140, 180, 220)) {
            val signalStrength = cycleCount * register
            println("$cycleCount: $signalStrength")
            count += signalStrength
        }
    }
    println(count)
}

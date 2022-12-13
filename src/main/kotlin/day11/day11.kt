package day11

import java.io.File
import kotlin.math.absoluteValue

val monkeys = File("./resources/day11").readLines().windowed(6, 7).map { Monkey.parse(it) }

open class Monkey private constructor(
    private val items: MutableList<Long>,
    private val operation: List<String>,
    private val divisionFactor: Int,
    private var references: Pair<Int, Int>
) {
    var inspectedItemsCount = 0L

    private fun throws(item: Long, monkey: Monkey) {
        monkey.items.add(item)
    }

    fun playTurn() {
        while (items.isNotEmpty()) {
            inspectedItemsCount++
            val itemWorryLevel = items.removeFirst()
            val new = applyOperationTo(itemWorryLevel) % commonModulus(monkeys)
            val receiverIndex = if (new % divisionFactor == 0L) references.first else references.second
            throws(new, monkeys[receiverIndex])
        }
    }

    private fun applyOperationTo(old: Long): Long {
        val leftOperand = when(val value = operation[0]) {
            "old" -> old
            else -> value.toLong()
        }
        val rightOperand = when(val value = operation[2]) {
            "old" -> old
            else -> value.toLong()
        }
        return when(operation[1]) {
            "*" -> leftOperand * rightOperand
            "+" -> leftOperand + rightOperand
            else -> error("Unknown operation: ${operation[1]}")
        }
    }

    companion object {
        fun parse(block: List<String>): Monkey {
            val startingItems =
                Regex("(\\d+)").findAll(block[1]).map { s -> s.destructured.component1().toLong() }.toMutableList()
            val factor = Regex("new = (\\w+) (.) (\\w+)").find(block[2])!!.destructured.toList()
            val divisionFactor = block[3].findInt()

            val trueIndex = block[4].findInt()
            val falseIndex = block[5].findInt()

            return Monkey(startingItems, factor, divisionFactor, trueIndex to falseIndex)
        }

        private fun commonModulus(monkeys: List<Monkey>): Long {
            return monkeys.map { it.divisionFactor }.leastCommonMultiple()
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// Math functions
///////////////////////////////////////////////////////////////////////////

fun greatestCommonDiviser(i1: Long, i2: Long): Long = if (i2 == 0L) i1.absoluteValue else greatestCommonDiviser(i2, i1 % i2)

fun leastCommonMultiple(i1: Long, i2: Long) = (i1 * i2) / greatestCommonDiviser(i1, i2)

fun Iterable<Int>.leastCommonMultiple(): Long = map { it.toLong() }.reduce(::leastCommonMultiple)

///////////////////////////////////////////////////////////////////////////
// Main
///////////////////////////////////////////////////////////////////////////

fun String.findInt() = Regex("(\\d+)").find(this)!!.destructured.component1().toInt()

fun main() {
    repeat(10000) {
        monkeys.forEach { it.playTurn() }
    }

    val sortedInspectionCounts = monkeys.map { monkey -> monkey.inspectedItemsCount }.sortedDescending()
    println(sortedInspectionCounts[0] * sortedInspectionCounts[1])
}
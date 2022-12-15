package day12

import java.io.File
import java.util.*

typealias Coordinates = Pair<Int, Int>

class Terrain<T : Comparable<T>>(private val nodes: List<List<Node<T>>>) {
    private val horizontalBounds = nodes.first().indices
    private val verticalBounds = nodes.indices

    private val visitedNodes = mutableSetOf<Node<T>>()

    data class Node<T : Comparable<T>>(val x: Int, val y: Int, val char: Char, val height: T) : Comparable<Node<T>> {
        val adjacentCoordinates = listOf(
            x - 1 to y,
            x + 1 to y,
            x to y - 1,
            x to y + 1
        )

        companion object {
            fun <T : Comparable<T>> from(x: Int, y: Int, char: Char, height: T) = Node(x, y, char, height)
        }

        override fun compareTo(other: Node<T>): Int {
            return compareValues(this.height, other.height)
        }
    }

    private data class Step<T : Comparable<T>>(val node: Node<T>, val distance: Int = 0) {
        fun towards(node: Node<T>) = Step(node, distance + 1)
    }

    fun shortestDistance(
        isStartingNode: (Node<T>) -> Boolean,
        isEndNode: (Node<T>) -> Boolean,
        filteringCondition: (Node<T>, Node<T>) -> Boolean
    ): Int? {
        val startingPoint = nodes.flatten().first(isStartingNode)
        val endPoint = nodes.flatten().first(isEndNode)
        visitedNodes.clear()

        val steps: Queue<Step<T>> = LinkedList<Step<T>>().apply { add(Step(startingPoint)) }

        while (steps.isNotEmpty()) {
            val step = steps.poll()
            if (step.node == endPoint) return step.distance

            step.node.adjacentCoordinates
                .mapNotNull(::getPlot)
                .filter { filteringCondition(it, step.node) }
                .filter { it !in visitedNodes }
                .forEach {
                    steps.add(step.towards(it))
                    visitedNodes.add(it)
                }
        }

        return null
    }

    private fun getPlot(coordinates: Coordinates) = coordinates
        .takeIf { it.withinBounds() }?.let { (x, y) -> nodes[y][x] }

    private fun Coordinates.withinBounds() = first in horizontalBounds && second in verticalBounds

    companion object {
        fun <T : Comparable<T>> parse(input: List<Iterable<Char>>, extraction: (Char) -> T): Terrain<T> {
            return input.mapIndexed { y, row ->
                row.mapIndexed { x, char ->
                    Node.from(x, y, char, extraction(char))
                }
            }.let(::Terrain)
        }
    }

    override fun toString(): String {
        return nodes.map { nodeRow ->
            nodeRow.map { node ->
                if (node in visitedNodes) '#'
                else ' '
            }
        }.joinToString("\n") { it.joinToString("") }
    }

    fun findAll(predicate: (Node<T>) -> Boolean): List<Node<T>> {
        return nodes.flatten().filter { predicate(it) }
    }
}

fun main() {
    val terrain = Terrain.parse(File("./resources/day12").readLines().map { it.toList() }) {
        when (it) {
            'S' -> 0
            'E' -> 'z' - 'a'
            else -> it - 'a'
        }
    }

    println(terrain.shortestDistance({ it.char == 'S' }, { it.char == 'E' }, { node, sourceNode ->
        node.height - sourceNode.height <= 1
    }))
    println(terrain)

    val min = terrain.findAll { it.char == 'a' || it.char == 'S' }.minOf { node ->
        terrain.shortestDistance({ it == node }, { it.char == 'E' }, { newNode, sourceNode ->
            newNode.height - sourceNode.height <= 1
        }) ?: Int.MAX_VALUE
    }
    println("Minimum steps: $min")
}
package day9

import java.awt.Point
import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun Point.distanceFrom(point: Point): Int {
    return abs(x - point.x) + abs(y - point.y)
}

val directions = listOf(
    Point(0, 0),
    Point(1, 1),
    Point(-1, -1),
    Point(1, -1),
    Point(-1, 1),
    Point(1, 0),
    Point(0, 1),
    Point(-1, 0),
    Point(0, -1)
)
val movements = mapOf(
    'R' to Point(1, 0),
    'L' to Point(-1, 0),
    'U' to Point(0, 1),
    'D' to Point(0, -1)
)

class Rope(private var head: Point = Point(0, 0), private var tail: Point = Point(0, 0)) {
    private val visitedSpots = mutableSetOf(tail)

    private fun unstable() = directions.none { direction -> tail + direction == head }

    fun move(move: Char, time: Int) {
        repeat(time) {
            head += movements[move]!!
            if (unstable()) {
                tail += directions
                    .filter { direction -> !Rope(head, tail + direction).unstable() }
                    .minBy { direction -> head.distanceFrom(tail + direction) }
                visitedSpots += tail
            }
        }
    }

    fun countVisitedSpots() = visitedSpots.count()

    override fun toString(): String {
        val lines = mutableListOf<String>()
        for (j in 0..maxOf(head.y, tail.y)) {
            val stringBuilder = StringBuilder()
            for (i in 0..max(head.x, tail.x)) {
                val c = when(Point(i, j)) {
                    head -> 'H'
                    tail -> 'T'
                    else -> '.'
                }
                stringBuilder.append("$c ")
            }
            lines.add(stringBuilder.toString())
        }
        return lines.reversed().joinToString("\n")
    }

    fun coverageMap(): String {
        val lines = mutableListOf<String>()
        for (j in 0..visitedSpots.maxOf { it.y }) {
            val stringBuilder = StringBuilder()
            for (i in 0..visitedSpots.maxOf { it.x }) {
                stringBuilder.append("${if (Point(i, j) in visitedSpots) '#' else '.'} ")
            }
            lines.add(stringBuilder.toString())
        }
        return lines.reversed().joinToString("\n")
    }
}

private operator fun Point.plus(point: Point): Point {
    return Point(x + point.x, y + point.y)
}

fun main() {
    val rope = Rope()

    println(rope)
    File("./resources/day9").forEachLine { line ->
        Regex("([a-zA-Z]) (\\d+)").find(line)?.destructured?.let { (movement, amount) ->
            rope.move(movement[0], amount.toInt())
            println("== $line ==")
            println(rope)
        }
    }

    println(rope.coverageMap())
    println("== RESULT ==")
    println(rope.countVisitedSpots())
}

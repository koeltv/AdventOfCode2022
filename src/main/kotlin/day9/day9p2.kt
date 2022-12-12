package day9

import java.awt.Point
import java.io.File

fun Point.isNextTo(point: Point) = directions.any { direction -> this + direction == point }

class Rope2(bodySize: Int = 10) {
    private val body: MutableList<Point> = MutableList(bodySize) { Point(0, 0) }

    private val visitedSpots = mutableSetOf(body[0])

    private fun unstable(index: Int) = !body[index].isNextTo(body[index-1])

    private fun move(index: Int) {
        if (unstable(index)) {
            body[index] += directions
                .filter { direction -> (direction + body[index]).isNextTo(body[index-1]) }
                .minBy { direction -> (direction + body[index]).distanceFrom(body[index-1]) }

            if (index == body.lastIndex) visitedSpots += body[index]
            else if (index < body.lastIndex) move(index+1)
        }
    }

    fun move(move: Char, time: Int) {
        repeat(time) {
            body[0] += movements[move]!!
            move(1)
        }
    }

    fun countVisitedSpots() = visitedSpots.count()

    override fun toString(): String {
        val lines = mutableListOf<String>()
        for (j in 0..body.maxOf{ knot -> knot.y }) {
            val stringBuilder = StringBuilder()
            for (i in 0..body.maxOf{ knot -> knot.x }) {
                val c = when (val point = Point(i, j)) {
                    body[0] -> 'H'
                    in body -> body.indexOf(point).digitToChar()
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
    val rope = Rope2()

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

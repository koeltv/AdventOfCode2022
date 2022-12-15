package day12

import java.awt.Point
import java.io.File

private object Map {
    private val data: List<String> = File("./resources/day12").readLines()
    private val startingPosition = findPoint('S')
    private val endPosition = findPoint('E')

    private fun findPoint(c: Char): Point {
        return data.withIndex().firstNotNullOf { (yIndex, line) ->
            val xIndex = line.indexOfFirst { char -> char == c }
            if (xIndex == -1) null
            else xIndex to yIndex
        }
    }

    private fun Itinerary.isCompleted() = positions.any { it == endPosition }

    fun findShortestItinerary(): Itinerary? {
        var paths = listOf(Itinerary(setOf(startingPosition)))

        do {
            //Parallel stream reduce time to 1/3 (1m30s to 30s)
            paths = paths
                .parallelStream()
                .flatMap { it.moveOnce().stream() }
                .toList()
        } while (paths.none { it.isCompleted() })

        return paths.firstOrNull { it.isCompleted() }
    }

    class Itinerary(val positions: Set<Point>) {
        companion object {
            private val possibleMoves = setOf(0 to -1, -1 to 0, 1 to 0, 0 to 1)
        }

        fun moveOnce(): List<Itinerary> = possibleMoves
            .parallelStream()
            .map { positions.last() to positions.last() + it }
            .filter { (oldPosition, newPosition) ->
                newPosition !in positions
                        && newPosition.y in data.indices
                        && newPosition.x in data[newPosition.y].indices
                        && relativeHeight(newPosition, oldPosition) <= 1
            }
            .map { (_, newPosition) -> Itinerary((positions + newPosition)) }
            .toList()

        private fun relativeHeight(p1: Point, p2: Point): Int {
            return height(data[p1.y][p1.x]) - height(data[p2.y][p2.x])
        }

        private fun height(c: Char) = when (c) {
            'S' -> 'a'.code
            'E' -> 'z'.code
            else -> c.code
        }
    }
}

private operator fun Point.plus(move: Point): Point {
    return this.x + move.x to this.y + move.y
}

private infix fun Int.to(that: Int): Point = Point(this, that)

fun main() {
    val path = Map.findShortestItinerary()!!

    println("shortest path has ${path.positions.count() - 1} steps")
}
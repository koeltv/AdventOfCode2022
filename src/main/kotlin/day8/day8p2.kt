package day8

import java.io.File

fun main() {
    val map = File("./resources/day8").readLines()
    val maxScenicScore = map.indices.maxOf { y ->
        map[y].indices.maxOf { x ->
            if (y == 0 || y == map.lastIndex || x == 0 || x == map[y].lastIndex) 0
            else {
                val horizontalAxis = map[y].map { c -> c.digitToInt() }
                val verticalAxis = map.map { line -> line[x].digitToInt() }
                val currentHeight = map[y][x].digitToInt()

                var currentScenicScore = 0

                currentScenicScore += horizontalAxis.subList(0, x).reversed().countVisibleInAxis(currentHeight)
                currentScenicScore *= horizontalAxis.subList(x+1, horizontalAxis.size).countVisibleInAxis(currentHeight)

                currentScenicScore *= verticalAxis.subList(0, y).reversed().countVisibleInAxis(currentHeight)
                currentScenicScore *= verticalAxis.subList(y+1, verticalAxis.size).countVisibleInAxis(currentHeight)

                currentScenicScore
            }
        }
    }
    println("Maximal scenic score = $maxScenicScore")
}

private fun List<Int>.countVisibleInAxis(currentHeight: Int): Int {
    var score = 0
    for (i in this.indices) {
        if (this[i] < currentHeight) score++
        else {
            score++
            break
        }
    }
    return score
}

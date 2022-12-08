package day8

import java.io.File

fun main() {
    val map = File("./resources/day8").readLines()
    val nbOfVisibleTrees = map.indices.sumOf { y ->
        map[y].indices.sumOf { x ->
            var currentVisibleTrees = 0
            if (y == 0 || y == map.lastIndex || x == 0 || x == map[y].lastIndex) currentVisibleTrees++
            else {
                val horizontalAxis = map[y].map { c -> c.digitToInt() }
                val verticalAxis = map.map { line -> line[x].digitToInt() }
                val currentHeight = map[y][x].digitToInt()

                if (horizontalAxis.subList(0, x).all { height -> height < currentHeight }
                    || horizontalAxis.subList(x+1, map[y].length).all { height -> height < currentHeight }
                    || verticalAxis.subList(0, y).all { height -> height < currentHeight }
                    || verticalAxis.subList(y+1, map.size).all { height -> height < currentHeight }
                ) {
                    currentVisibleTrees++
                }
            }
            currentVisibleTrees
        }
    }

    println("Number of visible trees = $nbOfVisibleTrees")
}

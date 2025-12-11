import kotlin.io.path.Path
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): MutableList<Pair<Long, Long>> {
        val parsed = mutableListOf<Pair<Long, Long>>()
        input.forEach {
            val line = it.split(",")
                .map { num -> num.toLong() }
            parsed.add(Pair(line[0], line[1]))
        }
        return parsed
    }

    fun part1(input: MutableList<Pair<Long, Long>>): Long {
        var result = 0L
        for ((i, j) in input.withIndex()) {
            for (k in i+1..<input.size) {
                val (x1, y1) = j
                val (x2, y2) = input[k]
                val area = (abs(x1 - x2)+1) * (abs(y1 - y2)+1)
                if (area > result) result = area
            }
        }
        return result
    }

    fun floodfill(grid: MutableList<MutableList<Char>>) {
        var startCoord = Pair(0, 0)
        outer@for ((i, row) in grid.withIndex()) {
            var foundWall = false
            for ((k, col) in row.withIndex()) {
                if (col == 'X') continue@outer
                if (col == 'O' && !foundWall) {
                    foundWall = true
                } else if (col == '.' && foundWall) {
                    startCoord = Pair(k, i)
                    break@outer
                }
            }
        }
        println(startCoord)
        val DIRECTIONS = listOf(Pair(-1, 0),
                                Pair(1, 0),
                                Pair(0, -1),
                                Pair(0, 1))

        val toFill = mutableListOf(startCoord)
        val filled = mutableListOf<Pair<Int, Int>>()
        while (toFill.isNotEmpty()) {
            val (x, y) = toFill.removeFirst()
            if (grid[y][x] == '.') grid[y][x] = 'O'
            for ((xOff, yOff) in DIRECTIONS) {
                val offsetX = x + xOff
                val offsetY = y + yOff
                if (offsetX in 0..<grid[0].size && offsetY in 0..<grid.size
                    && grid[offsetY][offsetX] == '.') toFill.add(Pair(offsetX, offsetY))
            }
        }
        grid.forEach { println(it) }
    }


    fun drawGrid(input: MutableList<Pair<Long, Long>>): MutableList<MutableList<Char>> {
        var maxX = 0L
        var maxY = 0L
        input.forEach {
            val (x, y) = it
            if (x+1 > maxX) maxX = x+1
            if (y+1 > maxY) maxY = y+1
        }
        val row = mutableListOf<Char>()
        // val grid = mutableListOf<MutableList<Char>>()
        repeat(maxX.toInt()+1) { row.add('.') }
        println("Gothere")
        println(maxY)
        var co = 0
        var grid = ArrayList<MutableList<Char>>(maxY.toInt())
        repeat(maxY.toInt()+1) { grid.add(row.toMutableList())
            println(co)
            co += 1
        }
        println("HEre as well")
        for ((no, item) in input.withIndex()) {
            val (x, y) = item
            grid[y.toInt()][x.toInt()] = 'X'
            val (x2, y2) =  if (no + 1 >= input.size) input.first() else input[no+1]
                when {
                    x < x2 -> for (i in x+1..x2) grid[y.toInt()][i.toInt()] = 'O'
                    x > x2 -> for (i in x2+1..x) grid[y.toInt()][i.toInt()] = 'O'
                    y < y2 -> for (i in y+1..y2) grid[i.toInt()][x.toInt()] = 'O'
                    y > y2 -> for (i in y2+1..y) grid[i.toInt()][x.toInt()] = 'O'
                }
            grid[y.toInt()][x.toInt()] = 'X'
        }
        grid.forEach { println(it) }

        // Flood fill with Os
        floodfill(grid)
        return grid
    }

    fun checkContent(grid: MutableList<MutableList<Char>>,
                     firstCoords: Pair<Int, Int>, secondCoords: Pair<Int, Int>): Boolean {
        val (x1, y1) = firstCoords
        val (x2, y2) = secondCoords
        val xRange = if (x1 <= x2) IntRange(x1, x2) else IntRange(x2, x1)
        val yRange = if (y1 <= y2) IntRange(y1, y2) else IntRange(y2, y1)
        for (y in yRange) {
            for (x in xRange) {
                if (grid[y][x] == '.') return false
            }
        }
        return true
    }
    /*
    fun part2(input: MutableList<Pair<Long, Long>>): Long {
        // This time, I probably have to draw the grid.
        val grid = drawGrid(input)
        println("Real grid")
        grid.forEach { println(it) }
        // Generate recangles and check if all are in the filled field
        var result = 0L
        for ((i,j) in input.withIndex()) {
            for (k in i+1..<input.size) {
                val (x1, y1) = j
                val (x2, y2) = input[k]
                val area = (abs(x1 - x2)+1) * (abs(y1 - y2)+1)
                if (area <= result) continue  // Save some time if it can't get bigger
                if (checkContent(grid, Pair(x1.toInt(), y1.toInt()),
                        Pair(x2.toInt(), y2.toInt()))) result = area
            }
            println(result)
        }

        return result
    }
    */
    fun checkOverlap(rangeList: MutableList<Pair<LongRange, LongRange>>,
                     xRange: LongRange, yRange: LongRange): Boolean {
        // Check top to bottom

        for (line in rangeList) {
            val (xLine, yLine) = line
            println("xLine: $xLine; yLine: $yLine")
            if (xLine.count() > 1) {

                val y = yLine.first

                println("Entered xLine, y= $y")
                println("xRange $xRange")
                println("yRange $yRange")
                // quer
                if ((xRange.first in xLine || xRange.last in xLine) &&
                            y in yRange) {
                    println("got one")
                    return true
                }
            } else {
                val x = xLine.first
                if (((yLine.first < yRange.first && yLine.last > yRange.first) ||
                            (yLine.first < yRange.last && yLine.last > yRange.last)) &&
                    (xRange.first < x && xRange.last > x)) {
                    return true
                }
            }
        }
        return false
    }

    fun part2(input: MutableList<Pair<Long, Long>>): Long {
        var result = 0L
        val longRangeList = mutableListOf<Pair<LongRange, LongRange>>()
        for ((i,j) in input.withIndex()) {
            if (i+1 < input.size) {
                val (x1, y1) = j
                val (x2, y2) = input[i+1]
                val xRange = if (x1 < x2) LongRange(x1, x2) else LongRange(x2, x1)
                val yRange = if (y1 < y2) LongRange(y1, y2) else LongRange(y2, y1)
                longRangeList.add(Pair(xRange, yRange))
            }
        }

        println("Range List generated")
        for ((i,j) in input.withIndex()) {
            for (k in i+1..<input.size) {
                val (x1, y1) = j
                val (x2, y2) = input[k]
                val area = (abs(x1 - x2)+1) * (abs(y1 - y2)+1)
                if (area <= result) continue  // Save some time if it can't get bigger
                // Check if any corner is present inside
                val xRange = if (x1 < x2) LongRange(x1, x2) else LongRange(x2, x1)
                val yRange = if (y1 < y2) LongRange(y1, y2) else LongRange(y2, y1)
                // Check if corner moves out
                if (checkOverlap(longRangeList, xRange, yRange)) {
                    println("I got excluded")
                    continue
                }
                result = area
                //val crossing = input.filter { it.first in xRange || it.second in yRange }

            //println(crossing)
            }
        }
        println(result)
        return result
    }

    val testRawInput = readInput("Day09_Test")
    val testInput = parseInput(testRawInput)

    check(part1(testInput) == 50L)

    val rawInput = readInput("Day09")
    val input = parseInput(rawInput)
    val timePart1 = measureTime { part1(input).println() }
    println("Part 1 took $timePart1.")
    part2(testInput)
    check(part2(testInput) == 24L)
    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")
}

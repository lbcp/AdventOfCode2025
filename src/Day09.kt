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
                }
            }
        }
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
                if (0..offsetX..grid[0].size)
            }
        }
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
        val grid = mutableListOf<MutableList<Char>>()
        repeat(maxX.toInt()+1) { row.add('.') }
        repeat(maxY.toInt()+1) { grid.add(row.toMutableList()) }

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
        return grid
    }

    fun part2(input: MutableList<Pair<Long, Long>>): Long {
        // This time, I probably have to draw the grid.
        val grid = drawGrid(input)


        var result = 0L
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
    /*
    check(part2(testInput) == 6)


    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")

     */
}

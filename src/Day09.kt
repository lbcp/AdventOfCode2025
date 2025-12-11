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

    fun checkOverlap(rangeList: MutableList<Pair<LongRange, LongRange>>,
                     xRange: LongRange, yRange: LongRange): Boolean {
        val newXRange = LongRange(xRange.first+1, xRange.last-1)
        val newYRange = LongRange(yRange.first+1, yRange.last-1)
        for (line in rangeList) {
            val (xLine, yLine) = line
            if (xLine.count() > 1) {
                val y = yLine.first
                if ((newXRange.first in xLine || newXRange.last in xLine) && y in newYRange) return true
            } else {
                val x = xLine.first
                if ((newYRange.first in yLine || newYRange.last in yLine) && x in newXRange) return true
            }
        }
        return false
    }

    fun generateBorders(input: MutableList<Pair<Long, Long>>): MutableList<Pair<LongRange, LongRange>> {
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
        val (x1, y1) = input.first()
        val (x2, y2) = input.last()
        val xRange = if (x1 < x2) LongRange(x1, x2) else LongRange(x2, x1)
        val yRange = if (y1 < y2) LongRange(y1, y2) else LongRange(y2, y1)
        longRangeList.add(Pair(xRange, yRange))

        return longRangeList
    }

    fun part2(input: MutableList<Pair<Long, Long>>): Long {
        var result = 0L
        val longRangeList = generateBorders(input)

        for ((i,j) in input.withIndex()) {
            for (k in i+1..<input.size) {
                val (x1, y1) = j
                val (x2, y2) = input[k]
                val area = (abs(x1 - x2)+1) * (abs(y1 - y2)+1)
                if (area <= result) continue

                val xRange = if (x1 < x2) LongRange(x1, x2) else LongRange(x2, x1)
                val yRange = if (y1 < y2) LongRange(y1, y2) else LongRange(y2, y1)
                if (checkOverlap(longRangeList, xRange, yRange)) {
                    continue
                }
                result = area
            }
        }
        return result
    }

    val testRawInput = readInput("Day09_Test")
    val testInput = parseInput(testRawInput)
    check(part1(testInput) == 50L)

    val rawInput = readInput("Day09")
    val input = parseInput(rawInput)
    repeat(5) {
        val timePart1 = measureTime { part1(input).println() }
        println("Part 1 took $timePart1.")
    }
    check(part2(testInput) == 24L)
    repeat(5) {
        val timePart2 = measureTime { part2(input).println() }
        println("Part 2 took $timePart2.")
    }
}

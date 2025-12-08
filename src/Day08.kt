import java.util.PriorityQueue
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): MutableList<List<Long>> {
        val coordinates = mutableListOf<List<Long>>()
        input.forEach { line ->
            coordinates.add(line.split(",")
                .map { it.toLong() })
        }

        return coordinates
    }

    fun part1(input: MutableList<List<Long>>): Long {
        var result = 0L
        // Test to find shortes route
        val prioQueue = PriorityQueue<Double>()
        val connMap = mutableMapOf<Double, Pair<List<Long>, List<Long>>>()
        for ((no1, i) in input.withIndex()) {
            for (j in no1 + 1..<input.size) {
                if (i == input[j]) continue
                val dist = sqrt((i[0].toDouble() - input[j][0]).pow(2) +
                        (i[1].toDouble() - input[j][1]).pow(2) +
                        (i[2].toDouble() - input[j][2]).pow(2))
                prioQueue.add(dist)
                connMap[dist] = Pair(i, input[j])
            }
        }
        var circuits = mutableListOf< MutableSet<List<Long>>>()
        addLoop@repeat(10) {
            val next = prioQueue.poll()
            val (x, y) = connMap[next]!!
            var timesAdded = 0
            for (c in circuits) {
                if (x in c || y in c) {
                    c.add(x)
                    c.add(y)
                    timesAdded += 1
                }
            }
            if (timesAdded == 0) {
                circuits.add(mutableSetOf(x, y))
            }
            if (timesAdded > 1) println("I was added multiple times ${connMap[next]}")
        }
        // I also must merge them sets in the end
        var finalSets = mutableListOf< MutableSet<List<Long>>>()
        return result
    }

    fun part2(input: MutableList<Pair<Char, Int>>): Int {
        var result = 0
        return result
    }

    val testRawInput = readInput("Day08_Test")
    val testInput = parseInput(testRawInput)
    part1(testInput)
    /*
    val rawInput = readInput("Day08")
    val input = parseInput(rawInput)
    part1(input)
    //check(part1(testInput) == 40L)


     */
    /*
    check(part2(testInput) == 6)



    val timePart1 = measureTime { part1(input).println() }
    println("Part 1 took $timePart1.")
    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")

     */
}

import java.util.PriorityQueue
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

    fun mergeAllOverlapping(circuits: MutableSet<MutableSet<List<Long>>>): MutableSet<MutableSet<List<Long>>> {
        val remaining = circuits.toMutableSet()
        val result = mutableSetOf<MutableSet<List<Long>>>()
        while (remaining.isNotEmpty()) {
            val start = remaining.first()
            remaining.remove(start)
            val merged = start.toMutableSet()
            var changed: Boolean

            do {
                changed = false
                val it = remaining.iterator()
                while (it.hasNext()) {
                    val next = it.next()
                    if (next.any { it in merged }) {
                        merged.addAll(next)
                        it.remove()
                        changed = true
                    }
                }
            } while (changed)
            result.add(merged)
        }
        return result
    }


    fun part1(input: MutableList<List<Long>>, times: Int = 1000): Long {
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
        val circuits = mutableSetOf<MutableSet<List<Long>>>()
        repeat(times) {
            val next = prioQueue.poll()
            val (x, y) = connMap[next]!!
            val overlapping = circuits.filter { x in it || y in it }.toMutableSet()

            when (overlapping.size) {
                0 -> circuits.add(mutableSetOf(x, y))
                1 -> {
                    val c = overlapping.first()
                    c.add(x)
                    c.add(y)
                }
                else -> {
                    val merged = overlapping
                        .flatten()
                        .toMutableSet()
                        .apply {
                            add(x)
                            add(y)
                        }
                    circuits.removeAll(overlapping)
                    circuits.add(merged)
                }
            }
        }
        val new = mergeAllOverlapping(circuits)
        val sortedList = new.sortedBy { it.size }
        var result = 1L
        for (i in sortedList.size-1 downTo sortedList.size-3) result *= sortedList[i].size
        return result
    }

    fun part2(input: MutableList<List<Long>>): Long {
        val nodesCount = input.size
        println(nodesCount)
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
        var circuits = mutableSetOf<MutableSet<List<Long>>>()
        var lastPair: Pair<List<Long>, List<Long>>
        do {
            val next = prioQueue.poll()
            val (x, y) = connMap[next]!!
            val overlapping = circuits.filter { x in it || y in it }.toMutableSet()

            when (overlapping.size) {
                0 -> circuits.add(mutableSetOf(x, y))
                1 -> {
                    val c = overlapping.first()
                    c.add(x)
                    c.add(y)
                }
                else -> {
                    val merged = overlapping
                        .flatten()
                        .toMutableSet()
                        .apply {
                            add(x)
                            add(y)
                        }
                    circuits.removeAll(overlapping)
                    circuits.add(merged)
                }
            }
            circuits = mergeAllOverlapping(circuits)
            lastPair = Pair(x, y)
        } while (circuits.first().size < nodesCount)
        return lastPair.first[0] * lastPair.second[0]
    }

    val testRawInput = readInput("Day08_Test")
    val testInput = parseInput(testRawInput)
    check(part1(testInput, 10) == 40L)

    val rawInput = readInput("Day08")
    val input = parseInput(rawInput)
    repeat(5) {
        val timePart1 = measureTime { part1(input).println() }
        println("Part 1 took $timePart1.")
    }
    check(part2(testInput) == 25272L)
    repeat(5) {
        val timePart2 = measureTime { part2(input).println() }
        println("Part 2 took $timePart2.")
    }
}

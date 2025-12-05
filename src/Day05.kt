import kotlin.math.max
import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): Pair<MutableList<Pair<Long, Long>>, List<Long>> {
        var ranges = true
        val fruits = mutableListOf<Long>()
        val fresh = mutableListOf<Pair<Long, Long>>()
        input.forEach {
            if (it.isEmpty()) ranges = false
            else if (ranges) {
                val range = it.split("-").map { no -> no.toLong() }
                fresh.add(Pair(range[0], range[1]))
            } else {
                fruits.add(it.toLong())
            }
        }

        return Pair(fresh,fruits.toList())
    }

    fun part1(input: Pair<MutableList<Pair<Long, Long>>, List<Long>>): Int {
        var result = 0
        val (fresh, food) = input
        foodLoop@ for (f in food) {
            fresh.forEach {
                if (f in it.first..it.second) {
                    result += 1
                    continue@foodLoop
                }
            }
        }

        return result
    }

    fun part2(input: Pair<MutableList<Pair<Long, Long>>, List<Long>>): Long {
        val (ranges, _) = input
        var sorted = ranges.sortedWith( compareBy( {it.first}, { it.second } ))
        var changed = true

        while (changed) {
            changed = false
            val collectorList = mutableListOf<Pair<Long, Long>>()
            var lastChanged = false

            for (i in 0..sorted.size - 2) {
                if (sorted[i].second >= sorted[i + 1].first) {
                    changed = true
                    lastChanged = true
                    collectorList.add(Pair(sorted[i].first,
                            max(sorted[i+1].second, sorted[i].second)))
                } else if (!lastChanged) {
                    collectorList.add(sorted[i])
                    if (i == sorted.size - 2) collectorList.add(sorted[i+1])
                } else  lastChanged = false
            }
            sorted = collectorList.sortedWith( compareBy(
                {it.first}, { it.second } ))
        }
        var result = 0L
        sorted.forEach {
            result += (it.second - it.first) + 1
        }

        return result
    }

    val testRawInput = readInput("Day05_Test2")
    val testInput = parseInput(testRawInput)
    check(part1(testInput) == 3)
    check(part2(testInput) == 27L)

    val rawInput = readInput("Day05")
    val input = parseInput(rawInput)
    val timePart1 = measureTime { part1(input).println() }
    println("Part 1 took $timePart1.")
    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")
}

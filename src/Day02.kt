import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): MutableList<Pair<Long, Long>> {
        val parsed = mutableListOf<Pair<Long, Long>>()
        input.forEach { line ->
            line.split(",")
                .forEach {
                    parsed.add(Pair(it.split("-").component1().toLong(),
                                    it.split("-").component2().toLong()))
                }
        }
        return parsed
    }

    fun part1(input: MutableList<Pair<Long, Long>>): Long {
        var result = 0L
        for (i in input) {
            for (n in i.first..i.second) {
                val no = n.toString()
                if (no.length % 2 != 0) {
                    continue
                }
                val firstHalf = no.substring(0 until no.length / 2)
                val secondHalf = no.substring(no.length / 2)
                if (firstHalf == secondHalf) {
                    result += n
                }
            }
        }
        return result
    }

    fun part2(input: MutableList<Pair<Long, Long>>): Long {
        var result = 0L
        // My idea here is to generate a set of substrings. If a set is only size 1 it is identical.
        // Not sure if this saves anything though...
        for (i in input) {
            midLoop@ for (n in i.first..i.second) {
                val no = n.toString()
                val maxSubSize = no.length / 2
                for (len in 1 .. maxSubSize) {
                    val chunks = no.chunked(len).toSet()
                    if (chunks.size == 1) {
                        result += n
                        continue@midLoop
                    }
                }
            }
        }
        return result
    }

    val taskFile = readInput("Day02_Test")
    val testInput = parseInput(taskFile)
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265L)

    val input = readInput("Day02")
    val timePart1 = measureTime { part1(parseInput(input)).println() }
    println("Part 1 took $timePart1.")
    val timePart2 = measureTime { part2(parseInput(input)).println() }
    println("Part 2 took $timePart2.")
}

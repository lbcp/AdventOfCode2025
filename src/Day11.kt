import kotlin.time.measureTime
import arrow.core.MemoizedDeepRecursiveFunction

fun main() {
    fun parseInput(input: List<String>): MutableMap<String, List<String>> {
        val resultMap = mutableMapOf<String, List<String>>()
        input.forEach { line ->
            val (key, value) = line.split(":")
            resultMap[key] = value.trimStart().split(" ")
        }
        return resultMap
    }

    fun walk(input: MutableMap<String, List<String>>, nextNode: String) : Int {
        var result = 0
        if ("out" in input[nextNode]!!) return 1
        for (d in input[nextNode]!!) {
            result += walk(input, d)
        }
        return result
    }

    fun part1(input: MutableMap<String, List<String>>): Int {
        var result = 0
        for (d in input["you"]!!) result += walk(input, d)
        return result
    }

    fun part2(input: MutableMap<String, List<String>>): Long {
        var result = 0L
        val walker = MemoizedDeepRecursiveFunction<Pair<String, String>, Int> { (start, end) ->
            result = 0
            when (start) {
                end -> 1
                in input -> input.getValue(start).sumOf { child ->
                    callRecursive(Pair(child, end))
                }
                else -> 0
            }
        }
        println("From server To FFT: ${walker(Pair("svr", "fft"))}")
        println("From fft to DAC: ${walker(Pair("fft", "dac"))}")
        println("From DAC to out: ${walker(Pair("dac", "out"))}")
        result += walker(Pair("svr", "fft")).toLong() * walker(Pair("fft", "dac")) * walker(Pair("dac", "out"))

        return result
    }

    val testRawInput = readInput("Day11_Test")
    val testInput = parseInput(testRawInput)

    check(part1(testInput) == 5)
    val rawInput = readInput("Day11")
    val input = parseInput(rawInput)
    val timePart1 = measureTime { part1(input).println() }
    println("Part 1 took $timePart1.")

    val testRawInput2 = readInput("Day11_Test2")
    val testInput2 = parseInput(testRawInput2)
    check(part2(testInput2) == 2L)
    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")
}

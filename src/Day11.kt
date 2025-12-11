import kotlin.time.measureTime

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
        if ("out" in input[nextNode]!!) return result + 1
        for (d in input[nextNode]!!) {
            result += walk(input, d)
        }
        return result
    }

    fun walk2(input: MutableMap<String, List<String>>, nextNode: String, visited: MutableList<String>) : Int {
        var result = 0
        if ("out" in input[nextNode]!!) {
            if ("fft" in visited && "dac" in visited) return result + 1
            else return 0
        }
        for (d in input[nextNode]!!) {
            if (d in visited) continue
            val updated = visited.toMutableList()
            updated.add(d)
            result += walk2(input, d, updated)
        }
        return result
    }

    fun part1(input: MutableMap<String, List<String>>): Int {
        var result = 0
        for (d in input["you"]!!) result += walk(input, d)
        return result
    }

    fun part2(input: MutableMap<String, List<String>>): Int {
        var result = 0
        for (d in input["svr"]!!) result += walk2(input, d, mutableListOf("svr"))
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
    check(part2(testInput2) == 2)
    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")
}

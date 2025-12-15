import arrow.core.split
import kotlin.math.pow
import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): MutableList<Triple<List<Char>, List<List<Int>>, List<Int>>> {
        val parsed = mutableListOf<Triple<List<Char>, List<List<Int>>, List<Int>>>()
        input.forEach {
            val levers = """\s.+\s""".toRegex()
            val endLayout = """\[.+]""".toRegex()
            val joltage = """\{.+}""".toRegex()

            val layout =  endLayout.find(it)!!.value
                .replace("[", "")
                .replace("]", "")
            val extractedLevers = levers.find(it)!!.value.trim()
                .replace("(", "")
                .replace(")", "")
                .split(" ")
            val jolts = joltage.find(it)!!.value
                .replace("{", "")
                .replace("}", "")
                .split(",")
                .map { nums -> nums.toInt() }

            val leversAsNumbers = mutableListOf<Double>()

            for (parts in extractedLevers) {
                var foo = 0.0
                val temp = parts.split(",").map { num -> num.toInt() }
                for ((i, j) in temp.withIndex()) {
                    foo += 2.0.toFloat().pow(layout.length-1-j)
                }
                leversAsNumbers.add(foo)
                println(foo.toLong().toString(2).padStart(layout.length, '0'))
            }

            println(leversAsNumbers)

        }

        return parsed
    }

    fun part1(input: MutableList<Pair<Char, Int>>): Int {
        var result = 0
        return result
    }

    fun part2(input: MutableList<Pair<Char, Int>>): Int {
        var result = 0
        return result
    }

    val testRawInput = readInput("Day10_Test")
    val testInput = parseInput(testRawInput)

    // check(part1(testInput) == 3)

    /*
    check(part2(testInput) == 6)

    val rawInput = readInput("Day02")
    val input = parseInput(rawInput)
    val timePart1 = measureTime { part1(input).println() }
    println("Part 1 took $timePart1.")
    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")

     */
}

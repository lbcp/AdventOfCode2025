import arrow.core.split
import kotlin.math.pow
import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): MutableList<Triple<Int, List<Int>, List<Int>>> {
        val parsed = mutableListOf<Triple<Int, List<Int>, List<Int>>>()
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

            val leversAsNumbers = mutableListOf<Int>()

            for (parts in extractedLevers) {
                var foo = 0.0
                val temp = parts.split(",").map { num -> num.toInt() }
                for (j in temp) {
                    foo += 2.0.toFloat().pow(layout.length-1-j)
                }
                leversAsNumbers.add(foo.toInt())
            }
            var finalNumber = 0.0
            for ((i, j) in layout.reversed().withIndex()) {
                if (j == '#') finalNumber += 2.0.pow(i)
            }
            parsed.add(Triple(finalNumber.toInt(), leversAsNumbers, jolts))
        }
        return parsed
    }

    fun part1(input: MutableList<Triple<Int, List<Int>, List<Int>>>): Int {
        var result = 0
        input.forEach {
            val resultDict = mutableMapOf<Int, MutableList<Int>>(0 to mutableListOf<Int>())
            val (finalNumber, buttons, jolts) = it
            do {
                val tempDict = mutableMapOf<Int, MutableList<Int>>()
                for ((key, value) in resultDict) {
                    for (button in buttons) {
                        val newVal = key xor button
                        if (resultDict.containsKey(newVal)) {
                            continue
                        } else {
                            val temp = (value + button).toMutableList()
                            tempDict[newVal] = temp
                        }
                    }
                }
                resultDict += tempDict
            } while (!resultDict.containsKey(finalNumber))
            result += resultDict[finalNumber]!!.size
        }
        return result
    }

    fun part2(input: MutableList<Pair<Char, Int>>): Int {
        var result = 0
        return result
    }

    val testRawInput = readInput("Day10_Test")
    val testInput = parseInput(testRawInput)

    check(part1(testInput) == 7)

    val rawInput = readInput("Day10")
    val input = parseInput(rawInput)
    val timePart1 = measureTime { part1(input).println() }
    println("Part 1 took $timePart1.")

    /*
    check(part2(testInput) == 6)


    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")

     */
}

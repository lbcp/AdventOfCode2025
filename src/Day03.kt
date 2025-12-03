import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int {
        var result = 0
        input.forEach {
            var maxVal1 = 0
            var maxVal2 = 0
            for ((i ,j) in it.withIndex()) {
                if (j.digitToInt() > maxVal1 && i < it.length - 1) {
                    maxVal1 = j.digitToInt()
                    maxVal2 = 0
                } else if (j.digitToInt() > maxVal2) {
                    maxVal2 = j.digitToInt()
                }
            }
            result += (maxVal1 * 10) + maxVal2
        }
        return result
    }

    fun part2(input: List<String>): Long {
        var result = 0L
        input.forEach {
            var currentIndex = 0
            var lastIndex = 0
            var gapsLeft = 12
            val numbers = mutableListOf<Int>()
            while (gapsLeft > 0) {
                var maxVal = 0
                for ((i, j) in it.substring(currentIndex..it.length - gapsLeft).withIndex()) {
                    if (j.digitToInt() > maxVal) {
                        maxVal = j.digitToInt()
                        lastIndex = i
                    }
                }
                numbers.add(maxVal)
                gapsLeft -= 1
                currentIndex += lastIndex + 1
            }
            result += numbers.joinToString(separator = "").toLong()
        }
        return result
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day03_Test")
    check(part1(testInput) == 357)
    check(part2(testInput) == 3121910778619L)


    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    val timePart1 = measureTime { part1(input).println() }
    println("Part 1 took $timePart1.")

    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")
}

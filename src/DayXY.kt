import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): MutableList<Pair<Char, Int>> {
        return mutableListOf(Pair('A', 5))
    }

    fun part1(input: MutableList<Pair<Char, Int>>): Int {
        var result = 0
        return result
    }

    fun part2(input: MutableList<Pair<Char, Int>>): Int {
        var result = 0
        return result
    }

    val testRawInput = readInput("DayXY_Test")
    val testInput = parseInput(testRawInput)

    check(part1(testInput) == 3)

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

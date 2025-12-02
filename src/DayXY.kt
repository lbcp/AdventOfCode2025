import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): MutableList<Pair<Char, Int>> {
        val parsed = mutableListOf<Pair<Char, Int>>()
        input.forEach {
            parsed.add(Pair(it.first(), it.substring(1).toInt()))
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

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_Test")
    check(part1(parseInput(testInput)) == 3)
    check(part2(parseInput(testInput)) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    val timePart1 = measureTime { part1(parseInput(input)).println() }
    println("Part 1 took $timePart1.")
    val timePart2 = measureTime { part2(parseInput(input)).println() }
    println("Part 2 took $timePart2.")
}

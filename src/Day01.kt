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
        var dial = 50
        var result = 0
        input.forEach {
            val modifier = if (it.first == 'R') 1 else -1
            dial += (it.second % 100) * modifier
            if (dial < 0) dial += 100
            else if (dial > 99) dial -= 100

            if (dial == 0) {
                result += 1
            }
        }
        return result
    }

    fun part2(input: MutableList<Pair<Char, Int>>): Int {
        var dial = 50
        var result = 0
        input.forEach {
            val modifier = if (it.first == 'R') 1 else -1
            val fullTurns = it.second / 100
            dial += (it.second % 100) * modifier

            if (dial == 0) {
                result += 1
            } else if (dial < 0) {
                if ((it.second % 100) * modifier == dial) result -= 1 // Correction for start at zero.
                dial += 100
                result += 1
            } else if (dial > 99) {
                result += 1
                dial -= 100
            }
            result += fullTurns
        }
        return result
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_Test")
    check(part1(parseInput(testInput)) == 3)
    check(part2(parseInput(testInput)) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    val timePart1 = measureTime { part1(parseInput(input)).println() }
    println("Part 1 took $timePart1.")
    val timePart2 = measureTime { part2(parseInput(input)).println() }
    println("Part 2 took $timePart2.")
}

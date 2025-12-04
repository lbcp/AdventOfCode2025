import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): MutableList<MutableList<Char>> {
        val parsed = mutableListOf<MutableList<Char>>()
        input.forEach {
            val row = mutableListOf<Char>()
            for (c in it) {
                row.add(c)
            }
            parsed.add(row)
        }
        return parsed
    }

    fun checkSurrounding(map: MutableList<MutableList<Char>>, row: Int, column: Int): Boolean {
        var count = 0
        val directions = listOf<Pair<Int, Int>>(Pair(0, -1),
            Pair(0, 1),
            Pair(1, 0),
            Pair(1, -1),
            Pair(1, 1),
            Pair(-1, 0),
            Pair(-1, -1),
            Pair(-1, 1))
        for ((r, c) in directions) {
            val testRow = row + r
            val testCol = column + c
            if (testRow < 0 || testRow > map.size - 1 ||
                testCol < 0 ||
                testCol > map[row].size - 1) continue

            if (map[testRow][testCol] == '@') count += 1
        }
        return count >= 4
    }

    fun solve(input: MutableList<MutableList<Char>>, part1: Boolean = true): Int {
        var result = 0
        var changed = true
        while (changed) {
            changed = false
            for ((row, j) in input.withIndex()) {
                for ((column, c) in j.withIndex()) {
                    if (c == '@') {
                        if (!checkSurrounding(input, row, column)) {
                            result += 1
                            if (!part1) {
                                input[row][column] = '+'
                                changed = true
                            }
                        }
                    }
                }
            }
        }
        println(result)
        return result
    }


    // Or read a large test input from the `src/Day01_test.txt` file:
    val testRaw = readInput("Day04_Test")
    val parsedTest = parseInput(testRaw)
    //val testInput = parseInput(testRaw)
    check(solve(parsedTest) == 13)
    check(solve(parsedTest, false) == 43)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    val parsedInput =  parseInput(input)
    val timePart1 = measureTime { solve(parsedInput).println() }
    println("Part 1 took $timePart1.")

    val timePart2 = measureTime { solve(parsedInput, false).println() }
    println("Part 2 took $timePart2.")

}

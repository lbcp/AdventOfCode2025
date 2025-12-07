import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): MutableList<MutableList<Char>> {
        val grid = mutableListOf<MutableList<Char>>()
        input.forEach {
            val row = mutableListOf<Char>()
            for (c in it) row.add(c)
            grid.add(row)
        }
        return grid
    }

    fun solveParts(input: MutableList<MutableList<Char>>, part2: Boolean = false): Long {
        var result = 0L
        val splittersDict = mutableMapOf<Pair<Int, Int>, Long>()
        for ((rowNo, row) in input.withIndex()) {
            for ((colNo, c) in row.withIndex()) {
                if (c == 'S' || c == '|') {
                    if (c == 'S') splittersDict[Pair(rowNo, colNo)] = 1
                    if (rowNo + 1 < input.size) {
                        val own = splittersDict.getOrDefault(Pair(rowNo, colNo), 0)
                        when {
                            input[rowNo+1][colNo] != '^' -> {
                                val below = splittersDict.getOrDefault(Pair(rowNo+1,colNo), 0)
                                splittersDict[Pair(rowNo+1,colNo)] = below + own
                                input[rowNo+1][colNo] = '|'  // Straight beam
                                }
                            input[rowNo+1][colNo] == '^' -> {
                                result += 1
                                // check left and right not necessary
                                val left = splittersDict.getOrDefault(Pair(rowNo+1,colNo-1), 0)
                                val right = splittersDict.getOrDefault(Pair(rowNo+1,colNo+1), 0)
                                splittersDict[Pair(rowNo+1, colNo-1)] = left + own
                                splittersDict[Pair(rowNo+1, colNo+1)] = right + own
                                input[rowNo+1][colNo-1] = '|'
                                input[rowNo+1][colNo+1] = '|'
                            }
                        }
                    }
                }
            }
        }
        val secondPart: Long = splittersDict.filter { (key, _) -> key.first == input.size - 1 }
            .values
            .sum()

        if (part2) return secondPart
        return result
    }

    val testRawInput = readInput("Day07_Test")
    val testInput = parseInput(testRawInput)
    check(solveParts(testInput) == 21L)
    check(solveParts(testInput, true) == 40L)

    val rawInput = readInput("Day07")
    val input = parseInput(rawInput)
    repeat(5) {
        val timePart1 = measureTime { solveParts(input).println() }
        println("Part 1 took $timePart1.")
    }
    repeat(5) {
        val timePart2 = measureTime { solveParts(input, true).println() }
        println("Part 2 took $timePart2.")
    }

}

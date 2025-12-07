import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): List<List<String>> {
        fun <T> List<List<T>>.transpose(): List<List<T>> {
            val transposed = mutableListOf<List<T>>()
            for (i in first().indices) {
                val col: MutableList<T> = ArrayList()
                forEach { row ->
                    col.add(row[i])
                }
                transposed.add(col)
            }
            return transposed
        }

        val parsedList = mutableListOf<List<String>>()
        input.forEach {
            val temp = it.trimStart().split("\\s+".toRegex())
            if (temp.isNotEmpty()) parsedList.add(temp)
        }
        return parsedList.transpose()
    }

    fun parse2(input: List<String>): List<List<String>> {
        val fullList = mutableListOf<List<String>>()
        val partList = mutableListOf<String>()
        var maxRowLength = 0
        for (i in input) {
            if (i.length - 1 > maxRowLength) maxRowLength = i.length -1
        }
        (0..<input.size).forEach { partList.add("")
        }
        for (i in 0..maxRowLength) {
            var isEmpty = true
            for (r in 0..input.size - 1) {
                if (input[r].length - 1 < i) {
                    partList[r] += " "
                    continue
                }
                if (input[r][i].isLetterOrDigit()) isEmpty = false
                partList[r] += input[r][i]
            }
            if (isEmpty) {
                fullList.add(partList.toList())
                for ((x, _) in partList.withIndex()) partList[x] = ""
            }
        }
        fullList.add(partList.toList())
        return fullList.toList()
    }

    fun add(formular: List<String>): Long {
        var sum = 0L
        for (i in formular) {
            if (i == "+") return sum
            sum += i.toLong()
        }
        return sum
    }

    fun multiply(formular: List<String>): Long {
        var mul = 1L
        for (i in formular) {
            if (i == "*") return mul
            mul *= i.toLong()
        }
        return mul
    }

    fun part1(input: List<List<String>>): Long {
        var result = 0L
        input.forEach { formula ->
            val operator = formula.last()
            when (operator) {
                "+" -> result += add(formula)
                "*" -> result += multiply(formula)
                }
            }
        return result
    }

    fun part2(input: List<List<String>>): Long {
        var result = 0L
        input.forEach { formula ->
            val newNumbers = mutableListOf<String>()
            (0..<formula[0].length).forEach {
                newNumbers.add((""))
            }
            for (column in 0..formula[0].length - 1) {
                for (j in 0..formula.size - 2) newNumbers[column] += formula[j][column]
            }
            val finalNumbers = mutableListOf<String>()
            for (n in newNumbers) {
                val temp = n.trim()
                if (temp.isEmpty()) continue
                else finalNumbers.add(temp)
            }
            val operator = formula.last().trim()
            when (operator) {
                "+" -> result += add(finalNumbers)
                "*" -> result += multiply(finalNumbers)
            }
        }
        return result
    }

    val testRawInput = readInput("Day06_Test")
    val testInput = parseInput(testRawInput)
    val testInput2 = parse2(testRawInput)

    check(part1(testInput) == 4277556L)
    check(part2(testInput2) == 3263827L)

    val rawInput = readInput("Day06")
    val input = parseInput(rawInput)
    repeat(5) {
        val timePart1 = measureTime { part1(input).println() }
        println("Part 1 took $timePart1.")
    }

    val input2 = parse2(rawInput)
    repeat(5) {
        val timePart2 = measureTime { part2(input2).println() }
        println("Part 2 took $timePart2.")
    }
}

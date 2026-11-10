import arrow.core.getOrNone
import kotlin.math.pow

fun buildPossibilities(buttons: List<Int>): MutableMap<Int, MutableList<Set<Int>>> {
    // The idea is to cache all possible numbers, so I only have to create it once.
    val possibilities: Set<Set<Int>> = buttons.fold(setOf(emptySet<Int>())) { subsets, element ->
            subsets + subsets.map { it + element }
        }
    val patterns = mutableMapOf<Int, MutableList<Set<Int>>>()
    for (p in possibilities) {
        if (p.isNotEmpty()) {
            var result = 0
            for (i in p) {
                //println("i: $i = ${i.toUInt().toString(radix = 2)}")
                result = i xor result
                // println("xor'ed: ${result.toUInt().toString(radix = 2)}")
            }
            val available = patterns.getOrDefault(result, mutableListOf())
            available.add(p)
            patterns[result] = available
        }
    }

    return patterns
}

fun findPattern(machine: Triple<Int, List<Int>, List<Int>>) {
    val resultDict = mutableMapOf<Int, MutableList<Int>>(0 to mutableListOf<Int>())
    val (finalNumber, buttons, jolts) = machine
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
    println(resultDict)
}

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

            // Note from my future self: I have no clue how on earth I came up with this solution.
            // I basically transform the levers and patterns to binary representation.
            //
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

    fun getBinary(pattern: List<Int>): Int {
        var bin = 0
        for ((i, j) in pattern.reversed().withIndex()) {
            bin += (2.0.pow(i) * j).toInt()
        }
        return bin
    }

    fun countFlips(buttonsOptions: List<Set<Int>>, length: Int): MutableMap<Set<Int>, List<Int>> {
        // I felt so clever using binary, but now I have to deconstruct everything :-(
        val clicks = mutableMapOf<Set<Int>, List<Int>>()
        buttonsOptions.forEach { buttons ->
            val counts = MutableList(length) { 0 }
            for (button in buttons) {
                //println(button.toUInt().toString(radix = 2))
                for ((i, bit) in button.toUInt().toString(radix = 2).reversed().withIndex()) {
                    if (bit == '1') counts[counts.size - 1 - i] += 1
                }
            }
            clicks[buttons] = counts
        }
        // println("Clicks: $clicks")
        return clicks
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

    fun part2(input: MutableList<Triple<Int, List<Int>, List<Int>>>): Long {
        var result = 0L
        println("Input: $input")
        // Idea: Mainly inspired by Reddit.
        // Convert jolts to pattern -> Odd is 1, even 0.
        // Find *all* solutions to get the "jolt pattern"
        val resultsMap = mutableMapOf<List<Set<Int>>, Int>()
        fun calcPresses(jolts: List<Int>, possibilities: MutableMap<Int, MutableList<Set<Int>>>, path: List<Set<Int>>):
                Pair<Int, List<Set<Int>>>
            {
            //println("Jolts: $jolts")
            // println("Path: $path")
            if (jolts.all { it == 0 }) return Pair(0, path.toList())
            val pattern = jolts.map { x -> x % 2 }
            val binaryPattern = getBinary(pattern)
            // println(possibilities.getOrElse(binaryPattern, { Int.MAX_VALUE }))
            val buttonPresses = possibilities.getOrElse(binaryPattern, { emptyList() })
            if (buttonPresses.isNotEmpty()) {
                val flips = countFlips(buttonPresses, pattern.size)
                //println("Flips: $flips")
                for (flip in flips) {
                    val presses = flip.key.size
                    // println("Presses: $presses")
                    val newPath = path.toMutableList()
                    newPath.add(flip.key)
                    val newJolts = jolts.zip(flip.value) { i, j -> (i - j) / 2 }

                    //println("Old volt: $jolts")
                    //println("Presses: ${flip.value}")
                    //println("New volts: $newJolts")
                    if (newJolts.any { it < 0 }) continue // abort


                    var (result, finalPath) = calcPresses(newJolts, possibilities, newPath)
                    if (finalPath.isEmpty()) continue
                    result = (2 * result) + presses
                    resultsMap[finalPath] = result
                    // println("Result: $result")
                    // println("NEW Resultsmap: $resultsMap")
                }
            } else return Pair(-999, emptyList()) // Abort
            return Pair(-1, emptyList())
        }

        input.forEach {
            resultsMap.clear()
            println("NEW INPUT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Q")
            println(it.third)
            val deque = ArrayDeque<Pair<Int, List<Int>>>()
            val resultList = mutableListOf<Int>()
            val possibilities = buildPossibilities(it.second)
            val joltages = it.third
            calcPresses(joltages, possibilities, emptyList())
            // println("RESUKTSTKLAHKLJFNKJ $resultsMap")
            /* val stopList = List(joltages.size) { 0 }
            println("Stops: $stopList")
            deque.add(Pair(0, joltages))
            while (!deque.isEmpty()) {
                println("Dequing")
                val (curCount, curJolts) = deque.removeFirst()
                val pattern = curJolts.map { x -> x % 2 }
                val binaryPattern = getBinary(pattern)
                println(possibilities.getOrElse(binaryPattern, { Int.MAX_VALUE }))
                val buttonPresses = possibilities.getOrElse(binaryPattern, { emptyList() })
                if (buttonPresses.isNotEmpty()) {
                    val flips = countFlips(buttonPresses, pattern.size)
                    for (flip in flips) {
                        val presses = flip.key.size + (curCount * 2)
                        val newJolts = curJolts.zip(flip.value) { i, j -> (i - j) / 2 }

                        println("Old volt: $curJolts")
                        println("Presses: ${flip.value}")
                        println("New volts: $newJolts")
                        if (newJolts.any { it < 0 }) continue
                        if (newJolts == stopList) {
                            resultList.add(presses)
                        }
                        deque.add(Pair(presses, newJolts))
                    }
                }

            }
             */

            var testresult = Integer.MAX_VALUE
            for (r in resultsMap.keys) {
                var temp = 0
                for (v in r.reversed()) {
                    temp = (2 * temp) + v.size
                }
                println("Result: ${r.reversed()}")
                println("Temp: $temp")
                if (temp < testresult) testresult = temp
            }
            println("Test result: $testresult")
            println("RESUKTSTKLAHKLJFNKJ $resultsMap")
            result += testresult
        }


        return result


    }

    val testRawInput = readInput("Day10_Test")
    val testInput = parseInput(testRawInput)

    check(part1(testInput) == 7)

    val rawInput = readInput("Day10")
    val input = parseInput(rawInput)
    // val timePart1 = measureTime { part1(input).println() }
    // println("Part 1 took $timePart1.")
    part2(testInput)
    part2(input).println()
    //val timePart2 = measureTime {  }
    /*
    check(part2(testInput) == 6)


    val timePart2 = measureTime { part2(input).println() }
    println("Part 2 took $timePart2.")

     */
}

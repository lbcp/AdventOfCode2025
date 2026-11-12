import java.io.File

const val INF = Int.MAX_VALUE / 2

val BUTTONS = Regex("\\(([^)]*)\\)")
val JOLTAGE = Regex("\\{([^}]*)\\}")

/** Parses one input line into (button masks over counters, joltage target). */
fun parseMachine(line: String): Pair<IntArray, IntArray> {
    val target = JOLTAGE.find(line)!!.groupValues[1]
        .split(",").map { it.trim().toInt() }.toIntArray()

    val masks = BUTTONS.findAll(line).map { m ->
        var mask = 0
        for (part in itGroup(m.groupValues[1])) mask = mask or (1 shl part)
        mask
    }
        .toList()
        .toIntArray()

    return masks to target
}

fun itGroup(s: String) = s.split(",").map { it.trim().toInt() }

fun solveMachine(target: IntArray, masks: IntArray): Int {
    val m = target.size                 // counters
    val n = masks.size                  // buttons
    val subsets = 1 shl n

    // parity[s] = XOR of the button masks contained in subset s
    val parity = IntArray(subsets)
    for (s in 1 until subsets) {
        val low = s and (-s)
        parity[s] = parity[s xor low] xor masks[Integer.numberOfTrailingZeros(low)]
    }

    val patternCount = 1 shl m
    val counts = IntArray(patternCount)
    for (s in 0 until subsets) counts[parity[s]]++
    val groups = Array(patternCount) { IntArray(counts[it]) }
    val fill = IntArray(patternCount)
    for (s in 0 until subsets) {
        val p = parity[s]
        groups[p][fill[p]++] = s
    }

    val memo = HashMap<String, Int>()

    fun rec(t: IntArray): Int {
        if (t.all { it == 0 }) return 0

        val key = t.joinToString(",")
        memo[key]?.let { return it }

        var pattern = 0
        for (i in target.indices) if (t[i] and 1 == 1) pattern = pattern or (1 shl i)

        var best = INF
        for (s in groups[pattern]) {
            val next = t.copyOf()
            var ok = true
            var presses = 0
            var bits = s
            // press every button of S exactly once
            while (bits != 0) {
                val j = Integer.numberOfTrailingZeros(bits)
                bits = bits and (bits - 1)
                presses++
                var b = masks[j]
                while (b != 0) {
                    val i = Integer.numberOfTrailingZeros(b)
                    b = b and (b - 1)
                    if (--next[i] < 0) ok = false
                }
            }
            if (!ok) continue

            // everything left is even -> halve and recurse
            for (i in next.indices) next[i] = next[i] shr 1
            val sub = rec(next)
            if (sub < INF) best = minOf(best, presses + 2 * sub)
        }

        memo[key] = best
        return best
    }

    return rec(target)
}

fun main() {
    val path = "src/Day10.txt"
    val total = File(path).readLines()
        .filter { it.isNotBlank() }
        .sumOf { line ->
            val (masks, target) = parseMachine(line)
            solveMachine(target, masks)
        }
    println("Part 2: $total")
}

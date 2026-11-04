import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>):
            Pair<MutableMap<Int, MutableList<String>>, MutableList<Pair<String, List<Int>>>> {
        val gifts = mutableMapOf<Int, MutableList<String>>()
        val trees = mutableListOf<Pair<String, List<Int>>>()
        var curIndex = 0
        input.forEach { line ->
            if ("x" in line) {
                val tree = line.split(":")
                val presents = tree[1].trim().split(" ").map { it.toInt() }
                trees.add(Pair(tree[0], presents))
            }
            else {
                if (":" in line) {
                    val (ind, _) = line.split(":")
                    curIndex = ind.toInt()
                } else if (line.isNotEmpty()) {
                    val gift = gifts.getOrDefault(curIndex, mutableListOf())
                    gift.add(line)
                    gifts[curIndex] = gift
                }
            }
        }

        return Pair(gifts, trees)
    }

    fun part1(gifts: MutableMap<Int, MutableList<String>>, trees: MutableList<Pair<String, List<Int>>>): Int {
        var result = 0
        trees.forEach { tree ->
            val (sizeX, sizeY) = tree.first.split("x").map { it.toInt() }
            val treeSize = sizeX * sizeY
            var giftsSize = 0
            for ((g, count) in tree.second.withIndex()) {
                var size = 0
                for (p in gifts[g]!!) {
                    size += p.count { it == '#' }
                }
                giftsSize += size * count
            }
            if (giftsSize <= treeSize) {
                result += 1
            }
        }
        return result
    }

    val testRawInput = readInput("Day12")
    val (gifts, trees) = parseInput(testRawInput)

    val timePart1 = measureTime { part1(gifts, trees).println() }
    println("Part 1 took $timePart1.")
}

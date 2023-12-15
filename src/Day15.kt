fun main() {
    fun part1(input: List<String>): Int {
        return input.first()
            .split(",")
            .sumOf(String::hash)
    }

    fun part2(input: List<String>): Int {
        val boxes: MutableMap<Int, MutableList<Lens>> = mutableMapOf<Int, MutableList<Lens>>()
            .apply { (0..255).forEach { this[it] = mutableListOf() } }
        input.first()
            .split(",")
            .map(String::toLens)
            .forEach { lens ->
                if (lens.operation == '-') {
                    boxes[lens.box]?.removeIf { it.label == lens.label}
                } else {
                    val indexInBox = boxes[lens.box]?.indexOfFirst { it.label == lens.label } ?: -1
                    if (indexInBox < 0) {
                        boxes[lens.box]?.add(lens)
                    } else {
                        boxes[lens.box]?.removeAt(indexInBox)
                        boxes[lens.box]?.add(indexInBox, lens)
                    }
                }
            }
        return boxes.map{ (boxNumber, lensList) ->
            lensList.mapIndexed { lensIndex, lens ->
                (boxNumber + 1) * (lensIndex + 1) * lens.focal!!
            }
                .sum()
        }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)
    val testInput2 = readInput("Day15_test")
    check(part2(testInput2) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}

fun String.hash(): Int = this.toCharArray()
    .map { it.code }
    .fold(0) { acc, code -> ((acc + code) * 17) % 256 }

data class Lens(val label: String, val operation: Char, val focal: Int?) {
    val box = label.hash()
}

fun String.toLens(): Lens {
    val operationIndex = this.indexOfAny("-=".toCharArray())
    val label = this.substring(0, operationIndex)
    val operation = this[operationIndex]
    val focal = if (operation == '=') {
        this[operationIndex + 1].digitToInt()
    } else null
    return Lens(label, operation, focal)
}
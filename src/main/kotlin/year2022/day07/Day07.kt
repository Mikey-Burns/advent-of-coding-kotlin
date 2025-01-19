package year2022.day07

import utils.println
import utils.readInput

private const val TOTAL = 70000000L
private const val MIN_REQUIRED = 30000000L

fun main() {
    fun part1(input: List<String>): Long = input.parseInput()
        .allDirectories()
        .filter { it.nodeSize() < 100_000L }
        .sumOf { it.nodeSize() }

    fun part2(input: List<String>): Long {
        val root = input.parseInput()
        val used = TOTAL - root.nodeSize()
        val required = MIN_REQUIRED - used
        return root.allDirectories()
            .map(Node::nodeSize)
            .filter { it > required }
            .min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test", "2022")
    check(part1(testInput).also(::println) == 95437L)
    val testInput2 = readInput("Day07_test", "2022")
    check(part2(testInput2).also(::println) == 24933642L)

    val input = readInput("Day07", "2022")
    part1(input).println()
    part2(input).println()
}

private sealed class Node(val name: String, val parent: Node?) {

    abstract fun nodeSize(): Long

    abstract fun allDirectories(): Set<Directory>

    class File(name: String, parent: Node, val size: Long) : Node(name, parent) {
        override fun nodeSize(): Long = size
        override fun allDirectories(): Set<Directory> = emptySet()
    }

    class Directory(name: String, parent: Node?, val children: MutableList<Node>) : Node(name, parent) {
        override fun nodeSize(): Long = children.sumOf { it.nodeSize() }
        override fun allDirectories(): Set<Directory> {
            return buildSet {
                add(this@Directory)
                addAll(children.flatMap { it.allDirectories() })
            }
        }

        override fun toString(): String =
            "Directory[name=$name, parent=${parent?.name}, children=${children.map { it.name }}"
    }
}

private fun List<String>.parseInput(): Node.Directory {
    val root = Node.Directory("/", null, mutableListOf())
    var currentNode: Node.Directory = root

    this.drop(1).forEachIndexed { index, line ->
        if (line.startsWith("$")) {
            when (line.substring(2..3)) {
                "cd" -> {
                    val directoryName = line.substringAfter("cd ")
                    currentNode = if (directoryName == "..") {
                        currentNode.parent as Node.Directory
                    } else {
                        if (!currentNode.children.map { it.name }.contains(directoryName)) {
                            error("Uh oh")
                        }
                        currentNode.children
                            .first { it.name == directoryName } as Node.Directory
                    }
                }

                "ls" -> {
                    this.drop(index + 2)
                        .takeWhile { !it.startsWith("$") }
                        .map { lsLine ->
                            if (lsLine.startsWith("dir")) {
                                Node.Directory(lsLine.substringAfter("dir "), currentNode, mutableListOf())
                            } else {
                                lsLine.split(" ")
                                    .let { (size, name) -> Node.File(name, currentNode, size.toLong()) }
                            }
                        }
                        .let { currentNode.children.addAll(it) }
                }

                else -> error("Bad command")
            }
        }
    }
    return root
}
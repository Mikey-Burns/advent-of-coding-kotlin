fun main() {
    fun part1(input: List<String>): Int {
        val graph = Graph(input)
        val start = DPoint(0, 0, input.first().first().digitToInt())
        val end = DPoint(input.lastIndex, input.last().lastIndex, input.last().last().digitToInt())
        val tree = dijkstra(graph, start)
        val path = shortestPath(tree, start, end)
        val heatLoss = path.sumOf(DPoint::heatLoss)
        println(heatLoss)
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput).also(::println) == 102)
    val testInput2 = readInput("Day17_test")
    check(part2(testInput2).also(::println) == 0)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}

data class DPoint(val x: Int, val y: Int, val heatLoss: Int)

data class Graph(
    val vertices: Set<DPoint>,
    val edges: Map<DPoint, Set<DPoint>>
)

fun Graph(input: List<String>): Graph {
    val vertices: Set<DPoint> = input.flatMapIndexed() { rowIndex, row ->
        row.mapIndexed { columnIndex, c ->
            DPoint(rowIndex, columnIndex, c.digitToInt())
        }
    }
        .toSet()
    val edges: Map<DPoint, Set<DPoint>> = vertices.associateWith { point ->
        val left = vertices.firstOrNull { it.x == point.x - 1 && it.y == point.y }
        val right = vertices.firstOrNull { it.x == point.x + 1 && it.y == point.y }
        val up = vertices.firstOrNull { it.x == point.x && it.y == point.y - 1 }
        val down = vertices.firstOrNull { it.x == point.x && it.y == point.y + 1 }

        setOfNotNull(left, right, up, down)
    }

    return Graph(vertices, edges)
}

fun dijkstra(graph: Graph, start: DPoint): Map<DPoint, DPoint?> {
    // Empty set of finished vertices because we haven't visited any points
    val finishedVertices: MutableSet<DPoint> = mutableSetOf()
    // Tentative heat loss is infinite for every point
    val tentativeHeats: MutableMap<DPoint, Int> = graph.vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
    // 0 heat loss to get to our starting point
    tentativeHeats[start] = 0
    // No established paths anywhere yet
    val previousHeats: MutableMap<DPoint, DPoint?> = graph.vertices.associateWith { null }.toMutableMap()
    // Loop until we have finished every point
    while (finishedVertices != graph.vertices) {
        val currentPoint: DPoint = tentativeHeats
            .filterNot { finishedVertices.contains(it.key) }
            .minBy { it.value }
            .key

        graph.edges.getValue(currentPoint).minus(finishedVertices).forEach { neighbor: DPoint ->
            val newPath: Int = tentativeHeats.getValue(currentPoint) + neighbor.heatLoss

            if (newPath < tentativeHeats.getValue(neighbor)) {
                tentativeHeats[neighbor] = newPath
                previousHeats[neighbor] = currentPoint
            }
        }

        finishedVertices.add(currentPoint)
    }

    return previousHeats.toMap()
}

fun shortestPath(tree: Map<DPoint, DPoint?>, start: DPoint, end: DPoint): List<DPoint> {
    fun pathTo(start: DPoint, end: DPoint): List<DPoint> {
        if (tree[end] == null) return listOf(end)
        return listOf(pathTo(start, tree[end]!!), listOf(end)).flatten()
    }

    return pathTo(start, end)
}
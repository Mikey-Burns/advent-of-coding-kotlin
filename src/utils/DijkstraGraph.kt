package utils

data class DijkstraGraph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<Pair<T, T>, Int>
) {

    fun shortestPathTree(start: T): Map<T, T?> {
        val completed: MutableSet<T> = mutableSetOf()

        val delta = vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
        delta[start] = 0

        val previous: MutableMap<T, T?> = vertices.associateWith { null }.toMutableMap()

        while (completed != vertices) {
            val vertex: T = delta
                .filterNot { completed.contains(it.key) }
                .minBy { it.value }
                .key

            edges.getValue(vertex).minus(completed).forEach { neighbor ->
                val newPath = delta.getValue(vertex) + weights.getValue(vertex to neighbor)

                if (newPath < delta.getValue(neighbor)) {
                    delta[neighbor] = newPath
                    previous[neighbor] = vertex
                }
            }

            completed.add(vertex)
        }

        return previous.toMap()
    }

    fun longestPathTree(start: T): Map<T, T?> {
        val completed: MutableSet<T> = mutableSetOf()

        val delta = vertices.associateWith { Int.MIN_VALUE }.toMutableMap()
        delta[start] = 0

        val previous: MutableMap<T, T?> = vertices.associateWith { null }.toMutableMap()

        while (completed != vertices) {
            val vertex: T = delta
                .filterNot { completed.contains(it.key) }
                .maxBy { it.value }
                .key

            edges.getValue(vertex).minus(completed).forEach { neighbor ->
                val newPath = delta.getValue(vertex) + weights.getValue(vertex to neighbor)

                if (newPath > delta.getValue(neighbor)) {
                    delta[neighbor] = newPath
                    previous[neighbor] = vertex
                }
            }

            completed.add(vertex)
        }

        return previous.toMap()
    }

    fun <T> idealPath(idealPathTree: Map<T, T?>, start: T, end: T): List<T> {
        fun pathTo(start: T, end: T): List<T> {
            if (idealPathTree[end] == null) return listOf(end)
            return pathTo(start, idealPathTree[end]!!) + end
        }

        return pathTo(start, end)
    }

    fun weightOfIdealPath(idealPath: List<T>): Int = idealPath.zipWithNext().sumOf { weights[it]!! }
}
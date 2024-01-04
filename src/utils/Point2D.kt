package utils

data class Point2D(val x: Int, val y: Int) {
    fun east(): Point2D = copy(x = x + 1)
    fun west(): Point2D = copy(x = x - 1)
    fun north(): Point2D = copy(y = y - 1)
    fun south(): Point2D = copy(y = y + 1)

    fun step(direction: Compass): Point2D = when (direction) {
        Compass.NORTH -> north()
        Compass.EAST -> east()
        Compass.SOUTH -> south()
        Compass.WEST -> west()
    }
}

fun Point2D.orthogonalNeighbors(): List<Point2D> = listOf(east(), west(), north(), south())
operator fun Point2D.plus(other: Point2D): Point2D = Point2D(x + other.x, y + other.y)
enum class Compass {
    NORTH, EAST, SOUTH, WEST
}

fun List<List<*>>.isInBounds(location: Point2D) = location.y in this.indices
        && location.x in this[location.y].indices
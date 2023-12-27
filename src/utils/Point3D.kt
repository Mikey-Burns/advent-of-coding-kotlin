package utils

data class Point3D(val x: Int, val y: Int, val z: Int) {

    operator fun plus(other: Point3D): Point3D = 
        Point3D(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Point3D): Point3D =
        Point3D(x - other.x, y - other.y, z - other.z)
    
    companion object {
        val UP = Point3D(0, 1, 0)
        val DOWN = Point3D(0, -1, 0)
        val LEFT = Point3D(-1, 0, 0)
        val RIGHT = Point3D(1, 0, 0)
        val ABOVE = Point3D(0, 0, 1)
        val BELOW = Point3D(0, 0, -1)
    }
}

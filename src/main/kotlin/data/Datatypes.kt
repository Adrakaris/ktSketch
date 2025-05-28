package hu.yijun.data

data class Point(val x: Double, val y: Double) {
    fun toIntPoint() = IntPoint(x.toInt(), y.toInt())
}

data class IntPoint(val x: Int, val y: Int) : java.awt.Point(x, y) {
    fun toPoint() = Point(x.toDouble(), y.toDouble())
}

data class View(val x: Double, val y: Double, val w: Double, val h: Double) {
    val offset get() = Point(x, y)
    val size get() = Point(w, h)
}


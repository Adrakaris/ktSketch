package hu.yijun.util

import java.awt.Point

data class Coord(val x: Double, val y: Double) {
    fun toIntCoord() = IntCoord(x.toInt(), y.toInt())

    operator fun div(f: Double) = Coord(x/f, y/f)
}

data class IntCoord(val x: Int, val y: Int) : Point(x, y) {
    fun toCoord() = Coord(x.toDouble(), y.toDouble())
}

data class View(val x: Double, val y: Double, val w: Double, val h: Double) {
    val offset get() = Coord(x, y)
    val size get() = Coord(w, h)
}


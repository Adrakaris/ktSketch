package hu.yijun.sketchbook.util

import java.awt.Point

data class Coord(val x: Double, val y: Double) {
    fun toIntCoord() = IntCoord(x.toInt(), y.toInt())

    operator fun div(f: Double) = Coord(x/f, y/f)
    operator fun div(c: Coord) = Coord(x/c.x, y/c.y)
    operator fun div(c: IntCoord) = Coord(x/c.x.toDouble(), y/c.y.toDouble())

    operator fun times(c: Coord) = Coord(x*c.x, y*c.y)

    operator fun unaryMinus() = Coord(-x, -y)

    companion object {
        val ZERO = Coord(0.0, 0.0)
    }
}

data class IntCoord(val x: Int, val y: Int) : Point(x, y) {
    fun toCoord() = Coord(x.toDouble(), y.toDouble())

    operator fun minus(c: IntCoord) = IntCoord(x - c.x, y - c.y)

    companion object {
        val ZERO = IntCoord(0, 0)
    }
}

data class View(val x: Double, val y: Double, val w: Double, val h: Double) {
    constructor(x: Int, y: Int, w: Int, h: Int) :
            this(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())

    val offset get() = Coord(x, y)
    val size get() = Coord(w, h)

    companion object {
        val ZERO = View(0, 0, 0, 0)
    }
}


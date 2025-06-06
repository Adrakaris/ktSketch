package hu.yijun.sketchbook.util

import com.formdev.flatlaf.util.UIScale
import java.awt.Dimension
import java.awt.Point

fun Dimension.toIntCoord() = IntCoord(width, height)

fun unscaleIntCoord(value: IntCoord) = IntCoord(UIScale.unscale(value.x), UIScale.unscale(value.y))

fun Point.toIntCoord() = IntCoord(x, y)

package hu.yijun.sketchbook.model

import hu.yijun.sketchbook.util.IntCoord
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D

interface DrawAction {
    fun draw(graphics: Graphics2D)
}

class DrawInterpolatedLine(
    private val current: IntCoord,
    private val previous: IntCoord,
    private val color: Color,
    radius: Int,
): DrawAction {
    private val stroke = BasicStroke(
        (radius*2).toFloat(),
        BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND
    )

    override fun draw(graphics: Graphics2D) {
        graphics.color = color
        graphics.stroke = stroke
        graphics.drawLine(previous.x, previous.y, current.x, current.y)
    }
}

package hu.yijun.model

import com.formdev.flatlaf.util.UIScale
import hu.yijun.util.Coord
import hu.yijun.util.View
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class CanvasModel(
    width: Int, height: Int
) {
    val image: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    var view: View = View(0.0, 0.0, UIScale.scale(width).toDouble(), UIScale.scale(height).toDouble())
        private set
    var zoom: Double = 1.0
        private set
    private val graphics: Graphics2D = image.graphics as Graphics2D

    init {
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, image.width, image.height)
    }

    fun setViewSize(size: Coord) {
        view = view.copy(w = size.x, h = size.y)
    }

    fun close() {
        graphics.dispose()
    }
}
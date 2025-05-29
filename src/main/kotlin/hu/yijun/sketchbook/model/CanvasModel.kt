package hu.yijun.sketchbook.model

import com.formdev.flatlaf.util.UIScale
import hu.yijun.sketchbook.util.Coord
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.View
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class CanvasModel(
    width: Int, height: Int
) {
    val image: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val imageSize: IntCoord get() = IntCoord(image.width, image.height)

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

    // zero-centred, i.e. change zoom by 0.2 multiplies the internal zoom by 1.2
    fun changeZoom(zoomby: Double) {
        zoom = zoom * (1 + zoomby)
    }

    fun close() {
        graphics.dispose()
    }
}
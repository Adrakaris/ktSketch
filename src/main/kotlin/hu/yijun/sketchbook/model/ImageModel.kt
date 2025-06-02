package hu.yijun.sketchbook.model

import com.formdev.flatlaf.util.UIScale
import hu.yijun.sketchbook.util.Coord
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.View
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

const val MIN_ZOOM = 0.1
const val MAX_ZOOM = 10.0

class ImageModel(
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

    fun moveView(delta: Coord) {
        view = view.copy(x = view.x + delta.x, y = view.y + delta.y)
    }

    // zero-centred, i.e. change zoom by 0.2 multiplies the internal zoom by 1.2
    fun changeZoom(zoomby: Double, centre: Coord) {
        val factor = 1 + zoomby
        val newZoom = (zoom * factor).coerceIn(MIN_ZOOM, MAX_ZOOM)
        if (newZoom == zoom) return
        val scale = newZoom / zoom

        zoom = newZoom
        val newW = view.w / scale
        val newH = view.h / scale
        val newX = centre.x - (centre.x - view.x) / scale
        val newY = centre.y - (centre.y - view.y) / scale
        view = View(newX, newY, newW, newH)
    }

    fun close() {
        graphics.dispose()
    }
}
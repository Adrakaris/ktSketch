package hu.yijun.presenter

import hu.yijun.data.View
import hu.yijun.view.CanvasView
import java.awt.Color
import java.awt.image.BufferedImage

class CanvasPresenter {
    private var canvas: CanvasView? = null

    private val im = BufferedImage(50, 25, BufferedImage.TYPE_INT_ARGB)
    private val vi = View(-10.0, -10.0, 100.0, 100.0)

    init {
        val g = im.graphics
        g.color = Color.RED
        g.fillRect(0, 0, 50, 25)
        g.dispose()
    }

    fun attach(view: CanvasView) {
        canvas = view
        canvas?.drawImage(im, vi)
    }

    fun detach() {
        canvas = null
    }
}
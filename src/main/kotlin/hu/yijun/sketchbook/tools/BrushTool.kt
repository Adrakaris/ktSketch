package hu.yijun.sketchbook.tools

import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.util.toIntCoord
import java.awt.Color
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class BrushTool(
    private val canvasPresenter: CanvasPresenter
) {
    private var radius: Int = 10
    private var colour: Color = Color.BLACK

    val mouseAdapter: MouseAdapter = BrushAdapter()

    private inner class BrushAdapter : MouseAdapter() {
        private var lastPos: Point? = null

        override fun mousePressed(e: MouseEvent) {
            canvasPresenter.startDrawAction()
            val imageCoord = canvasPresenter.toImageCoords(e.point.toIntCoord()).toIntCoord()
            canvasPresenter.draw(imageCoord, imageCoord, colour, radius)
            lastPos = e.point
        }

        override fun mouseReleased(e: MouseEvent) {
            canvasPresenter.stopDrawAction()
            lastPos = null
        }

        override fun mouseDragged(e: MouseEvent) {
            lastPos?.let {
                canvasPresenter.draw(
                    canvasPresenter.toImageCoords(e.point.toIntCoord()).toIntCoord(),
                    canvasPresenter.toImageCoords(it.toIntCoord()).toIntCoord(),
                    colour,
                    radius
                )
                lastPos = e.point
            }
        }
    }
}
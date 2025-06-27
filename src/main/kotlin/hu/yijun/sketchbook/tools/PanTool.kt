package hu.yijun.sketchbook.tools

import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.util.toIntCoord
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class PanTool(
    private val canvasPresenter: CanvasPresenter
) {
    val mouseAdapter: MouseAdapter = PanAdapter()

    private inner class PanAdapter : MouseAdapter() {
        private var lastPos: Point? = null

        override fun mousePressed(e: MouseEvent) {
            lastPos = e.point
        }

        override fun mouseReleased(e: MouseEvent) {
            lastPos = null
        }

        override fun mouseDragged(e: MouseEvent) {
            lastPos?.let {
                val screenDelta = e.point.toIntCoord() - it.toIntCoord()
                canvasPresenter.pan(screenDelta)
                lastPos = e.point
            }
        }
    }
}

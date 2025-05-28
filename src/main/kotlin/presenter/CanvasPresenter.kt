package hu.yijun.presenter

import hu.yijun.model.CanvasModel
import hu.yijun.util.IntCoord
import hu.yijun.util.unscaleIntCoord
import hu.yijun.view.CanvasView

class CanvasPresenter {
    private var canvas: CanvasView? = null
    private var canvasModel: CanvasModel? = null

    fun attach(view: CanvasView) {
        canvas = view
    }

    fun newImage(size: IntCoord) {
        canvasModel?.close()

        val newModel = CanvasModel(size.x, size.y)
        canvas?.let {
            newModel.setViewSize(unscaleIntCoord(it.canvasSize).toCoord())
        }
        canvasModel = newModel
        paint()
    }

    fun onResize(newIntSize: IntCoord) {
        canvasModel?.let {
            val newSize = newIntSize.toCoord()
            val scaled = newSize / it.zoom
            it.setViewSize(scaled)
            paint()
        }
    }

    fun clear() {
        // TODO: this is temporary
        canvasModel?.close()
        canvasModel = null

        canvas?.clear()
    }

    private fun paint() {
        canvasModel?.let { model ->
            canvas?.draw(model.image, model.view)
        }
    }

    fun detach() {
        canvas = null
        canvasModel?.close()
    }
}

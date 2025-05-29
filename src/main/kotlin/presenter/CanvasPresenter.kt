package hu.yijun.presenter

import hu.yijun.model.CanvasModel
import hu.yijun.ui.CanvasView
import hu.yijun.util.IntCoord
import hu.yijun.util.View
import hu.yijun.util.unscaleIntCoord

fun interface ImageDataListener {
    fun onData(size: IntCoord, view: View)
}

interface ImageMetadataRepository {
    fun addImageDataListener(listener: ImageDataListener)
    fun removeImageDataListener(listener: ImageDataListener)
}

class CanvasPresenter : ImageMetadataRepository {

    private var canvas: CanvasView? = null
    private var canvasModel: CanvasModel? = null

    private var imageDataListeners: MutableList<ImageDataListener> = mutableListOf()

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

        notifyImageDataListeners()
        paint()
    }

    fun onScreenResize(newIntSize: IntCoord) {
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
        notifyImageDataListeners()
    }

    fun detach() {
        canvas = null
        canvasModel?.close()
    }

    override fun addImageDataListener(listener: ImageDataListener) {
        imageDataListeners.add(listener)
    }

    override fun removeImageDataListener(listener: ImageDataListener) {
        imageDataListeners.remove(listener)
    }

    private fun notifyImageDataListeners() {
        canvasModel?.let { model ->
            imageDataListeners.forEach {
                it.onData(model.imageSize, model.view)
            }
        } ?: imageDataListeners.forEach {
            it.onData(IntCoord.ZERO, View.ZERO)
        }
    }

    private fun paint() {
        canvasModel?.let { model ->
            canvas?.draw(model.image, model.view)
        }
    }
}

package hu.yijun.sketchbook.presenter

import hu.yijun.sketchbook.model.CanvasModel
import hu.yijun.sketchbook.ui.CanvasView
import hu.yijun.sketchbook.util.Coord
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.View
import hu.yijun.sketchbook.util.unscaleIntCoord

const val ZOOM_FACTOR = 0.05

fun interface ImageDataListener {
    fun onData(data: Data)

    data class Data(val size: IntCoord, val view: View, val mouseOnImage: Coord, val zoom: Double)
}

interface ImageMetadataRepository {
    fun addImageDataListener(listener: ImageDataListener)
    fun removeImageDataListener(listener: ImageDataListener)
}

class CanvasPresenter : ImageMetadataRepository {

    private var canvas: CanvasView? = null
    private var canvasModel: CanvasModel? = null

    private var imageDataListeners: MutableList<ImageDataListener> = mutableListOf()

    var mouseOnImage: Coord = Coord.ZERO
        set(value) {
            field = value
            notifyImageDataListeners()
        }

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

    fun zoom(normalisedPos: Coord, velocity: Double) {
        canvasModel?.let { model ->
            val positionOnImage = getImageCoordinates(normalisedPos, model.view)
            val zoom = -velocity * ZOOM_FACTOR

            model.changeZoom(zoom, positionOnImage)

            notifyImageDataListeners()
            paint()
        }
    }

    fun pan(normalisedDelta: Coord) {
        canvasModel?.let { model ->
            val imageDelta = normalisedDelta * model.view.size
            model.moveView(-imageDelta)
            notifyImageDataListeners()
            paint()
        }
    }

    fun imageCoordsOf(normalisedPos: Coord) =
        canvasModel?.let { getImageCoordinates(normalisedPos, it.view) } ?: Coord.ZERO

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
        val data: ImageDataListener.Data = canvasModel?.let { model ->
            ImageDataListener.Data(model.imageSize, model.view, mouseOnImage, model.zoom)
        } ?: ImageDataListener.Data(IntCoord.ZERO, View.ZERO, Coord.ZERO, 1.0)

        imageDataListeners.forEach {
            it.onData(data)
        }
    }

    private fun paint() {
        canvasModel?.let { model ->
            canvas?.draw(model.image, model.view)
        }
    }
}

fun getImageCoordinates(normalisedPos: Coord, view: View): Coord = Coord(
    x = view.x + view.w * normalisedPos.x,
    y = view.y + view.h * normalisedPos.y
)

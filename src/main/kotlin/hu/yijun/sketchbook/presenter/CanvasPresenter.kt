package hu.yijun.sketchbook.presenter

import hu.yijun.sketchbook.model.DrawInterpolatedLine
import hu.yijun.sketchbook.model.ImageDrawer
import hu.yijun.sketchbook.model.ImageModel
import hu.yijun.sketchbook.ui.CanvasView
import hu.yijun.sketchbook.util.Coord
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.View
import hu.yijun.sketchbook.util.unscaleIntCoord
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

const val ZOOM_FACTOR = 0.05

val VALID_IMAGE_FORMATS = ImageIO.getReaderFormatNames().map { it.lowercase() }.toSet()

fun interface ImageDataListener {
    fun onData(data: Data)

    data class Data(val size: IntCoord, val view: View, val mouseOnImage: Coord, val zoom: Double, val name: String)
}

interface ImageMetadataRepository {
    fun addImageDataListener(listener: ImageDataListener)
    fun removeImageDataListener(listener: ImageDataListener)
}

class CanvasPresenter(
    private val drawProcessor: ImageDrawer
) : ImageMetadataRepository {

    private var canvas: CanvasView? = null
    private var imageModel: ImageModel? = null

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
        imageModel?.let {
            it.close()
            drawProcessor.uninstall()
        }

        val newModel = ImageModel.with(size.x, size.y)
        canvas?.let {
            newModel.setViewSize(unscaleIntCoord(it.canvasSize).toCoord())
        }
        imageModel = newModel
        drawProcessor.install(newModel)

        notifyImageDataListeners()
        paint()
    }

    fun loadImage(file: File) {
        if (!file.exists() || !file.isFile) {
            println("Error loading image: File does not exist: $file")
            return
        }

        if (file.extension.lowercase() !in VALID_IMAGE_FORMATS) {
            println("Error loading image: unsupported format: .${file.extension}")
            return
        }

        val image = ImageIO.read(file)
        if (image == null) {
            println("Error loading image: could not read file: $file")
            return
        }

        imageModel?.let {
            it.close()
            drawProcessor.uninstall()
        }
        imageModel = ImageModel.with(image.width, image.height, image, file.name).also {
            drawProcessor.install(it)
        }
        canvas?.let {
            imageModel?.setViewSize(unscaleIntCoord(it.canvasSize).toCoord())
        }

        notifyImageDataListeners()
        paint()
    }

    fun closeImage() {
        // TODO: this is temporary
        imageModel?.close()
        imageModel = null

        canvas?.clear()
        notifyImageDataListeners()
    }

    fun onScreenResize(newIntSize: IntCoord) {
        imageModel?.let {
            val newSize = newIntSize.toCoord()
            val scaled = newSize / it.zoom
            it.setViewSize(scaled)

            paint()
        }
    }

    fun zoom(normalisedPos: Coord, velocity: Double) {
        imageModel?.let { model ->
            val positionOnImage = getImageCoordinates(normalisedPos, model.view)
            val zoom = -velocity * ZOOM_FACTOR

            model.changeZoom(zoom, positionOnImage)

            notifyImageDataListeners()
            paint()
        }
    }

    fun pan(normalisedDelta: Coord) {
        imageModel?.let { model ->
            val imageDelta = normalisedDelta * model.view.size
            model.moveView(-imageDelta)
            notifyImageDataListeners()
            paint()
        }
    }

    fun resetZoomAndPan() {
        imageModel?.let {
            it.resetZoomAndPosition()
            notifyImageDataListeners()
            paint()
        }
    }

    fun imageCoordsOf(normalisedPos: Coord) =
        imageModel?.let { getImageCoordinates(normalisedPos, it.view) } ?: Coord.ZERO

    private var shouldUpdateImageAfterDraw = false

    /** Positions in image coordinates */
    @OptIn(DelicateCoroutinesApi::class)
    fun draw(position: IntCoord, prevPosition: IntCoord, color: Color = Color.BLACK, radius: Int = 50) {
        if (imageModel == null) return

        val command = DrawInterpolatedLine(position, prevPosition, color, radius)
        drawProcessor.draw(command)

        if (!shouldUpdateImageAfterDraw) {
            shouldUpdateImageAfterDraw = true
            GlobalScope.launch {
                delay(16)
                paint()
                shouldUpdateImageAfterDraw = false
            }
        }
    }

//    fun detach() {
//        canvas = null
//        imageModel?.close()
//    }

    override fun addImageDataListener(listener: ImageDataListener) {
        imageDataListeners.add(listener)
    }

    override fun removeImageDataListener(listener: ImageDataListener) {
        imageDataListeners.remove(listener)
    }

    private fun notifyImageDataListeners() {
        val data: ImageDataListener.Data = imageModel?.let { model ->
            ImageDataListener.Data(model.imageSize, model.view, mouseOnImage, model.zoom, model.name)
        } ?: ImageDataListener.Data(IntCoord.ZERO, View.ZERO, Coord.ZERO, 1.0, "None")

        imageDataListeners.forEach {
            it.onData(data)
        }
    }

    private fun paint() {
        imageModel?.let { model ->
            canvas?.draw(model.image, model.view)
        }
    }
}

fun getImageCoordinates(normalisedPos: Coord, view: View): Coord = Coord(
    x = view.x + view.w * normalisedPos.x,
    y = view.y + view.h * normalisedPos.y
)

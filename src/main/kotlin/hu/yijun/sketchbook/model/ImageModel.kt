package hu.yijun.sketchbook.model

import hu.yijun.sketchbook.util.Coord
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.View
import java.awt.image.BufferedImage

interface ImageModel {
    val image: BufferedImage
    val imageSize: IntCoord
    val view: View
    val zoom: Double
    val name: String

    fun setViewSize(size: Coord)
    fun moveView(delta: Coord)
    /** zero-centred, i.e. change zoom by 0.2 multiplies the internal zoom by 1.2 */
    fun changeZoom(zoomby: Double, centre: Coord)
    fun resetZoomAndPosition()

    fun draw(drawBatch: List<DrawAction>)
    fun drawImage(image: BufferedImage, position: IntCoord)

    fun startEdit()
    fun finishEdit()
    fun undo()
    fun redo()

    fun close()

    companion object {
        fun with(width: Int, height: Int, image: BufferedImage, name: String): ImageModel =
            ImageModelImpl(width, height, image, name)

        fun with(width: Int, height: Int): ImageModel = ImageModelImpl(width, height)
    }
}

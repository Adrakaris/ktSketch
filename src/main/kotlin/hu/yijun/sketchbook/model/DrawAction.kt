package hu.yijun.sketchbook.model

import hu.yijun.sketchbook.model.undo.AtomicUndoableDrawEdit
import hu.yijun.sketchbook.util.IntCoord
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import kotlin.math.max
import kotlin.math.min

interface DrawAction {
    fun draw(graphics: Graphics2D, image: Image)
    fun getAtomicUndo(model: ImageModel): AtomicUndoableDrawEdit
}

class DrawInterpolatedLine(
    private val current: IntCoord,
    private val previous: IntCoord,
    private val color: Color,
    private val radius: Int,
): DrawAction {
    private val stroke = BasicStroke(
        (radius*2).toFloat(),
        BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND
    )

    private lateinit var clipPrevious: BufferedImage
    private lateinit var clipPost: BufferedImage

    override fun draw(graphics: Graphics2D, image: Image) {
        clipPrevious = copyClip(image, previous, current, radius)

        graphics.color = color
        graphics.stroke = stroke
        graphics.drawLine(previous.x, previous.y, current.x, current.y)

        clipPost = copyClip(image, previous, current, radius)
    }

    override fun getAtomicUndo(model: ImageModel): AtomicUndoableDrawEdit {
        return AtomicUndoableDrawEdit(
            model,
            clipPrevious,
            clipPost,
            minXYOf(current, previous) - radius
        )
    }
}

private fun copyClip(original: Image, from: IntCoord, to: IntCoord, padding: Int): BufferedImage {
    val topLeft = minXYOf(from, to) - padding
    val bottomRight = maxXYOf(from, to) + padding
    val size = bottomRight - topLeft
    val copy = BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB)

    val g = copy.createGraphics()
    g.drawImage(
        original,
        0, 0, size.x, size.y,
        topLeft.x, topLeft.y, bottomRight.x, bottomRight.y,
        null
    )
    g.dispose()

    return copy
}

private fun minXYOf(corner1: IntCoord, corner2: IntCoord) =
    IntCoord(min(corner1.x, corner2.x), min(corner1.y, corner2.y))

private fun maxXYOf(corner1: IntCoord, corner2: IntCoord) =
    IntCoord(max(corner1.x, corner2.x), max(corner1.y, corner2.y))

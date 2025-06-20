package hu.yijun.sketchbook.model

import com.formdev.flatlaf.util.UIScale
import hu.yijun.sketchbook.util.Coord
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.View
import java.awt.Color
import java.awt.image.BufferedImage
import javax.swing.undo.CompoundEdit
import javax.swing.undo.UndoManager
import javax.swing.undo.UndoableEdit

private const val MIN_ZOOM = 0.1
private const val MAX_ZOOM = 20.0

class ImageModelImpl(
    width: Int,
    height: Int,
    override val image: BufferedImage,
    override var name: String,
) : ImageModel {
    override val imageSize: IntCoord get() = IntCoord(image.width, image.height)

    override var view: View = View(0.0, 0.0, UIScale.scale(width).toDouble(), UIScale.scale(height).toDouble())
    override var zoom: Double = 1.0

    private val graphics = image.createGraphics()
    private val drawLock = Any()

    private val undoManager = UndoManager()
    private var currentCompoundEdit: CompoundEdit? = null

    constructor(width: Int, height: Int) : this(
        width, height,
        BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB),
        "New Image",
    ) {
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, image.width, image.height)
    }

    override fun setViewSize(size: Coord) {
        view = view.copy(w = size.x, h = size.y)
    }

    override fun moveView(delta: Coord) {
        view = view.copy(x = view.x + delta.x, y = view.y + delta.y)
    }

    override fun changeZoom(zoomby: Double, centre: Coord) {
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

    override fun resetZoomAndPosition() {
        view = view.copy(x = 0.0, y = 0.0)
        changeZoom(1/zoom - 1, Coord.Companion.ZERO)
    }

    override fun draw(drawBatch: List<DrawAction>) {
        val batchActions = mutableListOf<UndoableEdit>()

        synchronized(drawLock) {
            for (command in drawBatch) {
                command.draw(graphics, image)
                val action = command.getAtomicUndo(this)
                batchActions.add(action)
            }
        }

        for (action in batchActions) {
            currentCompoundEdit?.addEdit(action)
        }
    }

    override fun drawImage(image: BufferedImage, position: IntCoord) {
        synchronized(drawLock) {
            graphics.drawImage(image, null, position.x, position.y)
        }
    }

    override fun startEdit() {
        currentCompoundEdit = CompoundEdit()
    }

    override fun finishEdit() {
        currentCompoundEdit?.apply {
            end()
            undoManager.addEdit(this)
        }
        currentCompoundEdit = null
    }

    override fun undo() {
        if (undoManager.canUndo()) {
            undoManager.undo()
        }
    }

    override fun redo() {
        if (undoManager.canRedo()) {
            undoManager.redo()
        }
    }

    override fun close() {
        graphics.dispose()
    }
}
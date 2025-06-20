package hu.yijun.sketchbook.model.undo

import hu.yijun.sketchbook.model.ImageModel
import hu.yijun.sketchbook.util.IntCoord
import java.awt.image.BufferedImage
import javax.swing.undo.AbstractUndoableEdit

class AtomicUndoableDrawEdit(
    private val modelToUndoOn: ImageModel,
    private val sectionBeforeDraw: BufferedImage,
    private val sectionAfterDraw: BufferedImage,
    private val sectionPosition: IntCoord
) : AbstractUndoableEdit() {
    override fun undo() {
        super.undo()
        modelToUndoOn.drawImage(sectionBeforeDraw, sectionPosition)
    }

    override fun redo() {
        super.redo()
        modelToUndoOn.drawImage(sectionAfterDraw, sectionPosition)
    }

    override fun getPresentationName(): String = "Atomic draw"
}
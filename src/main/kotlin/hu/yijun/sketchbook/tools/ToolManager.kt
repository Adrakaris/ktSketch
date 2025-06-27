package hu.yijun.sketchbook.tools

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities

class ToolManager(
    private val brushTool: BrushTool,
    private val panTool: PanTool
) {
    var toolActive = false
        private set

    val mouseAdapter: MouseAdapter = ToolAdapter()

    private inner class ToolAdapter : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            toolActive = true

            if (shouldPan(e)) {
                panTool.mouseAdapter.mousePressed(e)
            }

            if (shouldDraw(e)) {
                brushTool.mouseAdapter.mousePressed(e)
            }
        }

        override fun mouseReleased(e: MouseEvent) {
            toolActive = false
            panTool.mouseAdapter.mouseReleased(e)
            brushTool.mouseAdapter.mouseReleased(e)
        }

        override fun mouseDragged(e: MouseEvent) {
            if (shouldPan(e)) {
                panTool.mouseAdapter.mouseDragged(e)
            }

            if (shouldDraw(e)) {
                brushTool.mouseAdapter.mouseDragged(e)
            }
        }

        private fun shouldPan(e: MouseEvent): Boolean {
            val isMiddle =
                SwingUtilities.isMiddleMouseButton(e)
            val isCtrlOrMeta = e.isControlDown || e.isMetaDown
            val isLeft = SwingUtilities.isLeftMouseButton(e)
            return isMiddle || (isCtrlOrMeta && isLeft)
        }

        private fun shouldDraw(e: MouseEvent) = !shouldPan(e)
    }
}
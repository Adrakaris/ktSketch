package hu.yijun.sketchbook.ui

import hu.yijun.sketchbook.constants.AppSizes
import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.presenter.VALID_IMAGE_FORMATS
import hu.yijun.sketchbook.ui.components.BorderlessButton
import hu.yijun.sketchbook.util.Coord
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.koinInject
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.io.File
import java.io.FilenameFilter
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingUtilities

class TopPanel(
    private val canvasPresenter: CanvasPresenter = koinInject()
) : JPanel() {

    private val newButton = JButton("New Image")
    private val loadButton = JButton("Load image")
    private val clearButton = JButton("Clear Image")
    private val resetViewButton = BorderlessButton("Reset")

    init {
        initComponents()

        newButton.addActionListener {
            canvasPresenter.newImage(IntCoord(1280, 720))
        }
        clearButton.addActionListener {
            canvasPresenter.closeImage()
        }
        loadButton.addActionListener(::loadImageAction)
        resetViewButton.addActionListener {
            canvasPresenter.resetZoomAndPan()
        }
        canvasPresenter.addImageDataListener { data ->
            resetViewButton.isEnabled = data.zoom != 1.0 || data.view.offset != Coord.ZERO
        }

        SwingUtilities.invokeLater {
            rootPane.defaultButton = newButton
        }
    }

    private fun initComponents() {
        layout = BorderLayout()

        val left = JPanel().apply {
            layout = FlowLayout(FlowLayout.LEFT, AppSizes.SMALL, AppSizes.SMALL)
            add(newButton)
            add(loadButton)
            add(clearButton)
        }

        val right = JPanel().apply {
            layout = FlowLayout(FlowLayout.LEFT, AppSizes.SMALL, AppSizes.SMALL)
            add(resetViewButton)
        }

        add(left, BorderLayout.WEST)
        add(right, BorderLayout.EAST)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun loadImageAction(e: ActionEvent) {
        val parent = SwingUtilities.getWindowAncestor(this) as? java.awt.Frame
        val dialog = java.awt.FileDialog(parent, "Open Image", java.awt.FileDialog.LOAD)

        dialog.filenameFilter = FilenameFilter { _, name ->
            VALID_IMAGE_FORMATS.any { name.lowercase().endsWith(".$it") }
        }

        dialog.isVisible = true

        val dir = dialog.directory
        val file = dialog.file
        if (dir != null && file != null) {
            val selectedFile = File(dir, file)
            canvasPresenter.loadImage(selectedFile)
        }
    }
}
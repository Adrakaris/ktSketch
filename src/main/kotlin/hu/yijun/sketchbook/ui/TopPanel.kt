package hu.yijun.sketchbook.ui

import hu.yijun.sketchbook.constants.AppSizes
import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.presenter.VALID_IMAGE_FORMATS
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.koinInject
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

    init {
        initComponents()

        newButton.addActionListener {
            canvasPresenter.newImage(IntCoord(1280, 720))
        }
        clearButton.addActionListener {
            canvasPresenter.clear()
        }
        loadButton.addActionListener(::loadImageAction)

        SwingUtilities.invokeLater {
            rootPane.defaultButton = newButton
        }
    }

    private fun initComponents() {
        layout = FlowLayout(FlowLayout.LEFT, AppSizes.SMALL, AppSizes.SMALL)

        add(newButton)
        add(loadButton)
        add(clearButton)
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
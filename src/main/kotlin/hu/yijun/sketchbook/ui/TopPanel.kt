package hu.yijun.sketchbook.ui

import hu.yijun.sketchbook.constants.AppSizes
import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.koinInject
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingUtilities

class TopPanel(
    canvasPresenter: CanvasPresenter = koinInject()
) : JPanel() {

    private val newButton = JButton("New Image")
    private val clearButton = JButton("Clear Image")

    init {
        initComponents()

        newButton.addActionListener {
            canvasPresenter.newImage(IntCoord(1280, 720))
        }
        clearButton.addActionListener {
            canvasPresenter.clear()
        }

        SwingUtilities.invokeLater {
            rootPane.defaultButton = newButton
        }
    }

    private fun initComponents() {
        layout = FlowLayout(FlowLayout.LEFT, AppSizes.SMALL, AppSizes.SMALL)

        add(newButton)
        add(clearButton)
    }
}
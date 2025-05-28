package hu.yijun.view

import hu.yijun.constants.AppPadding
import hu.yijun.presenter.CanvasPresenter
import hu.yijun.util.IntCoord
import hu.yijun.util.koinInject
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
        layout = FlowLayout(FlowLayout.LEFT, AppPadding.SMALL, AppPadding.SMALL)

        add(newButton)
        add(clearButton)
    }
}
package hu.yijun.view

import hu.yijun.constants.AppColours
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.SwingUtilities

class Sketchbook : JPanel() {
    private val topPanel = TopPanel()
    private val canvas = CanvasPanel()
    private val bottomBar = BottomBar()

    init {
        initComponents()
        setColours()
    }

    override fun updateUI() {
        super.updateUI()
        setColours()
    }

    private fun initComponents() {
        layout = GridBagLayout()
        val gbc = GridBagConstraints().apply {
            gridx = 0
            weightx = 1.0
            fill = GridBagConstraints.HORIZONTAL
        }

        gbc.gridy = 0
        gbc.weighty = 0.0
        add(topPanel, gbc)

        gbc.gridy++
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.BOTH
        add(canvas, gbc)

        gbc.weighty = 0.0
        gbc.fill = GridBagConstraints.HORIZONTAL

        gbc.gridy++
        add(bottomBar, gbc)
    }

    private fun setColours() {
        SwingUtilities.invokeLater {
            topPanel.border = BorderFactory.createMatteBorder(
                0, 0, 1, 0, AppColours.BORDER
            )

            bottomBar.border = BorderFactory.createMatteBorder(
                1, 0, 0, 0, AppColours.BORDER
            )
        }
    }
}


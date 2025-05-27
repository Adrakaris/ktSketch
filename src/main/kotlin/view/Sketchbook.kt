package hu.yijun.view

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JFrame

class Sketchbook : JFrame("KtSketchbook") {
    private val topPanel = TopPanel()
    private val canvas = CanvasPanel()
    private val bottomBar = BottomBar()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(1280, 960)

        initComponents()
    }

    private fun initComponents() {
        layout = GridBagLayout()
        val gbc = GridBagConstraints().apply {
            gridx = 0
            weightx = 1.0
        }

        gbc.gridy = 0
        gbc.weighty = 0.0
        gbc.fill = GridBagConstraints.HORIZONTAL
        add(topPanel, gbc)

        gbc.gridy = 1
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.BOTH
        add(canvas, gbc)

        gbc.gridy = 2
        gbc.weighty = 0.0
        gbc.fill = GridBagConstraints.HORIZONTAL
        add(bottomBar, gbc)

    }
}


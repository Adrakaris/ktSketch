package hu.yijun.view

import hu.yijun.constants.AppColours
import javax.swing.JPanel

class CanvasPanel : JPanel() {
    init {
        setColours()
        initComponents()
    }

    private fun setColours() {
        background = AppColours.SHADED
    }

    private fun initComponents() {
    }

    override fun updateUI() {
        super.updateUI()
        setColours()
    }
}
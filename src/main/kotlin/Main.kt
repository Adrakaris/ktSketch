package hu.yijun

import com.formdev.flatlaf.FlatLightLaf
import hu.yijun.theme.ThemeManager
import hu.yijun.theme.Theme
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        FlatLightLaf.setup()
        ThemeManager.setTheme(Theme.LIGHT)

        val window = JFrame("KtSketchbook")
        window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        window.setSize(1280, 720)

        val toggleButton = JButton("Toggle Theme").apply {
            addActionListener { ThemeManager.toggle() }
        }

        window.add(toggleButton)
        window.isVisible = true
    }
}

private fun ThemeManager.toggle() {
    if (theme() == Theme.LIGHT) {
        setTheme(Theme.DARK)
    } else {
        setTheme(Theme.LIGHT)
    }
}

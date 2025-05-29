package hu.yijun.sketchbook.theme

import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLightLaf
import com.formdev.flatlaf.extras.FlatAnimatedLafChange
import java.awt.Font
import javax.swing.JWindow
import javax.swing.SwingUtilities
import javax.swing.UIManager


enum class Theme { LIGHT, DARK }

object ThemeManager {

    private val lightLaf = FlatLightLaf()
    private val darkLaf = FlatDarkLaf()

    private var currentTheme: Theme = Theme.LIGHT
    private var currentFont: Font? = null

    fun setTheme(theme: Theme) {
        if (theme == currentTheme) return

        currentTheme = theme

        FlatAnimatedLafChange.showSnapshot()
        UIManager.setLookAndFeel(
            when (theme) {
                Theme.LIGHT -> lightLaf
                Theme.DARK -> darkLaf
            }
        )

        updateFrameUI()
        FlatAnimatedLafChange.hideSnapshotWithAnimation()
    }

    fun theme() = currentTheme

    fun setFont(font: Font) {
        currentFont = font
        UIManager.put("defaultFont", font)
        updateFrameUI()
    }

    fun font() = currentFont

    private fun updateFrameUI() {
        for (window in JWindow.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window)
        }
    }
}


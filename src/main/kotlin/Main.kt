package hu.yijun

import com.formdev.flatlaf.FlatLightLaf
import com.formdev.flatlaf.extras.FlatInspector
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector
import hu.yijun.theme.Theme
import hu.yijun.theme.ThemeManager
import hu.yijun.view.Sketchbook
import javax.swing.SwingUtilities

fun main() {

    FlatInspector.install( "ctrl shift alt X" )
    FlatUIDefaultsInspector.install("ctrl shift alt Y")

    SwingUtilities.invokeLater {
        FlatLightLaf.setup()
        ThemeManager.setTheme(Theme.LIGHT)

        val window = Sketchbook()
        window.isVisible = true
    }
}

package hu.yijun.sketchbook

import com.formdev.flatlaf.FlatLightLaf
import com.formdev.flatlaf.extras.FlatInspector
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector
import com.formdev.flatlaf.util.UIScale
import hu.yijun.sketchbook.theme.Theme
import hu.yijun.sketchbook.theme.ThemeManager
import hu.yijun.sketchbook.ui.Sketchbook
import hu.yijun.sketchbook.util.mainModule
import org.koin.core.context.startKoin
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE

fun main() {
    startKoin {
        modules(mainModule)
    }

    FlatInspector.install("ctrl shift alt X")
    FlatUIDefaultsInspector.install("ctrl shift alt Y")

    SwingUtilities.invokeLater {
        FlatLightLaf.setup()
        ThemeManager.setTheme(Theme.LIGHT)

        val window = JFrame("KtSketchbook")
        window.defaultCloseOperation = EXIT_ON_CLOSE
        window.setSize(UIScale.scale(1280), UIScale.scale(960))

        val sketchbook = Sketchbook()
        window.add(sketchbook)

        window.isVisible = true
    }
}

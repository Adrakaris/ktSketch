package hu.yijun

import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.extras.FlatInspector
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector
import com.formdev.flatlaf.util.UIScale
import hu.yijun.theme.Theme
import hu.yijun.theme.ThemeManager
import hu.yijun.ui.Sketchbook
import hu.yijun.util.mainModule
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
        FlatDarkLaf.setup()
        ThemeManager.setTheme(Theme.DARK)

        val window = JFrame("KtSketchbook")
        window.defaultCloseOperation = EXIT_ON_CLOSE
        window.setSize(UIScale.scale(1280), UIScale.scale(960))

        val sketchbook = Sketchbook()
        window.add(sketchbook)

        window.isVisible = true
    }
}

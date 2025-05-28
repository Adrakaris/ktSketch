package hu.yijun.view

import hu.yijun.constants.AppPadding
import hu.yijun.theme.Theme
import hu.yijun.theme.ThemeManager
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel

class BottomBar : JPanel() {
    private val themeToggleButton = JButton()

    init {
        initComponents()
    }

    private fun initComponents() {
        themeToggleButton.apply {
            text = "Toggle Theme"
            addActionListener { ThemeManager.toggle() }
            border = AppPadding.XSMALL_EMPTY_BORDER
        }

        layout = BoxLayout(this, BoxLayout.X_AXIS)
        add(themeToggleButton)
    }
}

private fun ThemeManager.toggle() {
    if (theme() == Theme.LIGHT) {
        setTheme(Theme.DARK)
    } else {
        setTheme(Theme.LIGHT)
    }
}

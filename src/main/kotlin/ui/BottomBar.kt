package hu.yijun.ui

import hu.yijun.constants.AppSizes
import hu.yijun.presenter.ImageMetadataRepository
import hu.yijun.theme.Theme
import hu.yijun.theme.ThemeManager
import hu.yijun.util.IntCoord
import hu.yijun.util.View
import hu.yijun.util.koinInject
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.SwingConstants

class BottomBar(
    imageMetadataRepository: ImageMetadataRepository = koinInject()
) : JPanel() {
    private val themeToggleButton = JButton()
    private val zoomLabel = JLabel("Zoom: 1.0")
    private val mouseLabel = JLabel("Mouse: (0, 0)")
    private val imageLabel = JLabel("(no image!)")

    init {
        imageMetadataRepository.addImageDataListener(::onImageDataChanged)

        initComponents()
    }

    private fun onImageDataChanged(size: IntCoord, view: View) {
        imageLabel.text = "Image (${size.x}x${size.y})"
    }

    private fun initComponents() {
        val smallFont = font.deriveFont(AppSizes.SMALL_FONT_SIZE)

        themeToggleButton.apply {
            text = "Toggle Theme"
            font = smallFont
            addActionListener { ThemeManager.toggle() }
            border = AppSizes.XSMALL_EMPTY_BORDER
        }
        zoomLabel.font = smallFont
        mouseLabel.font = smallFont
        imageLabel.font = smallFont

        layout = BoxLayout(this, BoxLayout.X_AXIS)
        add(themeToggleButton)

        add(Box.createHorizontalGlue())

        val rightInfoPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)

            add(zoomLabel)
            addSpacer(this)
            add(mouseLabel)
            addSpacer(this)
            add(imageLabel)
            add(Box.createHorizontalStrut(AppSizes.XSMALL))
        }
        add(rightInfoPanel)
    }

    private fun addSpacer(panel: JPanel) {
        panel.add(Box.createHorizontalStrut(AppSizes.XSMALL))
        panel.add(divider())
        panel.add(Box.createHorizontalStrut(AppSizes.XSMALL))
    }

    private fun divider(): JComponent {
        return JSeparator(SwingConstants.VERTICAL).apply {
            maximumSize = Dimension(3, 16)
        }
    }
}

private fun ThemeManager.toggle() {
    if (theme() == Theme.LIGHT) {
        setTheme(Theme.DARK)
    } else {
        setTheme(Theme.LIGHT)
    }
}

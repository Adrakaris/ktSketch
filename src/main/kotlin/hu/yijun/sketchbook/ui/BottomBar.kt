package hu.yijun.sketchbook.ui

import hu.yijun.sketchbook.constants.AppSizes
import hu.yijun.sketchbook.presenter.ImageDataListener
import hu.yijun.sketchbook.presenter.ImageMetadataRepository
import hu.yijun.sketchbook.theme.Theme
import hu.yijun.sketchbook.theme.ThemeManager
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.View
import hu.yijun.sketchbook.util.koinInject
import java.awt.Dimension
import javax.swing.*

class BottomBar(
    imageMetadataRepository: ImageMetadataRepository = koinInject()
) : JPanel() {
    private val themeToggleButton = JButton()
    private val zoomLabel = JLabel("Zoom: 1.0")
    private val mouseLabel = JLabel("Mouse: (-, -)")
    private val imageLabel = JLabel("(no image!)")

    private var view = View.ZERO

    init {
        imageMetadataRepository.addImageDataListener(::onImageDataChanged)

        initComponents()
    }

    private fun onImageDataChanged(data: ImageDataListener.Data) {
        val size = data.size
        val view = data.view
        val mouse = data.mouseOnImage

        this.view = view

        if (view == View.ZERO && size == IntCoord.ZERO) {
            imageLabel.text = "(no image!)"
            mouseLabel.text = "Mouse: (-, -)"
            zoomLabel.text = "Zoom: 1.0"
            return
        }

        zoomLabel.text = String.format("Zoom: %.1f", data.zoom)
        imageLabel.text = "Image (${size.x}x${size.y})"
        mouseLabel.text = "Mouse (${mouse.x.toInt()}, ${mouse.y.toInt()})"
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

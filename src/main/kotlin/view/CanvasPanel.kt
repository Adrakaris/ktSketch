package hu.yijun.view

import hu.yijun.constants.AppColours
import hu.yijun.data.View
import hu.yijun.presenter.CanvasPresenter
import hu.yijun.util.koinInject
import org.koin.core.component.KoinComponent
import java.awt.BorderLayout
import java.awt.Canvas
import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.AffineTransform
import javax.swing.JPanel

interface CanvasView {
    fun drawImage(image: Image, imageView: View)
}

class CanvasPanel(
    private val presenter: CanvasPresenter = koinInject()
) : JPanel(), CanvasView, KoinComponent {
    private val canvas = Canvas()

    init {
        presenter.attach(this)

        setColours()
        initComponents()
    }

    override fun drawImage(image: Image, imageView: View) {
        println("Boop")
        val graphics = canvas.graphics as Graphics2D? ?: return

        val scaleX = graphics.clipBounds.width / imageView.w
        val scaleY = graphics.clipBounds.height / imageView.h

        val transform = AffineTransform()
        transform.translate(-imageView.x * scaleX, -imageView.y * scaleY)
        transform.scale(scaleX, scaleY)

        graphics.drawImage(image, transform, null)

        graphics.dispose()

        canvas.repaint()
    }

    private fun setColours() {
        background = AppColours.SHADED
    }

    private fun initComponents() {
        layout = BorderLayout()

        add(canvas, BorderLayout.CENTER)

    }

    override fun updateUI() {
        super.updateUI()
        setColours()
    }
}
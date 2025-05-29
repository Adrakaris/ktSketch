package hu.yijun.sketchbook.ui

import hu.yijun.sketchbook.constants.AppColours
import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.View
import hu.yijun.sketchbook.util.koinInject
import hu.yijun.sketchbook.util.toIntCoord
import org.koin.core.component.KoinComponent
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.Rectangle
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.geom.AffineTransform
import javax.swing.JPanel

interface CanvasView {
    val canvasSize: IntCoord

    fun draw(image: Image, imageView: View)
    fun clear()
}

class CanvasPanel(
    private val presenter: CanvasPresenter = koinInject()
) : JPanel(), CanvasView, KoinComponent {

    private var image: Image? = null
    private var imageView: View? = null

    init {
        presenter.attach(this)

        setColours()
        initComponents()

        addComponentListener(CanvasAdapter(size))
    }

    override val canvasSize: IntCoord get() = size.toIntCoord()

    override fun draw(image: Image, imageView: View) {
        this.image = image
        this.imageView = imageView
        repaint()
    }

    override fun clear() {
        this.image = null
        this.imageView = null
        repaint()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        val graphics = g as Graphics2D

        if (image != null && imageView != null) {
            val transform = transformViewToFit(graphics.clipBounds, imageView!!)
            graphics.drawImage(image, transform, null)
        }
    }

    override fun updateUI() {
        super.updateUI()
        setColours()
    }

    private fun transformViewToFit(clipBounds: Rectangle, view: View): AffineTransform {
        val scaleX = clipBounds.width / view.w
        val scaleY = clipBounds.height / view.h
        val transform = AffineTransform()
        transform.translate(-view.x * scaleX, -view.y * scaleY)
        transform.scale(scaleX, scaleY)
        return transform
    }

    private fun setColours() {
        background = AppColours.SHADED
    }

    private fun initComponents() {
    }

    private inner class CanvasAdapter(initialSize: Dimension) : ComponentAdapter() {
        private var prevSize = initialSize

        override fun componentResized(e: ComponentEvent?) {
            super.componentResized(e)
            val newSize = e?.component?.size ?: return
            if (newSize == prevSize) return

            presenter.onScreenResize(newSize.toIntCoord())
            prevSize = newSize
        }
    }
}
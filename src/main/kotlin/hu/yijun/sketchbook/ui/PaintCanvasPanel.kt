package hu.yijun.sketchbook.ui

import com.formdev.flatlaf.FlatClientProperties
import hu.yijun.sketchbook.constants.AppColours
import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.util.*
import org.koin.core.component.KoinComponent
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.AffineTransform
import javax.swing.JPanel
import javax.swing.SwingUtilities

interface CanvasView {
    val canvasSize: IntCoord

    fun draw(image: Image, imageView: View)
    fun clear()
}

class PaintCanvasPanel(
    private val presenter: CanvasPresenter = koinInject()
) : JPanel(), CanvasView, KoinComponent {

    private var image: Image? = null
    private var imageView: View? = null

    init {
        presenter.attach(this)

        putClientProperty(FlatClientProperties.STYLE, "background: \$${AppColours.SHADED_BG_STYLE}")
        initComponents()

        addComponentListener(CanvasAdapter(size))
        addMouseWheelListener { e ->
            val normalisedPos = normaliseMouse(e.x, e.y)
            val velocity = e.preciseWheelRotation
            presenter.zoom(normalisedPos, velocity)
        }
        val mouseAdapter = CanvasMouseAdapter()
        addMouseListener(mouseAdapter)
        addMouseMotionListener(mouseAdapter)
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

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        val graphics = g as Graphics2D
        // supposedly, there's no way to have nearest neighbour with fractional translation. So
        // the image is always axis aligned despite everything I try.
        // Unless I want to use something like lwjgl3-awt this will be the case.
        // maybe that's something to think about in the future? (pain)

        if (image != null && imageView != null) {
            val transform = transformViewToFit(graphics.clipBounds, imageView!!)
            graphics.drawImage(image, transform, null)
        }
    }

    private fun transformViewToFit(clipBounds: Rectangle, view: View): AffineTransform {
        val scaleX = clipBounds.width / view.w
        val scaleY = clipBounds.height / view.h
        val transform = AffineTransform()
        transform.translate(-view.x * scaleX, -view.y * scaleY)
        transform.scale(scaleX, scaleY)
        return transform
    }

    private fun initComponents() {
    }

    private fun normaliseMouse(x: Int, y: Int): Coord {
        val pos = Coord(x.toDouble(), y.toDouble())
        return pos / canvasSize.toCoord()
    }

    private inner class CanvasAdapter(initialSize: Dimension) : ComponentAdapter() {
        private var prevSize = initialSize

        override fun componentResized(e: ComponentEvent) {
            super.componentResized(e)
            val newSize = e.component?.size ?: return
            if (newSize == prevSize) return

            presenter.onScreenResize(newSize.toIntCoord())
            prevSize = newSize
        }
    }

    private inner class CanvasMouseAdapter : MouseAdapter() {
        private var lastPos: Point? = null

        override fun mousePressed(e: MouseEvent) {
            if (shouldPan(e)) {
                lastPos = e.point
            }
        }

        override fun mouseReleased(e: MouseEvent) {
            lastPos = null
        }

        override fun mouseDragged(e: MouseEvent) {
            lastPos?.let {
                if (shouldPan(e)) {
                    val screenDelta = e.point.toIntCoord() - it.toIntCoord()
                    val normalisedDelta = screenDelta.toCoord() / canvasSize
                    presenter.pan(normalisedDelta)
                    lastPos = e.point
                }
            }
        }

        override fun mouseMoved(e: MouseEvent) {
            val imageCoords = presenter.imageCoordsOf(normaliseMouse(e.x, e.y))
            presenter.mouseOnImage = imageCoords
        }

        private fun shouldPan(e: MouseEvent): Boolean {
            val isMiddle =
                SwingUtilities.isMiddleMouseButton(e)
            val isCtrlOrMeta = e.isControlDown || e.isMetaDown
            val isLeft = SwingUtilities.isLeftMouseButton(e)
            return isMiddle || (isCtrlOrMeta && isLeft)
        }
    }
}
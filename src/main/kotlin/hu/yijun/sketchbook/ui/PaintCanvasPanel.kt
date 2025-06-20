package hu.yijun.sketchbook.ui

import com.formdev.flatlaf.FlatClientProperties
import hu.yijun.sketchbook.constants.AppColours
import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.util.*
import org.koin.core.component.KoinComponent
import java.awt.*
import java.awt.event.*
import java.awt.geom.AffineTransform
import javax.swing.AbstractAction
import javax.swing.JPanel
import javax.swing.KeyStroke
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
    // Mac three finger drag has a cooldown period where the mouse is still pressed but you are not pressing the drag
    // this works great in general but causes funny behavious if we allow undos to happen whilst the mouse is down and
    // drawing (anything which doesn't edit the canvas / cause an undo is fine)
    private var actionActive = false

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

        val inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW)
        val actionMap = actionMap

        val controlKey = Toolkit.getDefaultToolkit().menuShortcutKeyMaskEx
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, controlKey), "undo")
        actionMap.put("undo", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                if (!actionActive) presenter.undo()
            }
        })
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, controlKey or InputEvent.SHIFT_DOWN_MASK), "redo")
        actionMap.put("redo", object: AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                if (!actionActive) presenter.redo()
            }
        })
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
        // In terms of nearest neighbour with fractional translation, MAC does not do it, properly, so its always
        // axis aligned. WINDOWS, however, does.
        // This is a mac L. Though if I really want this, maybe try something like lwjgl3-awt

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

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

            if (shouldDraw(e)) {
                actionActive = true
                presenter.startDrawAction()
                val imageCoord = imageCoordsOfScreen(e.point.toIntCoord())
                presenter.draw(imageCoord, imageCoord)
                lastPos = e.point
            }
        }

        override fun mouseReleased(e: MouseEvent) {
            actionActive = false
            presenter.stopDrawAction()
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

                if (shouldDraw(e)) {
                    presenter.draw(imageCoordsOfScreen(e.point.toIntCoord()), imageCoordsOfScreen(it.toIntCoord()))
                    lastPos = e.point
                }
            }
        }

        override fun mouseMoved(e: MouseEvent) {
            val imageCoords = presenter.imageCoordsOf(normaliseMouse(e.x, e.y))
            presenter.mouseOnImage = imageCoords
        }

        private fun imageCoordsOfScreen(point: IntCoord): IntCoord =
            presenter.imageCoordsOf(normaliseMouse(point.x, point.y)).toIntCoord()

        private fun shouldPan(e: MouseEvent): Boolean {
            val isMiddle =
                SwingUtilities.isMiddleMouseButton(e)
            val isCtrlOrMeta = e.isControlDown || e.isMetaDown
            val isLeft = SwingUtilities.isLeftMouseButton(e)
            return isMiddle || (isCtrlOrMeta && isLeft)
        }

        private fun shouldDraw(e: MouseEvent) = !shouldPan(e)
    }
}
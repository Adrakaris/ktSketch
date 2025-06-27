package hu.yijun.sketchbook.ui

import com.formdev.flatlaf.FlatClientProperties
import hu.yijun.sketchbook.constants.AppColours
import hu.yijun.sketchbook.presenter.CanvasPresenter
import hu.yijun.sketchbook.tools.ToolManager
import hu.yijun.sketchbook.util.IntCoord
import hu.yijun.sketchbook.util.View
import hu.yijun.sketchbook.util.koinInject
import hu.yijun.sketchbook.util.toIntCoord
import org.koin.core.component.KoinComponent
import java.awt.*
import java.awt.event.*
import java.awt.geom.AffineTransform
import javax.swing.AbstractAction
import javax.swing.JPanel
import javax.swing.KeyStroke

interface CanvasView {
    val canvasSize: IntCoord

    fun draw(image: Image, imageView: View)
    fun clear()
}

class PaintCanvasPanel(
    private val presenter: CanvasPresenter = koinInject(),
    toolManager: ToolManager = koinInject()
) : JPanel(), CanvasView, KoinComponent {

    private var image: Image? = null
    private var imageView: View? = null
    // Mac three finger drag has a cooldown period where the mouse is still pressed but you are not pressing the drag
    // this works great in general but causes funny behaviours if we allow undos to happen whilst the mouse is down and
    // drawing (anything which doesn't edit the canvas / cause an undo is fine)
    private var actionActive = false

    init {
        presenter.attach(this)

        putClientProperty(FlatClientProperties.STYLE, "background: \$${AppColours.SHADED_BG_STYLE}")
        initComponents()

        addComponentListener(CanvasAdapter(size))
        addMouseWheelListener { e ->
            val velocity = e.preciseWheelRotation
            presenter.zoom(e.point.toIntCoord(), velocity)
        }

        addMouseListener(toolManager.mouseAdapter)
        addMouseMotionListener(toolManager.mouseAdapter)

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                val imageCoords = presenter.toImageCoords(e.point.toIntCoord())
                presenter.mouseOnImage = imageCoords
            }
        })

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
}
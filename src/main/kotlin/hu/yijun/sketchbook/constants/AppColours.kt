package hu.yijun.sketchbook.constants

import java.awt.Color
import javax.swing.UIManager

object AppColours {
    const val SHADED_BG_STYLE: String = "TextField.light"
    val BORDER: Color get() = UIManager.getColor("Component.borderColor")

//    https://github.com/JFormDesigner/FlatLaf/pull/375
    // accent colours can be changed this way
}
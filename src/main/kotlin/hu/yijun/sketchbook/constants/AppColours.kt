package hu.yijun.sketchbook.constants

import java.awt.Color
import javax.swing.UIManager

object AppColours {
    val SHADED: Color get() = UIManager.getColor("TextField.light")
    val BORDER: Color get() = UIManager.getColor("Component.borderColor")

//    https://github.com/JFormDesigner/FlatLaf/pull/375
    // accent colours can be changed this way
}
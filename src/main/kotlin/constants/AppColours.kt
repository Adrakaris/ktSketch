package hu.yijun.constants

import java.awt.Color
import javax.swing.UIManager

object AppColours {
    val SHADED: Color get() = UIManager.getColor("TextField.light") ?: Color.RED

//    https://github.com/JFormDesigner/FlatLaf/pull/375
    // accent colours can be changed this way
}
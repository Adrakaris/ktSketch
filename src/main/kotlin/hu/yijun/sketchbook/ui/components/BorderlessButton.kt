package hu.yijun.sketchbook.ui.components

import com.formdev.flatlaf.FlatClientProperties
import javax.swing.Icon
import javax.swing.JButton

class BorderlessButton(reset: String? = null, icon: Icon? = null) : JButton(reset, icon) {
    init {
        putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS)
        putClientProperty(FlatClientProperties.STYLE, "focusedBorderColor: #0000; innerFocusWidth: 0")
    }
}
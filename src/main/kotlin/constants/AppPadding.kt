package hu.yijun.constants

import javax.swing.BorderFactory
import javax.swing.border.Border

object AppPadding {
    val SMALL = 5

    val SMALL_EMPTY_BORDER: Border get() = BorderFactory.createEmptyBorder(SMALL, SMALL, SMALL, SMALL)
}
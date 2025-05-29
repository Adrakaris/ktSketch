package hu.yijun.sketchbook.constants

import javax.swing.BorderFactory
import javax.swing.border.Border

object AppSizes {
    const val XSMALL = 5
    const val SMALL = 10

    const val SMALL_FONT_SIZE = 10f

    val XSMALL_EMPTY_BORDER: Border get() = BorderFactory.createEmptyBorder(XSMALL, XSMALL, XSMALL, XSMALL)
//    val SMALL_EMPTY_BORDER: Border get() = BorderFactory.createEmptyBorder(SMALL, SMALL, SMALL, SMALL)
}
package hu.yijun.constants

import javax.swing.BorderFactory
import javax.swing.border.Border

object AppPadding {
    val XSMALL = 5
    val SMALL = 10

    val XSMALL_EMPTY_BORDER: Border get() = BorderFactory.createEmptyBorder(XSMALL, XSMALL, XSMALL, XSMALL)
    val SMALL_EMPTY_BORDER: Border get() = BorderFactory.createEmptyBorder(SMALL, SMALL, SMALL, SMALL)
}
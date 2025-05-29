package hu.yijun.scratch

import com.formdev.flatlaf.FlatLightLaf
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.SwingUtilities

fun main() {

    SwingUtilities.invokeLater {
        FlatLightLaf.setup()
        val frame = JFrame("Test")
        val button = JButton("CLick me!")
        val fileChooser = JFileChooser() // flat light laf is sufficient for native file choosing
        button.addActionListener {
            fileChooser.showDialog(frame, "Open")
        }
        frame.add(button)
        frame.setLocationRelativeTo(null)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.pack()
        frame.isVisible = true
    }
}
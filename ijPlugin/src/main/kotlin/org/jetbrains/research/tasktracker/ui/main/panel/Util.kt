package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.util.io.FileUtil
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.util.UIBundle
import java.io.File
import javax.swing.JButton

fun createJButton(
    uiBundleMessageKey: String,
    isBorderPaintedProp: Boolean = false,
    isVisibleProp: Boolean = true,
) = JButton(UIBundle.message(uiBundleMessageKey)).apply {
    isBorderPainted = isBorderPaintedProp
    isVisible = isVisibleProp
}

/**
 * Saves selected agreements as a json file.
 *
 * @property agreementString is an object to save in String format
 */
fun saveAgreements(agreementString: String) {
    val agreementFile = File(MainTaskTrackerConfig.agreementFilePath)
    FileUtil.createIfDoesntExist(agreementFile)
    agreementFile.writeText(agreementString)
}

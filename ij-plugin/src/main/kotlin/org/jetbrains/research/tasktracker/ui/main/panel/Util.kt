package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.io.FileUtil
import org.jetbrains.concurrency.Promise
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.ui.main.panel.models.ButtonState
import org.jetbrains.research.tasktracker.ui.main.panel.models.LinkType
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

fun MainPluginWindow.jslinkProcess(type: LinkType, action: (param: String) -> Unit) {
    with(type) {
        val preventCode = if (prevent) {
            """
            link.addEventListener('click', function(event) {
                event.preventDefault();
            });
            """.trimIndent()
        } else {
            System.lineSeparator()
        }
        executeJavascript(
            """
            const elements$name = document.querySelectorAll('$queryFilter');
            for (const link of elements$name) {
            $preventCode
                link.onclick = get_link$name
            }
            
            function get_link$name() {
                 link = this.getAttribute('$getAttribute');
            """,
            "}",
            "link"
        ) {
            action(it)
            null
        }
    }
}

/**
 * Creates new JButton state and return previous JButton state.
 */
fun JButton.changeState(buttonState: ButtonState) {
    addActionListener(buttonState.actionListener)
    this.text = buttonState.text
    isVisible = buttonState.isVisibleProp
}

fun JButton.getState(): ButtonState = ButtonState(text, isVisible, actionListeners.firstOrNull())

fun JButton.setListener(listener: () -> Unit = {}) {
    actionListeners.forEach {
        removeActionListener(it)
    }
    addActionListener {
        isEnabled = false
        listener.invoke()
        isEnabled = true
    }
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

fun <T> Promise<T>.runOnSuccess(task: (response: T) -> Unit) =
    onSuccess {
        ApplicationManager.getApplication().invokeLater {
            task(it)
        }
    }.onError {
        error(it.localizedMessage)
    }

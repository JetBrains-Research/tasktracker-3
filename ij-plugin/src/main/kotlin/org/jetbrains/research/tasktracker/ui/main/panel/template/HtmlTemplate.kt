package org.jetbrains.research.tasktracker.ui.main.panel.template

/**
 * This interface presents the html page and
 * has only one method to present this page as a string.
 */
interface HtmlTemplate {
    /**
     * @return html content as a text.
     */
    val htmlContent: String
}

package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.tracking.webcam.WebCamInfo
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme

class WebcamChoicePageTemplate(private val listOfCameras: List<WebCamInfo>) : HtmlTemplateBase() {
    override val htmlFileName: String? = null

    private fun generateTemplateForListOfCameras() = listOfCameras.mapIndexed { i, info ->
        """
            <div class="webcam-info-container">
                <p class="webcam-info-name">${getDeviceName(i)}: </p>
                <img class="webcam-info-img" src="${info.pictureExample}" alt="Photo">
            </div>
        """.trimIndent()
    }.joinToString(System.lineSeparator())

    private fun getDeviceName(deviceIndex: Int) = "Device#${deviceIndex + 1}"

    private fun generateTemplateForChoosingCamera() = listOfCameras.mapIndexed { i, info ->
        val value = getDeviceName(i)
        """
            <option value="${info.deviceNumber}" id="${info.deviceNumber}">$value</option>
        """.trimIndent()
    }.joinToString(System.lineSeparator())

    override fun pageContent(theme: Theme, vararg arguments: String) = pageTemplate(theme, buildPageTemplate())

    private fun buildPageTemplate() = """
        <div class="container">
            <div>
                <p class="small-font">We've made test pictures from all connected devices, please choose which one you prefer to use to detect your emotions:</p>
                ${generateTemplateForListOfCameras()}
                <label class="webcam-info-list" for="tasks">Please, choose the preferable option: </label>
                <div class="webcam-select-container">
                    <select name="cameras" id="cameras">
                    ${generateTemplateForChoosingCamera()}
                    </select>
                </div>
            </div>
        </div>
    """.trimIndent()
}

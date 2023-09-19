package org.jetbrains.research.tasktracker.ui.main.panel.template

import org.jetbrains.research.tasktracker.tracking.webcam.WebCamInfo

class WebcamChoicePageTemplate(private val listOfCameras: List<WebCamInfo>) : HtmlBaseTemplate() {
    override val content: String
        get() = buildPageTemplate()

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

    private fun buildPageTemplate() = """
        <div class="container">
            <div>
            <p class="small-font">We have captured test pictures from all connected devices. Please select the one you prefer to use for registering your emotions:</p>
            ${generateTemplateForListOfCameras()}
            <label class="webcam-info-list" for="tasks">Please select your preferred option: </label>
            <div class="webcam-select-container">
                <select name="cameras" id="cameras">
                ${generateTemplateForChoosingCamera()}
                </select>
            </div>
            </div>
        </div>
    """.trimIndent()
}

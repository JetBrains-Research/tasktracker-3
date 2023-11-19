package org.jetbrains.research.tasktracker.config.scenario.models

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage
import java.util.*

@Serializable
data class Scenario(
    @Serializable(with = QueueSerializer::class) val steps: Queue<ScenarioStep>
) {
    @Transient
    private val logger: Logger = Logger.getInstance(javaClass)

    @Transient
    private var currentStepIterator: Iterator<ScenarioUnit>? = null

    private fun getNextStep(): ScenarioStep? {
        var isValid: Boolean
        var step: ScenarioStep
        do {
            if (steps.isEmpty()) {
                logger.warn("No steps found!")
                return null
            }

            step = steps.poll()
            isValid = step.isValid()
            if (!isValid) {
                logger.warn("Found useless step, all configs are null. Skip it")
            }
        } while (!isValid)
        return step
    }

    @Suppress("ReturnCount")
    fun getNextUnit(project: Project): ScenarioUnit? {
        if (currentStepIterator?.hasNext() != true) {
            cleanStepSettings()
            val currentStep = getNextStep() ?: return null
            currentStep.prepareSettings(project)
            currentStepIterator = currentStep.getUnits().iterator()
            if (currentStepIterator?.hasNext() != true) {
                return null
            }
        }
        return currentStepIterator?.next()
    }

    private fun cleanStepSettings() =
        MainPanelStorage.activeIdeHandlers.forEach {
            it.destroy()
        }

    private class QueueSerializer<T>(private val dataSerializer: KSerializer<T>) :
        KSerializer<Queue<T>> {

        override val descriptor: SerialDescriptor = ListSerializer(dataSerializer).descriptor

        override fun serialize(encoder: Encoder, value: Queue<T>) {
            encoder.encodeSerializableValue(ListSerializer(dataSerializer), value.toList())
        }

        override fun deserialize(decoder: Decoder): Queue<T> {
            val queue = LinkedList<T>()
            val elements = decoder.decodeSerializableValue(ListSerializer(dataSerializer))
            queue.addAll(elements)
            return queue
        }
    }
}

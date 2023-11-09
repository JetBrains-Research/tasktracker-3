package org.jetbrains.research.tasktracker.config.scenario

import com.intellij.openapi.diagnostic.Logger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.BaseConfig
import java.util.*

enum class ScenarioUnitType {
    TASK_CONTENT,

    // TODO: add other types of content
    IDE_SETTINGS;

    val config: BaseConfig?
        get() = when (this) {
            TASK_CONTENT -> TaskTrackerPlugin.mainConfig.taskContentConfig
            IDE_SETTINGS -> TaskTrackerPlugin.mainConfig.mainIdeConfig
        }
}

@Serializable
data class ScenarioStep(
    // TODO: sort according to the plugin's implementation to make sure in the right order
    val units: List<ScenarioUnitType>
) {
    fun isValid() = units.any { it.config != null }
}

@Serializable
data class Scenario(
    @Serializable(with = QueueSerializer::class) val steps: Queue<ScenarioStep>
) {
    @Transient
    private val logger: Logger = Logger.getInstance(javaClass)

    fun getNextStep(): ScenarioStep? {
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

package org.jetbrains.research.tasktracker.config.emoji

// TODO make 2 config types?
object DefaultEmotion : Emotion(
    -1,
    "DEFAULT",
    listOf("New beginnings bring the promise of unexplored opportunities and the chance to create a brighter future."),
    listOf(
        buildString {
            append("<p>Here, you’ll find advice on handling your current emotions.</p>")
            append("<p>Since you just started, we haven’t tracked your emotions yet.</p>")
            append("<p>Please check back later!</p>")
        }
    )
) {
    override val iconName: String
        get() = "sun_with_face.png"
    override val modalWindowIconName: String
        get() = "modal_sun_with_face.png"
}

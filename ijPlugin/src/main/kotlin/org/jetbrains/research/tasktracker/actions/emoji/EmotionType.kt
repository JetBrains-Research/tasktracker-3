@file:Suppress("MaximumLineLength", "MaxLineLength", "MagicNumber")

package org.jetbrains.research.tasktracker.actions.emoji

import javax.swing.Icon

enum class EmotionType(
    val modelScore: Int,
    private val affirmDescriptions: List<String>,
    private val adviceDescriptions: List<String> = emptyList(),
    val icon: Icon? = null,
    val modalWindowIcon: Icon? = null
) {
    DEFAULT(
        -1,
        listOf("New beginnings bring the promise of unexplored opportunities and the chance to create a brighter future."),
        listOf(
            "<p>Here, you’ll find advice on handling your current emotions.</p><p>Since you just started, we haven’t tracked your emotions yet.</p><p>Please check back later!</p>"
        ),
        icon = EmojiActionIcons.defaultIcon,
        modalWindowIcon = EmojiActionIcons.defaultModalIcon
    ),
    NEUTRAL(
        0,
        listOf(
            "Being neutral allows people to fully experience what's happening now.",
            "Recognize that neutral states offer a valuable break from intense emotions.",
            "It's okay to embrace moments of calm and tranquility."
        ),
        listOf(
            "<p>Value emotional stability and balance</p><p>as signs of emotional maturity.</p>",
            "<p>Neutral times can be ideal for setting</p><p>and planning future objectives.</p>",
            "<p>Use neutral moments for mindfulness.</p><p>Pay attention to your thoughts, sensations,<p>and surroundings without judgment.</p>"
        ),
        icon = EmojiActionIcons.relievedFaceIcon,
        modalWindowIcon = EmojiActionIcons.seedlingIcon
    ),
    HAPPINESS(
        1,
        listOf(
            "Even small accomplishments contribute to happiness.",
            "Your happiness is a wonderful and valid emotion.",
            "You deserve to feel joy."
        ),
        listOf(
            "<p>Objectively evaluate the situation to</p><p>determine if there is a genuine threat.</p>",
            "<p>Try to reframe your perception of the situation.</p><p>What is the worst-case scenario,</p><p>and how likely is it to happen?</p>",
            "<p>Reflect on times when you've overcome similar fears.</p><p>Use these experiences as sources of strength and confidence.</p>"
        ),
        icon = EmojiActionIcons.smilingFaceWithSmilingEyesIcon,
        modalWindowIcon = EmojiActionIcons.glowingStarIcon
    ),
    SURPRISE(
        2,
        listOf(
            "Surprises add spice to life and keep things interesting",
            "Acknowledging your surprise shows that you're in tune with your environment and open to new experiences.",
            "Surprise is a completely natural and spontaneous emotion. It's a sign of your adaptability to unexpected situations."
        ),
        listOf(
            "<p>View surprises as adventures and avoid making</p><p>judgments before experiencing them.</p>",
            "<p>If the surprise is confusing or unsettling,</p><p>ask questions to gain a better understanding.</p>",
            "<p>In unsettling surprise, take a moment to</p><p>collect your thoughts before reacting.</p>"
        ),
        icon = EmojiActionIcons.faceWithOpenMouthIcon,
        modalWindowIcon = EmojiActionIcons.wrappedGiftIcon
    ),
    SADNESS(
        3,
        listOf(
            "This sadness is temporary; brighter moments will come.",
            "Your sadness is a valid and natural response to difficult situations.",
            "Sadness is a temporary state. It doesn't define your future; it's just a part of your journey."
        ),
        listOf(
            "<p>Reach out to others for comfort and perspective,</p><p>recognizing that you don't have to face sadness alone.</p>",
            "<p>Treat yourself with kindness and understanding</p><p>during moments of sadness.</p>",
            "<p>Set small, achievable goals for the day.</p><p>Accomplishing even minor tasks can boost your mood.</p>"
        ),
        icon = EmojiActionIcons.pensiveFaceIcon,
        modalWindowIcon = EmojiActionIcons.waterWaveIcon
    ),
    ANGER(
        4,
        listOf(
            "You might be angry and going through a tough time right now.",
            "It's okay to feel angry right now.",
            "Feeling angry is a sign that you care about something."
        ),
        listOf(
            "<p>Inhale deeply and exhale slowly to</p><p>calm your anger and regain control.</p>",
            "<p>Take a deep breath and think about whether</p><p>this situation is worth getting so upset about.</p>",
            "<p>Take a step back, assess the situation objectively,</p><p>and consider whether there's a different way to interpret it.</p>"
        ),
        icon = EmojiActionIcons.angryFaceIcon,
        modalWindowIcon = EmojiActionIcons.doveIcon
    ),
    DISGUST(
        5,
        listOf(
            "Disgust is a natural emotion, but it doesn't have to control you.",
            "Pay attention to your bodily sensations and thoughts related to disgust without judgment.",
            "Feeling disgust is a natural human response."
        ),
        listOf(
            "<p>Identify the specific aspect of the situation</p><p>causing disgust to gain clarity.<p>",
            "<p>Engage in an activity or focus your attention on</p><p>something else to divert your thoughts away</p><p>from the source of disgust.</p>",
            "<p>If possible, remove yourself from the source of disgust</p><p>or minimize exposure to it. Creating physical distance can help</p><p>reduce the intensity of the emotion.</p>"
        ),
        icon = EmojiActionIcons.confoundedFaceIcon,
        modalWindowIcon = EmojiActionIcons.cherryBlossomIcon
    ),
    FEAR(
        6,
        listOf(
            "Many people experience anxiety in similar situations.",
            "Fear and anxiety are a natural response to perceived threats. It's okay to feel this way; it's your body's way of keeping you safe.",
            "Anxiety is a common human emotion, and you're not alone in experiencing it."
        ),
        listOf(
            "<p>Objectively evaluate the situation</p><p>to determine if there is a genuine threat.</p>",
            "<p>Try to reframe your perception of the situation.</p><p>What is the worst-case scenario,</p><p>and how likely is it to happen?</p>",
            "<p>Reflect on times when you've overcome similar fears.</p><p>Use these experiences as sources of strength and confidence.</p>"
        ),
        icon = EmojiActionIcons.frowningFaceWithOpenMouthIcon,
        modalWindowIcon = EmojiActionIcons.leafFlutteringIcon
    ),
    CONTEMPT(
        7,
        listOf(
            "Being open-minded doesn't mean you have to agree and ignore your contempt.",
            "Acknowledging your contempt doesn't mean you have to dwell on it.",
            "Your feelings of contempt are a part of your emotional landscape, and you have the ability to navigate them wisely."
        ),
        listOf(
            "<p>Examine your assumptions and gather</p><p>more information before passing judgment.</p>",
            "<p>If the contemptuous situation becomes too heated,</p><p>it's okay to take a break from it to</p><p>cool off and regain perspective.</p>",
            "<p>Take a moment to reflect on why you're feeling contempt.</p><p>Understanding the source of your contempt is the first step.</p>"
        ),
        icon = EmojiActionIcons.expressionlessFaceIcon,
        modalWindowIcon = EmojiActionIcons.foldedHandsIcon
    );

    companion object {
        fun byModelScore(modelScore: Int): EmotionType {
            return when (modelScore) {
                0 -> NEUTRAL
                1 -> HAPPINESS
                2 -> SURPRISE
                3 -> SADNESS
                4 -> ANGER
                5 -> DISGUST
                6 -> FEAR
                7 -> CONTEMPT
                else -> DEFAULT
            }
        }
    }

    fun getRandomAffirmDescription() = affirmDescriptions.random()

    fun getRandomAdviceDescription() = adviceDescriptions.random()
}

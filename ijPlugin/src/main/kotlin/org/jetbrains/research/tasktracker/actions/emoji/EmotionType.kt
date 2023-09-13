package org.jetbrains.research.tasktracker.actions.emoji

import javax.swing.Icon

enum class EmotionType(
    val score: Int,
    private val affirmDescriptions: List<String>,
    private val adviceDescriptions: List<String> = emptyList(),
    val icon: Icon? = null
) {
    DEFAULT(
        -1,
        listOf("New beginnings bring the promise of unexplored opportunities and the chance to create a brighter future."),
        listOf(
            "<p>Here, you’ll find advice on handling your current emotions.</p><p>Since you just started, we haven’t tracked your emotions yet.</p><p>Please check back later!</p>"
        ),
        icon = EmojiActionIcons.defaultIcon
    ),
    NEUTRAL(
        0,
        listOf(
            "Being neutral allows people to fully experience what's happening now.",
            "Recognize that neutral states offer a valuable break from intense emotions.",
            "It's okay to embrace moments of calm and tranquility."
        ),
        listOf(
            "Value emotional stability and balance as signs of emotional maturity.",
            "Neutral times can be ideal for setting and planning future objectives.",
            "Use neutral moments for mindfulness. Pay attention to your thoughts, sensations, and surroundings without judgment."
        ),
        icon = EmojiActionIcons.relievedFaceIcon
    ),
    HAPPINESS(
        1,
        listOf(
            "Even small accomplishments contribute to happiness.",
            "Your happiness is a wonderful and valid emotion.",
            "You deserve to feel joy."
        ),
        listOf(
            "Objectively evaluate the situation to determine if there is a genuine threat.",
            "Try to reframe your perception of the situation. What is the worst-case scenario, and how likely is it to happen?",
            "Reflect on times when you've overcome similar fears. Use these experiences as sources of strength and confidence."
        ),
        icon = EmojiActionIcons.frowningFaceWithOpenMouthIcon
    ),
    SURPRISE(
        2,
        listOf(
            "Surprises add spice to life and keep things interesting",
            "Acknowledging your surprise shows that you're in tune with your environment and open to new experiences.",
            "Surprise is a completely natural and spontaneous emotion. It's a sign of your adaptability to unexpected situations."
        ),
        listOf(
            "View surprises as adventures and avoid making judgments before experiencing them.",
            "If the surprise is confusing or unsettling, ask questions to gain a better understanding.",
            "In unsettling surprise, take a moment to collect your thoughts before reacting."
        ),
        icon = EmojiActionIcons.faceWithOpenMouthIcon
    ),
    SADNESS(
        3,
        listOf(
            "This sadness is temporary; brighter moments will come.",
            "Your sadness is a valid and natural response to difficult situations.",
            "Sadness is a temporary state. It doesn't define your future; it's just a part of your journey."
        ),
        listOf(
            "Reach out to others for comfort and perspective, recognizing that you don't have to face sadness alone.",
            "Treat yourself with kindness and understanding during moments of sadness.",
            "Set small, achievable goals for the day. Accomplishing even minor tasks can boost your mood."
        ),
        icon = EmojiActionIcons.cryingFaceIcon
    ),
    ANGER(
        4,
        listOf(
            "You might be angry and going through a tough time right now.",
            "It's okay to feel angry right now.",
            "Feeling angry is a sign that you care about something."
        ),
        listOf(
            "Inhale deeply and exhale slowly to calm your anger and regain control.",
            "Take a deep breath and think about whether this situation is worth getting so upset about.",
            "Take a step back, assess the situation objectively, and consider whether there's a different way to interpret it."
        ),
        icon = EmojiActionIcons.angryFaceIcon
    ),
    DISGUST(
        5,
        listOf(
            "Disgust is a natural emotion, but it doesn't have to control you.",
            "Pay attention to your bodily sensations and thoughts related to disgust without judgment.",
            "Feeling disgust is a natural human response."
        ),
        listOf(
            "Identify the specific aspect of the situation causing disgust to gain clarity.",
            "Engage in an activity or focus your attention on something else to divert your thoughts away from the source of disgust.",
            "If possible, remove yourself from the source of disgust or minimize exposure to it. Creating physical distance can help reduce the intensity of the emotion."
        ),
        icon = EmojiActionIcons.confoundedFaceIcon
    ),
    FEAR(
        6,
        listOf(
            "Many people experience anxiety in similar situations.",
            "Fear and anxiety are a natural response to perceived threats. It's okay to feel this way; it's your body's way of keeping you safe.",
            "Anxiety is a common human emotion, and you're not alone in experiencing it."
        ),
        listOf(
            "Objectively evaluate the situation to determine if there is a genuine threat.",
            "Try to reframe your perception of the situation. What is the worst-case scenario, and how likely is it to happen?",
            "Reflect on times when you've overcome similar fears. Use these experiences as sources of strength and confidence."
        ),
        icon = EmojiActionIcons.frowningFaceWithOpenMouthIcon
    ),
    CONTEMPT(
        7,
        listOf(
            "Being open-minded doesn't mean you have to agree and ignore your contempt.",
            "Acknowledging your contempt doesn't mean you have to dwell on it.",
            "Your feelings of contempt are a part of your emotional landscape, and you have the ability to navigate them wisely."
        ),
        listOf(
            "Examine your assumptions and gather more information before passing judgment.",
            "If the contemptuous situation becomes too heated, it's okay to take a break from it to cool off and regain perspective.",
            "Take a moment to reflect on why you're feeling contempt. Understanding the source of your contempt is the first step."
        ),
        icon = EmojiActionIcons.expressionlessFaceIcon
    );

    fun getRandomAffirmDescription() = affirmDescriptions.random()

    fun getRandomAdviceDescription() = adviceDescriptions.random()
}
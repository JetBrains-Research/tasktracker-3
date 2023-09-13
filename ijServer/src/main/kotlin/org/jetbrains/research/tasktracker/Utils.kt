package org.jetbrains.research.tasktracker

import org.jetbrains.research.tasktracker.database.models.ResearchTable
import java.util.concurrent.atomic.AtomicInteger


private lateinit var currentUserIdAtomic: AtomicInteger
private lateinit var currentResearchIdAtomic: AtomicInteger

val currentUserId: Int
    get() = currentResearchIdAtomic.getAndIncrement()

val currentResearchId: Int
    get() = currentResearchIdAtomic.getAndIncrement()

fun initializeIds() {
    currentUserIdAtomic = AtomicInteger(ResearchTable.maxUserId() + 1)
    currentResearchIdAtomic = AtomicInteger(ResearchTable.maxId() + 1)
}


package org.jetbrains.research.tasktracker.requests

import io.ktor.client.call.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.Research
import org.jetbrains.research.tasktracker.utils.createResearchRequest
import org.jetbrains.research.tasktracker.utils.createUserRequest
import org.jetbrains.research.tasktracker.utils.testApplicationRouted
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ResearchRequestTest {

    @Test
    fun getResearchTest() = testApplicationRouted {
        client.createUserRequest("example", "example@example.example")
        client.createResearchRequest("research", 1).apply {
            assertEquals(HttpStatusCode.Created, status)
            assertEquals(1, body<Int>())
        }
        transaction {
            assertEquals(1L, Research.all().count())
            val research = Research.findById(1)
            assertNotNull(research)
            assertEquals("research", research.name)
            assertNull(research.description)
        }
        client.createResearchRequest("research", 1, "description").apply {
            assertEquals(HttpStatusCode.Created, status)
            assertEquals(2, body<Int>())
        }
        transaction {
            assertEquals(2L, Research.all().count())
            val research = Research.findById(2)
            assertNotNull(research)
            assertEquals("research", research.name)
            assertEquals("description", research.description)
        }
    }
}

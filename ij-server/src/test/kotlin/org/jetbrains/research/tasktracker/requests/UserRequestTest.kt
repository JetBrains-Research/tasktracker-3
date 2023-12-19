package org.jetbrains.research.tasktracker.requests

import io.ktor.client.call.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.User
import org.jetbrains.research.tasktracker.utils.createUserRequest
import org.jetbrains.research.tasktracker.utils.testApplicationRouted
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserRequestTest {

    @Test
    fun singleCreationTest() = testApplicationRouted {
        val id = client.createUserRequest("example", "example@example.example")
            .apply {
                assertEquals(HttpStatusCode.OK, status)
            }.body<Int>()
        transaction {
            val user = User.findById(id)
            assertNotNull(user)
            assertEquals("example", user.name)
            assertEquals("example@example.example", user.email)
        }
    }

    @Test
    fun multiCreationTest() = testApplicationRouted {
        val id1 = client.createUserRequest("example1", "example1@example.example")
            .apply {
                assertEquals(HttpStatusCode.OK, status)
            }.body<Int>()
        val id2 = client.createUserRequest("example2", "example2@example.example")
            .apply {
                assertEquals(HttpStatusCode.OK, status)
            }.body<Int>()
        transaction {
            assertNotNull(User.findById(id1))
            assertNotNull(User.findById(id2))
        }
    }

    @Test
    fun creationExistingTest() = testApplicationRouted {
        client.createUserRequest("example", "example@example.example")
        val size = transaction { User.all().count() }
        client.createUserRequest("example", "example@example.example")
            .apply {
                assertEquals(HttpStatusCode.OK, status)
            }
        transaction {
            assertEquals(size, User.all().count())
        }
    }

    @Test
    fun `creation with same email and name test`() = testApplicationRouted {
        client.createUserRequest("example", "example@example.example")
        val size = transaction { User.all().count() }
        client.createUserRequest("example", "example@example.example").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        transaction {
            assertEquals(size, User.all().count())
        }
    }
}

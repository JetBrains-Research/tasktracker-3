package org.jetbrains.research.tasktracker.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.ActivityFileTable
import org.jetbrains.research.tasktracker.database.models.DocumentFileTable
import org.jetbrains.research.tasktracker.database.models.Researches

object DatabaseFactory {
    fun init() {
        val database = Database.connect(
            url = System.getenv("POSTGRES_URL"),
            driver = "org.postgresql.Driver",
            user = System.getenv("POSTGRES_USER"),
            password = System.getenv("POSTGRES_PASSWORD")
        )
        transaction(database) {
            SchemaUtils.create(ActivityFileTable)
            SchemaUtils.create(DocumentFileTable)
            SchemaUtils.create(Researches)
            commit()
        }
    }
}

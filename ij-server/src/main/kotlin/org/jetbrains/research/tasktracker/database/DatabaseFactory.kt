package org.jetbrains.research.tasktracker.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.Researches
import org.jetbrains.research.tasktracker.database.models.Users
import org.jetbrains.research.tasktracker.database.models.data.*

object DatabaseFactory {
    fun init() {
        val database = Database.connect(
            url = System.getenv("DB_HOST"),
            driver = "org.postgresql.Driver",
            user = System.getenv("DB_USERNAME"),
            password = System.getenv("DB_PASSWORD")
        )
        transaction(database) {
            arrayOf(
                Users,
                Researches,
                DocumentData,
                ActivityData,
                WebCamData,
                ToolWindowData,
                SurveyData,
                FileEditorData
            ).let {
                SchemaUtils.create(tables = it)
            }
            commit()
        }
    }
}

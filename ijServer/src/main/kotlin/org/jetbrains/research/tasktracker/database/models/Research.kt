package org.jetbrains.research.tasktracker.database.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Research(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Research>(Researches)

    val userId by Researches.userId
    var name by Researches.name
}

object Researches : IntIdTable() {
    val userId = integer("user_id").autoIncrement()
    val name = text("name")
}

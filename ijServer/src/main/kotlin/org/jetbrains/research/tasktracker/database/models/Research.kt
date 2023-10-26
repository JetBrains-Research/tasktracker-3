package org.jetbrains.research.tasktracker.database.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.research.tasktracker.database.models.Research.Companion.referrersOn

class Research(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Research>(Researches)

    var name by Researches.name
    var description by Researches.description
    var user by User referencedOn Researches.user
}

object Researches : IntIdTable() {
    val name = text("name")
    val description = text("description").nullable()
    val user = reference("user", Users)
}

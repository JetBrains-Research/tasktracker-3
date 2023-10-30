package org.jetbrains.research.tasktracker.database.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Research(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Research>(Researches)

    var name by Researches.name
    var description by Researches.description
    var user by User referencedOn Researches.user
}

/**
 * The table with information about the research.
 */
object Researches : IntIdTable() {
    /**
     * Research name.
     */
    val name = text("name")

    /**
     * Research description.
     */
    val description = text("description").nullable()

    /**
     * User who took part in the research.
     */
    val user = reference("user", Users)
}

package org.jetbrains.research.tasktracker.database.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class User(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, User>(Users)

    var name by Users.name
    var email by Users.email
}

object Users : IntIdTable() {
    val name = text("name")
    val email = text("email")

    init {
        uniqueIndex(name, email)
    }
}
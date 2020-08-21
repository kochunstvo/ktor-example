package ru.khekk

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = uuid("id").primaryKey()
    val firstName = text("firstname")
    val lastName = text("lastname")
    val age = integer("age")
}

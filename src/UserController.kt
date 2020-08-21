package ru.khekk

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*
import kotlin.collections.ArrayList

class UserController {

    fun getAll(): ArrayList<User> {
        val users = arrayListOf<User>()
        transaction {
            Users.selectAll().map {
                users.add(
                    User(
                        id = it[Users.id],
                        firstName = it[Users.firstName],
                        lastName = it[Users.lastName],
                        age = it[Users.age],
                    )
                )
            }
        }
        return users
    }

    fun insert(user: UserDTO) {
        transaction {
            Users.insert {
                it[Users.id] = UUID.randomUUID()
                it[Users.age] = user.age
                it[Users.firstName] = user.firstName
                it[Users.lastName] = user.lastName
            }
        }
    }

    fun update(user: UserDTO, id: UUID) {
        transaction {
            Users.update({ Users.id eq id }) {
                it[Users.age] = user.age
                it[Users.firstName] = user.firstName
                it[Users.lastName] = user.lastName
            }
        }
    }

    fun delete(id: UUID) {
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }
}

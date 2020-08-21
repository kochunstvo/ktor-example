package ru.khekk

import java.util.*

data class User(
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String,
    val age: Int
)

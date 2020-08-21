package ru.khekk

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.jackson.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.*
import java.util.UUID

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    initDB()
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    val userController = UserController()

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }

        get("/users") {
            call.respond(userController.getAll())
        }

        post("/users") {
            val userDTO = call.receive<UserDTO>()
            userController.insert(userDTO)
            call.respond(HttpStatusCode.Created)
        }

        put("/users/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val userDTO = call.receive<UserDTO>()
            userController.update(userDTO, id)
            call.respond(HttpStatusCode.OK)
        }

        delete("/users/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"])
            userController.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    config.schema = "public"
    val ds = HikariDataSource(config)
    Database.connect(ds)
}


package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureRouting()
}
//fun Application.module() {
//    configureSecurity()
//    configureSerialization()
//    configureRouting()

//    install(Sessions){
//        cookie<MySession>("MY WORKING SESSION"){
//            cookie.extensions["SameSite"] = "lax"
//        }
//    }

//    install(Authentication){}
//    install(ContentNegotiation){
//        gson {
//
//        }
//    }

//    routing {
//        get("/") {
//            call.respondText("Route is working fine !!", contentType = ContentType.Text.Plain)
//        }
//
//        // Path Parameter
//        // Localhost:8085/note/{id}
//        get(path = "/notes/{id}"){
//            val id = call.parameters["id"]
//            call.respond("${id}")
//        }
//
//        // Query Parameters
//        // Localhost:8085/notes?q=value
//        get(path = "/notes"){
//            val id = call.request.queryParameters["id"]
//            call.respond("${id}")
//        }
//
//
//
//        route(path = "/notes"){
//            //Nesting routing:
//            route(path = "/create") {
//
//                // localhost:8085/notes/create
//                post {
//                    val body = call.receive<String>()
//                    call.respond(body)
//                }
//            }
//
//            delete{
//                val body = call.receive<String>()
//                call.respond(body)
//            }
//        }
//    }
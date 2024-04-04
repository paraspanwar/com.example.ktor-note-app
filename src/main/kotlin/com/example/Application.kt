package com.example

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.data.model.MySession
import com.example.data.model.Users
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.repository.DatabaseFactory
import com.example.repository.Repo
import com.example.routes.UserRoutes
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.Authentication
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.collections.set

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

private val logger: Logger = LoggerFactory.getLogger(Application::class.java)


@Suppress("unused")
fun Application.module() {


    DatabaseFactory.init()
    val db = Repo()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    configureSecurity()
    configureSerialization()
    configureRouting()

    install(Sessions) {
        cookie<MySession>("MY WORKING SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

//    install(Locations)
//    install(Authentication) {}
//    install(ContentNegotiation) { gson {} }

    authentication {  }


    routing {

        //Basic Routing syntax.

              //JWT Route - to get access token
//              get("/token"){
//                  val email = call.request.queryParameters["email"]!!
//                  val password = call.request.queryParameters["password"]!!
//                  val userName = call.request.queryParameters["email"]!!
//
//                  val user = Users(email, hashFunction(password),userName)
//                  call.respond(jwtService.generateToken(user))
//              }


//              get("/") {
//                  call.respondText("Route is working fine !!", contentType = ContentType.Text.Plain)
//              }

              // Path Parameter
              // Localhost:8085/note/{id}
//              get(path = "/notes/{id}") {
//                  val id = call.parameters["id"]
//                  call.respond("${id}")
//              }

              // Query Parameters
              // Localhost:8085/notes?q=value
//              get(path = "/notes") {
//                  val id = call.request.queryParameters["id"]
//                  call.respond("${id}")
//              }



        UserRoutes(
            db = db,
            jwtService = jwtService,
            hashFunction = hashFunction
        )
        logger.debug("paras22", "$db + $jwtService  + $hashFunction")


//        route(path = "/notes") {
            //Nesting routing:
//            route(path = "/create") {

                // localhost:8085/notes/create
//                post {
//                    val body = call.receive<String>()
//                    call.respond(body)
//                }
//            }
//
//            delete {
//                val body = call.receive<String>()
//                call.respond(body)
//            }
//        }
    }

}
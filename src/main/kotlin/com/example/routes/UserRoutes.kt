package com.example.routes

import com.example.authentication.JwtService
import com.example.data.model.LoginRequest
import com.example.data.model.RegisterRequest
import com.example.data.model.SimpleResponse
import com.example.data.model.Users
import com.example.repository.Repo
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"


@Location(REGISTER_REQUEST)
class UserRegisterRoute

@Location(LOGIN_REQUEST)
class UserLoginRoute

fun Route.UserRoutes(
    db: Repo,
    jwtService: JwtService,
    hashFunction: (String) -> String
) {
    post<UserRegisterRoute> {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(success = false, "Missing Some Fields"))
            return@post
        }

        try {
            val user = Users(registerRequest.email, hashFunction(registerRequest.password), registerRequest.name)
            db.addUser(user = user)
            call.respond(HttpStatusCode.OK, SimpleResponse(success = true, jwtService.generateToken(user)))
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.Conflict,
                SimpleResponse(success = false, message = e.message ?: "Some Problem occurs")
            )
        }
    }

    post<UserLoginRoute> {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(success = false, message = "Missing Some Fields"))
            return@post
        }

        try {
            val user = db.findUserByEmail(loginRequest.email)

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(success = false, message = "Wrong Email Id"))
            } else {
                if (user.hashPassword == hashFunction(loginRequest.password)) {
                    call.respond(
                        HttpStatusCode.OK,
                        SimpleResponse(success = true, message = jwtService.generateToken(user))
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        SimpleResponse(success = false, message = "Password Incorrect!")
                    )
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(success = false, message = "Some Problem Occurs"))
        }
    }
}
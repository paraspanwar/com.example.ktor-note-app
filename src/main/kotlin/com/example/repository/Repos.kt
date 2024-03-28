package com.example.repository

import com.example.data.model.Users
import com.example.data.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class Repo {

    /**
     * This function will insert the users
     * param will accept the users
     * dbQuery will map the users credential in UserTable
     */
    suspend fun addUser(user: Users) {
        dbQuery {
            UserTable.insert { table ->
                table[UserTable.email] = user.email
                table[UserTable.hashPassword] = user.hashPassword
                table[UserTable.name] = user.userName

            }
        }
    }

    /**
     * Find/Query for the user by its email in UserTable
     * This will provide the Row of records
     */
    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    /**
     * This will provide the user from the User row of table
     */
    private fun rowToUser(row: ResultRow?): Users? {
        if (row == null){
            return null
        }
        return Users(
            email = row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            userName = row[UserTable.name]
        )
    }
}
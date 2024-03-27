package com.example.repository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.h2.engine.Database
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    // connection call
    fun init(){
        Database.connect(hikari())
    }

    // Request Builder.
    fun hikari(): HikariDataSource{
        val config = HikariConfig()

        config.driverClassName = System.getenv("JDBC_DRIVER") // 1
        config.jdbcUrl = System.getenv("JDBC_DATABASE_URL") // 2
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)
    }

    // CRUDE operation extension function
    suspend fun <T> dbQuery(block:() -> T): T =
        withContext(Dispatchers.IO){
            transaction { block() }
        }
}
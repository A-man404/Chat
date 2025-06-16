package com.example.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.Dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import table.Users

class DatabaseFactory {


    private val dotenv = Dotenv.load()

    val database = HikariConfig().apply {

        driverClassName = "org.postgresql.Driver"
        jdbcUrl = dotenv["DATABASE_URL"]
        username = dotenv["DATABASE_USER"]
        password = dotenv["DATABASE_PASSWORD"]
        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }

    private val dataSource = HikariDataSource(database)

    val databaseConnection = Database.connect(dataSource)

    fun init() {
        transaction {
            SchemaUtils.create(Users)
        }
    }

}
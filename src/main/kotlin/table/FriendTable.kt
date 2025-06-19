package com.example.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp
import table.Users


object BlockedUsers : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("userId")
    val blockedUserId = integer("blockUserId")
    val blockedTime = timestamp("blockedTime").defaultExpression(CurrentTimestamp())
    override val primaryKey = PrimaryKey(Users.id)

}
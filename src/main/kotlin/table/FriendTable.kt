package com.example.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp
import table.Users


object BlockedUsers : Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("userid")
    val blockedUserId = integer("blockUserid")
    val blockedTime = timestamp("blockedtime").defaultExpression(CurrentTimestamp())
    override val primaryKey = PrimaryKey(Users.id)

}

object FriendRequestTable : Table() {
    val reqId = integer("reqid").autoIncrement()
    val senderId = integer("senderid")
    val receiverId = integer("receiverid")
    val sentTime = timestamp("senttime").defaultExpression(CurrentTimestamp())
}
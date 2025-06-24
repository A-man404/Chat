package com.example.repository

import com.example.model.AddFriend
import com.example.table.FriendTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class FriendRepository {

    fun addFriend(addFriend: AddFriend): Int {
        return transaction {
            FriendTable.insert {
                it[FriendTable.userId] = addFriend.userId
                it[FriendTable.friendId] = addFriend.friendId
            } get FriendTable.id
        }
    }

}
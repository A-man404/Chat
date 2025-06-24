package com.example.repository

import com.example.model.AddFriend
import com.example.table.FriendTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
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

    fun removeFriend(addFriend: AddFriend): Int {
        return transaction {
            FriendTable.deleteWhere {
                (FriendTable.userId eq addFriend.userId) and
                        (FriendTable.friendId eq addFriend.friendId)
            }
        }
    }

    fun alreadyFriend(addFriend: AddFriend): Boolean {
        return transaction {
            FriendTable.selectAll().where {
                (FriendTable.userId eq addFriend.userId) and
                        (FriendTable.friendId eq addFriend.friendId)
            }.limit(1).any()
        }
    }

    fun friendList(id: Int) {
        return transaction {
            FriendTable.selectAll().where {
                FriendTable.userId eq id
            }
        }
    }


}
package com.example.repository

import com.example.model.FriendRequest
import com.example.table.FriendRequestTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class FriendRequestRepository {

    fun addFriendRequest(friendRequest: FriendRequest): Int {
        return transaction {
            FriendRequestTable.insert {
                it[FriendRequestTable.receiverId] = friendRequest.receiverId
                it[FriendRequestTable.senderId] = friendRequest.senderId
            } get FriendRequestTable.reqId
        }
    }

    fun removeFriendRequest(friendRequest: FriendRequest): Int {
        return transaction {
            FriendRequestTable.deleteWhere {
                (FriendRequestTable.receiverId eq friendRequest.receiverId) and
                        (FriendRequestTable.senderId eq friendRequest.senderId)
            }
        }
    }

    


}


//TODO:  ACCEPT REJECT
//TODO: ADD CANCEL

//jab mai request bhjunga to check senderid = mine and uthani hai receiver id
//jab mai request check krungha to check receiver = mine uthani hai sender id

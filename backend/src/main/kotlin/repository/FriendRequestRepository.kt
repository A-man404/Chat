package com.example.repository

import com.example.model.FriendRequest
import com.example.model.FriendRequestDTO
import com.example.table.FriendRequestTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
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

    fun checkFriendRequest(friendRequest: FriendRequest): Boolean {
        return transaction {
            FriendRequestTable.selectAll().where {
                (FriendRequestTable.senderId eq friendRequest.senderId) and
                        (FriendRequestTable.receiverId eq friendRequest.receiverId)
            }.empty().not()
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

    fun getFriendRequestsList(id: Int): List<FriendRequestDTO> {
        val result = mutableListOf<FriendRequestDTO>()
        transaction {
            val connection = TransactionManager.current().connection
            val sql = """
            SELECT 'SEND' AS type, receiverid AS otherid, 
                   (SELECT username FROM users WHERE id = friendrequest.receiverid) AS name, 
                   (SELECT profileimage FROM users WHERE id = friendrequest.receiverid) AS profileimage
            FROM friendrequest 
            WHERE senderid = ?
            UNION ALL
            SELECT 'RECEIVE' AS type, senderid AS otherid, 
                   (SELECT username FROM users WHERE id = friendrequest.senderid) AS name, 
                   (SELECT profileimage FROM users WHERE id = friendrequest.senderid) AS profileimage
            FROM friendrequest 
            WHERE receiverid = ?
        """.trimIndent()

            val stmt = connection.prepareStatement(sql, false)
            stmt[1] = id
            stmt[2] = id
            val rs = stmt.executeQuery()

            while (rs.next()) {
                result.add(
                    FriendRequestDTO(
                        type = rs.getString("type"),
                        otherId = rs.getInt("otherid"),
                        name = rs.getString("name"),
                        profileImage = rs.getString("profileimage")
                    )
                )
            }
            rs.close()
            stmt.cancel()
        }
        return result
    }


}


//TODO:  ACCEPT REJECT
//TODO: ADD CANCEL

//jab mai request bhjunga to check senderid = mine and uthani hai receiver id
//jab mai request check krungha to check receiver = mine uthani hai sender id

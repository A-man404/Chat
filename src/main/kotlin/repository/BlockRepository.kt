package com.example.repository

import com.example.model.BlockRequest
import com.example.table.BlockedUsers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class BlockRepository {

    fun blockUser(blockRequest: BlockRequest): Int {
        return transaction {
            BlockedUsers.insert {
                it[BlockedUsers.userId] = blockRequest.userId
                it[BlockedUsers.blockedUserId] = blockRequest.blockedUserId
            } get BlockedUsers.id
        }
    }

    fun unblockUser(blockRequest: BlockRequest): Int {
        return transaction {
            BlockedUsers.deleteWhere {
                (BlockedUsers.userId eq blockRequest.userId) and
                        (BlockedUsers.blockedUserId eq blockRequest.blockedUserId)
            }
        }
    }

    fun userAlreadyBlocked(blockRequest: BlockRequest): Boolean {
        return transaction {
            BlockedUsers.selectAll().where {
                (BlockedUsers.userId eq blockRequest.userId) and
                        (BlockedUsers.blockedUserId eq blockRequest.blockedUserId)
            }.limit(1).any()
        }

    }
}
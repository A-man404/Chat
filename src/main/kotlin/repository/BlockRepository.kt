package com.example.repository

import com.example.model.BlockRequest
import com.example.model.BlockedUsersList
import com.example.table.BlockedUsers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import table.Users

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

    fun getBlockedUsers(userId: Int): List<BlockedUsersList> {
        return transaction {
            BlockedUsers
                .join(
                    Users, JoinType.LEFT,
                    additionalConstraint = { Users.id eq BlockedUsers.blockedUserId })
                .select(Users.username, BlockedUsers.blockedUserId)
                .where(BlockedUsers.userId eq userId)
                .map {
                    BlockedUsersList(
                        name = it[Users.username],
                        id = it[BlockedUsers.blockedUserId]
                    )
                }
        }.toList()
    }
}

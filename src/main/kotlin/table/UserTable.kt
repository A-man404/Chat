package table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp


object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 25).uniqueIndex()

    val email = varchar("email", 30).uniqueIndex()
    val mobile = varchar("mobile", 20).uniqueIndex()
    val hashedPassword = varchar("password", 200)
    val firstName = varchar("firstName", 50)
    val profilePhoto = varchar("profileImage", 100)
    val lastName = varchar("lastName", 50).nullable()
    val bio = varchar("bio", 255).default("")
    val isVerified = bool("isVerified").default(false)
    val role = enumerationByName("role", 10, ROLE::class).default(ROLE.USER)
    val createdAt = timestamp("createdAt").defaultExpression(CurrentTimestamp())
    val updatedAt = timestamp("updatedAt").defaultExpression(CurrentTimestamp())
    val accountStatus = enumerationByName("accountStatus", 15, AccountStatus::class).default(AccountStatus.ACTIVE)
    override val primaryKey = PrimaryKey(id)

}

enum class ROLE { ADMIN, USER }

enum class AccountStatus { ACTIVE, DEACTIVATED, BANNED }
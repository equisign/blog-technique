package equisign.validation.repository

import equisign.validation.dto.UserDto
import org.springframework.stereotype.Component

@Component
class UserRepository {

    private val userDb: MutableMap<String, UserDto> = mutableMapOf()

    fun save(user: UserDto) {
        require(null == userDb.putIfAbsent(user.username!!, user)) {
            "User ${user.username} already exists"
        }
    }

    fun update(username: String, user: UserDto) {
        if (user.username != username) {
            require(!userDb.containsKey(user.username!!)) {
                "User ${user.username} already exists"
            }
        }
        require(userDb.containsKey(user.username!!)) {
            "User ${user.username} not found"
        }
        userDb[user.username!!] = user
    }

    fun findAll(limit: Int?): List<UserDto> {
        return userDb.values.toList().let {
            if (limit != null && limit < it.size)
                it.subList(0, limit)
            it
        }
    }

    fun findByUsername(username: String): UserDto? {
        return userDb[username]
    }

    fun deleteByUsername(username: String) {
        requireNotNull(userDb.remove(username)) {
            "User $username not found"
        }
    }

}

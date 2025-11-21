package equisign.validation.service

import equisign.validation.dto.UserDto
import equisign.validation.getLog
import equisign.validation.groups.LogOnly
import equisign.validation.repository.UserRepository
import jakarta.validation.Validator
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val validator: Validator
) {
    private val log = getLog<UserService>()

    fun createUser(userDto: UserDto): UserDto {
        logConstraintViolations(userDto)
        userRepository.save(userDto)
        return requireNotNull(userRepository.findByUsername(userDto.username!!)) {
            "Could not find created user" // Unlikely
        }
    }

    fun listUsers(limit: Int?): List<UserDto> {
        return userRepository.findAll(limit)
    }

    fun readUser(username: String): UserDto {
        return requireNotNull(userRepository.findByUsername(username)) {
            "User $username not found"
        }
    }

    fun updateUser(username: String, userDto: UserDto): UserDto {
        logConstraintViolations(userDto)
        userRepository.update(username, userDto)
        return requireNotNull(userRepository.findByUsername(userDto.username!!)) {
            "Could not find updated user" // Unlikely
        }
    }

    fun deleteUser(username: String) {
        userRepository.deleteByUsername(username)
    }

    private fun logConstraintViolations(userDto: UserDto) {
        val constraintViolations = validator.validate(userDto, LogOnly::class.java)

        for (constraintViolation in constraintViolations) {
            log.warn("LogOnly constraint violation: ${constraintViolation.message}")
        }
    }

}
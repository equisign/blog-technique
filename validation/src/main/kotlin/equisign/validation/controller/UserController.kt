package equisign.validation.controller

import equisign.validation.dto.UserDto
import equisign.validation.groups.Create
import equisign.validation.groups.Update
import equisign.validation.service.UserService
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import jakarta.validation.groups.Default
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * Your typical CRUD controller, with validation
 */
@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/users")
    fun createUser(
        @Validated(Default::class, Create::class)
        @RequestBody
        userDto: UserDto
    ): UserDto {
        return userService.createUser(userDto)
    }

    @GetMapping("/users")
    fun readUsers(
        @RequestParam(name = "max", required = false)
        @Min(1) @Max(150) limit: Int?,
    ): List<UserDto> {
        return userService.listUsers(limit)
    }

    @GetMapping("/users/{username}")
    fun readUser(
        @PathVariable
        @Size(min = 3, max = 64) username: String
    ): UserDto {
        return userService.readUser(username)
    }

    @PutMapping("/users/{username}")
    fun updateUser(
        @PathVariable
        @Size(min = 3, max = 64) username: String,

        @Validated(Default::class, Update::class)
        @RequestBody
        userDto: UserDto
    ): UserDto {
        return userService.updateUser(username, userDto)
    }

    @DeleteMapping("/users/{username}")
    fun deleteUser(
        @PathVariable("username")
        @Size(min= 3, max = 64) toDelete: String
    ) {
        userService.deleteUser(toDelete)
    }

}

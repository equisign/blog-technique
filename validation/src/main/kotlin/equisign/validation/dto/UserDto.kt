package equisign.validation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import equisign.validation.custom.Password
import equisign.validation.groups.Create
import equisign.validation.groups.LogOnly
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/** DTO used for Create, Read and Update */
class UserDto {
    @field:Size(min= 3, max = 64)
    var username: String? = null

    @field:Size(min = 1, max = 128)
    var firstname: String? = null

    @field:Size(min = 1, max = 128)
    var lastname: String? = null

    // Scenario: password in DTO only for Create (not returned by Read, therefore not required for Update)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @field:NotNull(groups = [Create::class])
    @field:Password // Scenario: custom Password Validator with injected dependencies
    var password: String? = null

    @field:Email
    var email: String? = null

    // Scenario: cascade validation
    @field:Valid
    var address: AddressDto? = null

    @field:Pattern(regexp = "^[-+0-9]{5,16}$")
    // Scenario: Stricter pattern, log only for now, to refine this pattern
    @field:Pattern(
        groups = [LogOnly::class],
        regexp = "^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$")
    // Scenario: JSON name is 'phone_number' because default property naming strategy is set to SNAKE_CASE
    var phoneNumber: String? = null
}

package equisign.validation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

class AddressDto {
    @field:Size(min = 1, max = 256)
    var street: String? = null

    // Scenario: local property name override
    @field:JsonProperty("postal_code")
    @field:Size(min = 1, max = 16)
    var zipcode: String? = null

    @field:Size(min = 1, max = 256)
    var city: String? = null

    @field:Size(min = 1, max = 32)
    var country: String? = null
}

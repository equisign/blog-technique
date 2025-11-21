package equisign.validation.dto

import java.time.ZoneOffset
import java.time.ZonedDateTime

class ErrorDto(
    var errorName: String,
    var errorMessage: String
) {
    var timestamp: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)
}

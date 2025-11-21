package equisign.validation.conf.password

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties(prefix = "password")
class PasswordConfig (
    val allowSpaces: Boolean = true,
    @DefaultValue val length: PasswordLength = PasswordLength(),
    val entropyThreshold: Double = .0,
    val requireDigit: Int = 0,
    val requireSpecial: Int = 0,
)

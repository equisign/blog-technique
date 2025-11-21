package equisign.validation.custom

import equisign.validation.conf.password.PasswordConfig
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.passay.PasswordData
import org.passay.PasswordValidator
import org.springframework.stereotype.Component

/** Example of custom validator with injected dependencies */
@Component
class CustomPasswordValidator(
    private val passwordValidator: PasswordValidator,
    private val passwordConfig: PasswordConfig
) : ConstraintValidator<Password, String?> {

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext
    ): Boolean {
        // Ignore null, @NotNull or NotEmpty the field instead
        if (value == null) return true

        val data = PasswordData(value)
        val res = passwordValidator.validate(data)

        if (res.isValid) {
            // Entropy check
            if (passwordConfig.entropyThreshold > .0) {
                val entropy = passwordValidator.estimateEntropy(data)
                return entropy > passwordConfig.entropyThreshold
            }
            return true
        }

        // Handle constraint violations ourself
        context.disableDefaultConstraintViolation()
        passwordValidator.getMessages(res).forEach {
            val message = it.substring(0, it.length - 1) // Remove trailing dot
            context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
        }

        return false
    }

}

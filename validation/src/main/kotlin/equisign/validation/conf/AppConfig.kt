package equisign.validation.conf

import equisign.validation.conf.password.PasswordConfig
import org.hibernate.validator.HibernateValidator
import org.hibernate.validator.HibernateValidatorConfiguration
import org.passay.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class AppConfig {

    @Bean
    fun validator(
        propertyNodeNameProvider: JacksonPropertyNodeNameProvider
    ): LocalValidatorFactoryBean {
        return LocalValidatorFactoryBean().apply {
            setProviderClass(HibernateValidator::class.java) // HibernateValidator
            setConfigurationInitializer {
                val conf = it as HibernateValidatorConfiguration
                conf.propertyNodeNameProvider(propertyNodeNameProvider)
            }
        }
    }

    @Bean
    fun passwordValidator(config: PasswordConfig): PasswordValidator {
        // Load password policies from the configuration to customize our CustomPasswordValidator
        val rules = mutableListOf<Rule>()

        rules.add(LengthRule(config.length.min, config.length.max))

        if (config.requireDigit > 0) {
            rules.add(CharacterRule(EnglishCharacterData.Digit, config.requireDigit))
        }

        if (config.requireSpecial > 0) {
            rules.add(CharacterRule(EnglishCharacterData.Special, config.requireSpecial))
        }

        if (!config.allowSpaces) rules.add(WhitespaceRule())

        return PasswordValidator(rules)
    }
}

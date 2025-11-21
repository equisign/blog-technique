package equisign.validation

import equisign.validation.conf.password.PasswordConfig
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

inline fun <reified T> getLog(): Log = LogFactory.getLog(T::class.java)

@SpringBootApplication
@EnableConfigurationProperties(PasswordConfig::class)
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}

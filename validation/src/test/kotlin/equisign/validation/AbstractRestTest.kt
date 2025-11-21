package equisign.validation

import io.restassured.RestAssured
import io.restassured.response.ValidatableResponse
import org.hamcrest.CoreMatchers.endsWith
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.containsString
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractRestTest {

    protected val log = getLog<AbstractRestTest>()

    @LocalServerPort
    protected val port = 0

    /** Checks the status is BAD REQUEST and the response is an ErrorDTO */
    fun ValidatableResponse.isValidationError() {
        statusCode(400)
        body("error_name", equalTo("Validation"))
        body("timestamp", endsWith("Z"))
    }

    /** Checks error_message is equal to the given message */
    fun ValidatableResponse.isValidationError(messageEqTo: String) = isValidationError().also {
        body("error_message", equalTo(messageEqTo))
    }

    /** Checks error_message contains all the given messages */
    fun ValidatableResponse.isValidationError(vararg messageContains: String) = isValidationError().also {
        messageContains.forEach {
            body("error_message", containsString(it))
        }
    }

    companion object {
        init {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }
    }

}

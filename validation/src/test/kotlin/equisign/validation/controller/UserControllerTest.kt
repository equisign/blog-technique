package equisign.validation.controller

import equisign.validation.dto.AddressDto
import equisign.validation.dto.UserDto
import equisign.validation.repository.UserRepository
import equisign.validation.AbstractRestTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest(
    @Autowired val userRepository: UserRepository
): AbstractRestTest() {

    @BeforeAll
    fun setup() {
        log.info("Setup test bed, port=$port")
        // User for the update scenarios
        userRepository.save(UserDto().apply {
            username = "JohnMcAfee"
            firstname = "John"
            lastname = "McAfee"
            email = "john@mcafee.com"
            password = "V3RY Str0n5 P@ssWd"
            phoneNumber = "555-234564"
            address = AddressDto().apply {
                street = "6220 America Center Drive"
                zipcode = "CA 95002"
                city = "San Jose"
                country = "United States"
            }
        })
    }

    @Nested inner class GetUser {

        @Test
        fun `should validation of path param fail min`() {
            Given {
                port(port)
                accept(ContentType.JSON)
            } When {
                get("/users/a")
            } Then {
                isValidationError("path parameter username: size must be between 3 and 64")
            }
        }

        @Test
        fun `should validation of path param fail max`() {
            Given {
                port(port)
                accept(ContentType.JSON)
            } When {
                get("/users/AbigailRoseMarieJeanneFloreBrudeckerVonDutchHallenFriedrickDottir")
            } Then {
                isValidationError("path parameter username: size must be between 3 and 64")
            }
        }
    }

    @Nested inner class DeleteUser {

        @Test
        fun `should validation of path param fail min`() {
            Given {
                port(port)
                accept(ContentType.JSON)
            } When {
                delete("/users/a")
            } Then {
                isValidationError("path parameter username: size must be between 3 and 64")
            }
        }

        @Test
        fun `should validation of path param fail max`() {
            Given {
                port(port)
                accept(ContentType.JSON)
            } When {
                delete("/users/AbigailRoseMarieJeanneFloreBrudeckerVonDutchHallenFriedrickDottir")
            } Then {
                isValidationError("path parameter username: size must be between 3 and 64")
            }
        }
    }

    @Nested inner class ListUsers {

        @Test
        fun `should validation of query param fail min`() {
            Given {
                port(port)
                accept(ContentType.JSON)
            } When {
                get("/users?max=0")
            } Then {
                isValidationError("query parameter max: must be greater than or equal to 1")
            }
        }

        @Test
        fun `should validation of query param fail max`() {
            Given {
                port(port)
                accept(ContentType.JSON)
            } When {
                get("/users?max=151")
            } Then {
                isValidationError("query parameter max: must be less than or equal to 150")
            }
        }

    }

    @Nested inner class CreateUser {

        @Test
        fun `Should validation fail on username too short`() {
            Given {
                port(port)
                body(
                    """
                    {
                        "username": "am",
                        "firstname": "Anne",
                        "lastname": "Martin",
                        "email": "annne@martin.fr",
                        "password": "M0t de P@ss3",
                        "phone_number": "+33654321099",
                        "address": {
                            "street": "5 rue du chat qui pêche",
                            "postal_code": "75005",
                            "city": "Paris",
                            "country": "France"
                        }
                    }
                """.trimIndent()
                )
                contentType(ContentType.JSON)
                accept(ContentType.JSON)
            } When {
                post("/users")
            } Then {
                isValidationError("username: size must be between 3 and 64")
            }
        }

        @Test
        fun `Should validation fail on missing password`() {
            Given {
                port(port)
                body("""
                    {
                        "username": "AnneMartin",
                        "firstname": "Anne",
                        "lastname": "Martin",
                        "email": "annne@martin.fr",
                        "phone_number": "+33654321099"
                    }
                """.trimIndent())
                contentType(ContentType.JSON)
                accept(ContentType.JSON)
            } When {
                post("/users")
            } Then {
                isValidationError("password: must not be null")
            }
        }

        @ParameterizedTest
        @ValueSource(
            strings = [
                "plainaddress",
                "#@%^%#$@#$@#.com",
                "@example.com",
                "Joe Smith <email@example.com>",
                "email.example.com",
                "email@example@example.com",
                "    .email@example.com",
                "email.@example.com",
                "        email..email@example.com",
                "email@example.com (Joe Smith)",
                "email@-example.com",
                "email@example..com",
                "abc..123@example.com",
            ]
        )
        fun `Should validation fail on invalid email`(email: String) {
            Given {
                port(port)
                body(
                    """
                    {
                        "username": "AnneMartin",
                        "firstname": "Anne",
                        "lastname": "Martin",
                        "email": "$email",
                        "password": "M0t de P@ss3",
                        "phone_number": "+33654321099",
                        "address": {
                            "street": "5 rue du chat qui pêche",
                            "postal_code": "75005",
                            "city": "Paris",
                            "country": "France"
                        }
                    }
                """.trimIndent()
                )
                contentType(ContentType.JSON)
                accept(ContentType.JSON)
            } When {
                post("/users")
            } Then {
                isValidationError("email: must be a well-formed email address")
            }
        }
    }

    @Nested inner class UpdateUser {

        @Test
        fun `Should allow update without password`() {
            Given {
                port(port)
                body(
                    """
                    {
                        "username": "JohnMcAfee",
                        "firstname": "John",
                        "lastname": "McAfee",
                        "email": "john@mcafee.com",
                        "phone_number": null,
                        "address": {
                            "street": "Carrer del Doctor Trueta, 84, Sant Martí",
                            "postal_code": "08005",
                            "city": "Barcelona",
                            "country": "Spain"
                        }
                    }
                """.trimIndent()
                )
                contentType(ContentType.JSON)
                accept(ContentType.JSON)
            } When {
                put("/users/JohnMcAfee")
            } Then {
                statusCode(200)
                body("username", equalTo("JohnMcAfee"))
                body("phone_number", nullValue())
            }
        }
    }

    @Nested inner class PasswordValidation {

        @Test
        fun `Should validation fail on password 'querty'`() {
            Given {
                port(port)
                body("""
                    {
                        "username": "AnneMartin",
                        "firstname": "Anne",
                        "lastname": "Martin",
                        "email": "annne@martin.fr",
                        "password": "querty"
                    }
                """.trimIndent())
                contentType(ContentType.JSON)
                accept(ContentType.JSON)
            } When {
                post("/users")
            } Then {
                isValidationError(
                    "password: Password must be 8 or more characters in length",
                    "password: Password must contain 1 or more digit characters",
                    "password: Password must contain 1 or more special characters"
                )
            }
        }

        @Test
        fun `Should validation fail on password 'p@ssw0rd'`() {
            Given {
                port(port)
                body("""
                    {
                        "username": "AnneMartin",
                        "firstname": "Anne",
                        "lastname": "Martin",
                        "email": "annne@martin.fr",
                        "password": "p@ssw0rd"
                    }
                """.trimIndent())
                contentType(ContentType.JSON)
                accept(ContentType.JSON)
            } When {
                post("/users")
            } Then {
                isValidationError("password: Password too simple (password entropy below threshold)")
            }
        }

    }

    @Nested inner class RewriteAttribute {

        @Test
        fun `should fail on invalid phone_number`() {
            Given {
                port(port)
                body(
                    """
                    {
                        "username": "AnneMartin",
                        "firstname": "Anne",
                        "lastname": "Martin",
                        "email": "annne@martin.fr",
                        "password": "M0t de P@ss3",
                        "phone_number": "+3365A321099"
                    }
                """.trimIndent()
                )
                contentType(ContentType.JSON)
                accept(ContentType.JSON)
            } When {
                post("/users")
            } Then {
                isValidationError("""phone_number: must match "^[-+0-9]{5,16}$"""")
            }
        }

        @Test
        fun `should fail on invalid postal_code`() {
            Given {
                port(port)
                body(
                    """
                    {
                        "username": "AnneMartin",
                        "firstname": "Anne",
                        "lastname": "Martin",
                        "email": "annne@martin.fr",
                        "password": "M0t de P@ss3",
                        "phone_number": "+33654321099",
                        "address": {
                            "street": "5 rue du chat qui pêche",
                            "postal_code": "ABCDEFG 75005000500057",
                            "city": "Paris",
                            "country": "France"
                        }
                    }
                """.trimIndent()
                )
                contentType(ContentType.JSON)
                accept(ContentType.JSON)
            } When {
                post("/users")
            } Then {
                isValidationError("address.postal_code: size must be between 1 and 16")
            }
        }
    }

}

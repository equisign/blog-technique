package equisign.validation.custom

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/** Checks that the annotated password complies to password policies */
@MustBeDocumented
@Constraint(validatedBy = [CustomPasswordValidator::class])
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.TYPE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Password(

    /** Default validation message */
    val message: String = "Password too simple (password entropy below threshold)",

    /** Validation groups */
    val groups: Array<KClass<*>> = [],

    /** Payload for metadata */
    val payload: Array<KClass<out Payload>> = []

)

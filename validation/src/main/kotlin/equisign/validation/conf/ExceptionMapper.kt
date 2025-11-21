package equisign.validation.conf

import equisign.validation.dto.ErrorDto
import equisign.validation.getLog
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException

@RestControllerAdvice
class ExceptionMapper {

    val log = getLog<ExceptionMapper>()

    // Programmatic validation
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException) : ResponseEntity<ErrorDto> {
        log.debug("🔎 ConstraintViolationException")
        return ResponseEntity.badRequest()
            .body(ErrorDto("Validation", ex.message ?: ""))
    }

    // @Validated @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException) : ResponseEntity<ErrorDto> {
        log.debug("🔎 MethodArgumentNotValidException")

        // Access the underlying BindingResult
        val violations = ex.bindingResult.fieldErrors.joinToString {
            "${it.field}: ${it.defaultMessage}"
        }

        return ResponseEntity.badRequest()
            .body(ErrorDto("Validation", violations))
    }

    // Constraints on method parameters
    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleHandlerMethodValidationException(ex: HandlerMethodValidationException) : ResponseEntity<ErrorDto> {
        log.debug("🔎 HandlerMethodValidationException")

        val violations = ex.parameterValidationResults.joinToString { validationResult ->
            val param = validationResult.methodParameter

            // Is it a path parameter or a query parameter ?
            val isPathParam = param.hasParameterAnnotation(PathVariable::class.java)

            // Get name override from web annotation
            val paramName =
                param.getParameterAnnotation(PathVariable::class.java)?.name?.takeIf(String::isNotEmpty)
                    ?: param.getParameterAnnotation(RequestParam::class.java)?.name?.takeIf(String::isNotEmpty)
                    ?: param.parameterName

            validationResult.resolvableErrors.joinToString {
                "${if (isPathParam) "path" else "query"} parameter $paramName: ${it.defaultMessage}"
            }
        }

        return ResponseEntity.badRequest()
            .body(ErrorDto("Validation", violations))
    }
}

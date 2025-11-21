package equisign.validation.conf

import org.hibernate.validator.spi.nodenameprovider.JavaBeanProperty
import org.hibernate.validator.spi.nodenameprovider.Property
import org.hibernate.validator.spi.nodenameprovider.PropertyNodeNameProvider
import org.springframework.stereotype.Component
import tools.jackson.databind.BeanProperty
import tools.jackson.databind.JavaType
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper
import tools.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor

@Component
class JacksonPropertyNodeNameProvider(
    private val objectMapper: ObjectMapper
): PropertyNodeNameProvider {

    override fun getName(property: Property): String {
        return (property as? JavaBeanProperty)?.let {
            val visitor = JsonPropertyNameGetter(property.name)
            objectMapper.acceptJsonFormatVisitor(
                property.declaringClass,
                visitor
            )
            visitor.attributeName
        } ?: property.name
    }

}

private class JsonPropertyNameGetter(propertyName: String): JsonFormatVisitorWrapper.Base() {

    private val visitor = JsonPropertyNameMapper(propertyName)

    val attributeName: String?
        get() = visitor.attributeName

    override fun expectObjectFormat(type: JavaType?) = visitor

    class JsonPropertyNameMapper(val propertyName: String): JsonObjectFormatVisitor.Base()
    {
        var attributeName: String? = null

        private fun setName(writer: BeanProperty) {
            if (writer.member.name.equals("get$propertyName", true)) {
                attributeName = writer.name
            }
        }

        override fun property(writer: BeanProperty) = setName(writer)
        override fun optionalProperty(writer: BeanProperty)  = setName(writer)
    }
}

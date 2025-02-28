import com.google.gson.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter(pattern: String) : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    @Suppress("unused") private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private val formatter: DateTimeFormatter = pattern.let { DateTimeFormatter.ofPattern(it) }

    @Throws(JsonParseException::class) override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDateTime {
        return LocalDateTime.parse(json.asString, formatter)
    }

    override fun serialize(dateTime: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(dateTime.format(formatter))
    }
}
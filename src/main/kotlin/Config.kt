import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.stream.JsonReader
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.LocalDateTimeAdapter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

class Config {
    @Suppress("unused") private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Expose var pathDB: String = ""
    @Expose var pathTemplates: String = ""
    @Expose var dateTimeDB: LocalDateTime = LocalDateTime.of(1, 1, 1, 1, 1)

    companion object {
        val pathDirectory = System.getenv("APPDATA") + "\\slacker\\"
        val pathConfig = System.getenv("APPDATA") + "\\slacker\\config.json"
        val pathDBLocal = System.getenv("APPDATA") + "\\slacker\\slacker.db"

        fun load(): Config {
            val directory: File = File(pathDirectory)
            if (!directory.exists()) {
                FileUtils.forceMkdir(directory)
            }
            return if (Files.exists(Paths.get(pathConfig))) {
                val gson: Gson = GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter("dd.MM.yyyy HH:mm:ss"))
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()
                val configServer: Config =
                    gson.fromJson(JsonReader(FileReader(pathConfig)), Config::class.java)
                configServer
            } else {
                Config()
            }
        }
    }

    fun save() {
        val directory: File = File(pathDirectory)
        if (!directory.exists()) {
            FileUtils.forceMkdir(directory)
        }
        FileWriter(pathConfig).use { file ->
            val gson: Gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter("dd.MM.yyyy HH:mm:ss"))
                .excludeFieldsWithoutExposeAnnotation()
                .create()
            file.write(gson.toJson(this, Config::class.java))
            file.flush()
        }
    }
}
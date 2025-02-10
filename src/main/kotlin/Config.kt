import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.stream.JsonReader
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

class Config {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Expose
    var pathDB: String = ""

    @Expose
    var pathTemplates: String = ""

    @Expose
    lateinit var dateTimeDB: LocalDateTime

    fun save() {
        val pathDirectory = System.getenv("APPDATA") + "\\slacker\\"
        val pathConfig = System.getenv("APPDATA") + "\\slacker\\config.json"

        val directory: File = File(pathDirectory)

        if (!directory.exists()) {
            try {
                FileUtils.forceMkdir(directory)
            } catch (e: IOException) {
                logger.error(e.message)
            }
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

    companion object {
        fun load(): Config {
            val pathDirectory = System.getenv("APPDATA") + "\\slacker\\"
            val pathConfig = System.getenv("APPDATA") + "\\slacker\\config.json"

            val directory: File = File(pathDirectory)

            if (!directory.exists()) {
                try {
                    FileUtils.forceMkdir(directory)
                } catch (e: IOException) {
                    logger.error(e.message)
                }
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
}
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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

    lateinit var pathDB: String
    lateinit var pathTemplates: String
    lateinit var dateTimeDB: LocalDateTime

    fun save() {
        val pathDirectory = System.getenv("APPDATA") + "\\Local\\slacker\\"
        val pathConfig = System.getenv("APPDATA") + "\\Local\\slacker\\config.json"

        val directory: File = File(pathDirectory)

        if (!directory.exists()) {
            FileUtils.forceMkdir(directory)
        }

        FileWriter(pathConfig).use { file ->
            val gson: Gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter("dd.MM.yyyy HH:mm:ss"))
                .create()
            file.write(gson.toJson(this, Config::class.java))
            file.flush()
        }
    }

    companion object {
        fun load(): Config {
            val pathConfig = System.getenv("APPDATA") + "\\Local\\slacker\\config.json"
            return if (Files.exists(Paths.get(pathConfig))) {
                val gson: Gson = GsonBuilder().registerTypeAdapter(
                    LocalDateTime::class.java,
                    LocalDateTimeAdapter("dd.MM.yyyy HH:mm:ss")
                ).create()
                val configServer: Config =
                    gson.fromJson(JsonReader(FileReader(pathConfig)), Config::class.java)
                configServer
            } else {
                Config()
            }
        }
    }
}
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

class Config {
    lateinit var pathDB: String
    lateinit var pathTemplates: String
    lateinit var dateTimeDB: LocalDateTime

    fun save(config: Config) {
        val pathConfig = System.getenv("APPDATA") + "\\Local\\slacker\\config.json"
        FileWriter(pathConfig).use { file ->
            val gson: Gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter("dd.MM.yyyy HH:mm:ss"))
                .create()
            file.write(gson.toJson(this, Config::class.java))
            file.flush()
        }
    }

    companion object {
        fun load() : Config {
            val pathConfig = System.getenv("APPDATA") + "\\Local\\slacker\\config.json"
            return if (Files.exists(Paths.get(pathConfig))) {
                val gson: Gson = GsonBuilder().create()
                val configServer: Config =
                    gson.fromJson(JsonReader(FileReader(pathConfig)), Config::class.java)
                configServer
            } else {
                Config()
            }
        }
    }
}
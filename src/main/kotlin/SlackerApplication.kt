import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Thread.sleep

class SlackerApplication : Application() {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        lateinit var slacker: SlackerApplication
    }

    override fun start(stage: Stage) {
        runBlocking {
            launch {
                val fxmlLoader = FXMLLoader(SlackerApplication::class.java.getResource("SlackerApplication.fxml"))
                val scene = Scene(fxmlLoader.load())
                stage.title = "Slacker"
                stage.scene = scene
                stage.show()
            }
            launch {
                sleep(3000)
                Data.companion.config = Config.load()
            }
        }
    }
}

val logger: Logger = LoggerFactory.getLogger("Main")

fun main() {
    Application.launch(SlackerApplication::class.java)
}
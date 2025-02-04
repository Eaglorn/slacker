import javafx.application.Application
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("Main")

class SlackerApplication : Application() {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun start(stage: Stage) {
        runBlocking {
            launch {
                val fxmlLoader = FXMLLoader(SlackerApplication::class.java.getResource("SlackerApplication.fxml"))
                val scene = Scene(fxmlLoader.load())
                stage.title = "Slacker!"
                stage.scene = scene
                stage.show()
            }
        }
    }

    fun onHelloButtonClick(actionEvent: ActionEvent) {

    }
}

fun main() {
    Application.launch(SlackerApplication::class.java)
}
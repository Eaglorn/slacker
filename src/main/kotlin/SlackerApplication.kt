import javafx.application.Application
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SlackerApplication : Application() {
    override fun start(stage: Stage) {
        runBlocking {
            launch {
                val fxmlLoader = FXMLLoader(SlackerApplication::class.java.getResource("SlackerApplication.fxml"))
                val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
                stage.title = "Hello!"
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
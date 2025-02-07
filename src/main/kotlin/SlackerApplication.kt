import controllers.SlackerController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SlackerApplication : Application() {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private lateinit var controller: SlackerController

    private fun beforeShow() {
        controller = Data.companion.controller
        controller.tabWriteOff.disableProperty().set(true)
        controller.tabExpertise.disableProperty().set(true)
        controller.tabDataBase.disableProperty().set(true)
        controller.beforeShow()
    }

    override fun start(stage: Stage) {
        runBlocking {
            launch {
                val fxmlLoader = FXMLLoader(SlackerApplication::class.java.getResource("SlackerApplication.fxml"))
                val scene = Scene(fxmlLoader.load())
                stage.title = "Slacker"
                stage.scene = scene
                Data.companion.scene = scene
                beforeShow()
                stage.show()
            }
        }
    }
}

val logger: Logger = LoggerFactory.getLogger("Main")

fun main() {
    Application.launch(SlackerApplication::class.java)
}
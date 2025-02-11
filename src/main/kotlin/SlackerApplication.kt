import controllers.SlackerController
import db.Maker
import db.TypeOfHardware
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
        val database = SqliteDatabase.connect(Data.config.pathDB)

        database.useConnection { conn ->
            Maker.createDatabase(conn)
            TypeOfHardware.createDatabase(conn)
        }

        controller = Data.controller
        controller.beforeShow()
    }

    override fun start(stage: Stage) {
        runBlocking {
            launch {
                val fxmlLoader = FXMLLoader(SlackerApplication::class.java.getResource("SlackerApplication.fxml"))
                val scene = Scene(fxmlLoader.load())
                stage.title = "Slacker"
                stage.scene = scene
                Data.scene = scene
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
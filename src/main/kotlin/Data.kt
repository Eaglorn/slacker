import controllers.SlackerController
import javafx.scene.Scene
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Data {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    object companion {
        var config: Config = Config()
        lateinit var controller: SlackerController
        lateinit var scene: Scene
    }
}
import controllers.DBMakerController
import controllers.DBTypeOfHardwareController
import controllers.SettingController
import controllers.SlackerController
import javafx.scene.Scene
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Data {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        var config: Config = Config()
        lateinit var controller: SlackerController
        lateinit var settingController: SettingController
        lateinit var dbMakerController: DBMakerController
        lateinit var dbTypeOfHardwareController: DBTypeOfHardwareController
        lateinit var scene: Scene
    }
}
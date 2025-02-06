import javafx.scene.Scene

class Data {
    object companion {
        var config: Config = Config()
        lateinit var controller: SlackerController
        lateinit var scene: Scene
    }
}
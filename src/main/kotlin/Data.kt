import controllers.SettingController
import controllers.SlackerController
import controllers.maker.DBMakerController
import controllers.model.DBModelController
import controllers.typeofhardware.DBTypeOfHardwareController
import controllers.user.DBUserController
import db.Makers
import db.Models
import db.TypeOfHardwares
import db.Users
import javafx.scene.Scene
import org.ktorm.dsl.Query
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.SqliteDatabase

class Data {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        var config : Config = Config()
        lateinit var controller : SlackerController
        lateinit var settingController : SettingController
        lateinit var dbMakerController : DBMakerController
        lateinit var dbTypeOfHardwareController : DBTypeOfHardwareController
        lateinit var dbModelController : DBModelController
        lateinit var dbUserController : DBUserController
        lateinit var scene : Scene
        lateinit var dbMaker : Query
        lateinit var dbTypeOfHardware : Query
        lateinit var dbModel : Query
        lateinit var dbUser : Query

        fun updateDB() {
            val database = SqliteDatabase.connect(Config.pathDBLocal)
            dbMaker = database.from(Makers).select()
            dbTypeOfHardware = database.from(TypeOfHardwares).select()
            dbModel = database.from(Models).select()
            dbUser = database.from(Users).select()
        }
    }
}
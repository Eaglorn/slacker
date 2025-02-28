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

class Data {
    @Suppress("unused") private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        /**
         * Клас настроек приложения.
         */
        var config: Config = Config()

        /**
         * Главный контроллер приложения.
         */
        lateinit var controller: SlackerController

        /**
         * Контроллер шаблона настроек приложения.
         */
        lateinit var settingController: SettingController

        /**
         * Контроллер шаблона базы (производитель).
         */
        lateinit var dbMakerController: DBMakerController

        /**
         * Контроллер шаблона базы (тип оборудования).
         */
        lateinit var dbTypeOfHardwareController: DBTypeOfHardwareController

        /**
         * Контроллер шаблона базы (модель).
         */
        lateinit var dbModelController: DBModelController

        /**
         * Контроллер шаблона базы (составитель).
         */
        lateinit var dbUserController: DBUserController

        /**
         * Отображаемое приложение.
         */
        lateinit var scene: Scene

        /**
         * Классы для работы с базой (производитель).
         */
        lateinit var dbMaker: Query

        /**
         * Классы для работы с базой (тип оборудования).
         */
        lateinit var dbTypeOfHardware: Query

        /**
         * Классы для работы с базой (модель).
         */
        lateinit var dbModel: Query

        /**
         * Классы для работы с базой (составитель).
         */
        lateinit var dbUser: Query

        /**
         * Считывание базы данных и обновление перменных.
         */
        fun updateDB() {
            val database = SqliteDatabase.connect(Config.pathDBLocal)

            dbMaker = database.from(Makers).select()
            dbTypeOfHardware = database.from(TypeOfHardwares).select()
            dbModel = database.from(Models).select()
            dbUser = database.from(Users).select()
        }
    }
}
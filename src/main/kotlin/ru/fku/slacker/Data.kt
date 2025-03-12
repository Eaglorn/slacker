package ru.fku.slacker

import javafx.scene.Scene
import org.controlsfx.control.Notifications
import org.ktorm.dsl.Query
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.controllers.SettingController
import ru.fku.slacker.controllers.SlackerController
import ru.fku.slacker.controllers.defect.DBDefectController
import ru.fku.slacker.controllers.maker.DBMakerController
import ru.fku.slacker.controllers.model.DBModelController
import ru.fku.slacker.controllers.typeofhardware.DBTypeOfHardwareController
import ru.fku.slacker.controllers.user.DBUserController
import ru.fku.slacker.db.*
import ru.fku.slacker.utils.SqliteDatabase

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
        lateinit var dbDefectController : DBDefectController
        lateinit var scene : Scene
        lateinit var dbMaker : Query
        lateinit var dbTypeOfHardware : Query
        lateinit var dbModel : Query
        lateinit var dbUser : Query
        lateinit var dbDefect : Query
        val methodMap : MutableMap<String, (Any) -> Any> = HashMap()
        val messageMap: MutableMap<String, String> = HashMap()

        fun updateDB() {
            SqliteDatabase.connect(Config.pathDBLocal).let {
                dbMaker = it.from(Makers).select()
                dbTypeOfHardware = it.from(TypeOfHardwares).select()
                dbModel = it.from(Models).select()
                dbUser = it.from(Users).select()
                dbDefect = it.from(Defects).select()
            }
        }

        fun reloadTable(vararg names: String) {
            if(names.isNotEmpty()) {
                names.forEach {
                    methodMap["Table.Reload.${it}"]?.invoke("")
                }
            } else {
                methodMap
                    .filter { it.component1().contains("Table.Reload.") }
                    .forEach { it.value.invoke("") }
            }
        }

        fun onButtonClickTable(controller: String, button: String) {
            methodMap["Table.${button}.${controller}"]?.invoke("")
        }

        fun showMessage(level: String, message: String) {
            var notification = Notifications.create()
            when (level) {
                "Warning" -> notification.title("Предупреждение").showWarning()
                "Error" -> notification.title("Ошибка").showError()
                else -> notification.title("Сообщение").showInformation()
            }
        }
    }
}
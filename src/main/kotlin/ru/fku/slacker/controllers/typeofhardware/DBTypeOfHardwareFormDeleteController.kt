package ru.fku.slacker.controllers.typeofhardware

import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.db.Models
import ru.fku.slacker.db.TypeOfHardware
import ru.fku.slacker.db.TypeOfHardwares
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBTypeOfHardwareFormDeleteController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName : TextField

    init {
        Data.dbTypeOfHardwareController.formDeleteController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDelete() {
        if (Data.dbTypeOfHardwareController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        } else {
            runBlocking {
                launch {
                    Data.updateDB()
                    val result = Data.dbTypeOfHardware
                        .where { (TypeOfHardwares.id eq Data.dbTypeOfHardwareController.selectId) }
                        .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                        .firstOrNull()
                    if (result == null) {
                        Notifications.create()
                            .title("Предупреждение!")
                            .text("Запись с выбранным id в базе отсуствует.")
                            .showWarning()
                    } else {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        database.delete(TypeOfHardwares) {
                            it.id eq result.id !!
                        }
                        database.delete(Models) {
                            it.type_of_hardware_id eq result.id !!
                        }
                        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                        Data.run {
                            updateDB()
                            reloadTable("TypeOfHardware", "Model")
                            dbTypeOfHardwareController.formStage.close()
                        }
                    }
                }
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbTypeOfHardwareController.formStage.close()
    }
}

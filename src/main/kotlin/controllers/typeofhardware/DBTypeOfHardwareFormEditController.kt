package controllers.typeofhardware

import Config
import Data
import db.TypeOfHardware
import db.TypeOfHardwares
import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.update
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.SqliteDatabase
import java.io.File

class DBTypeOfHardwareFormEditController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName : TextField

    init {
        Data.dbTypeOfHardwareController.formEditController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickEdit() {
        if (Data.dbTypeOfHardwareController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        } else {
            runBlocking {
                launch {
                    Data.updateDB()
                    val result =
                        Data.dbTypeOfHardware
                            .where { (TypeOfHardwares.id eq Data.dbTypeOfHardwareController.selectId) }
                            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                            .firstOrNull()
                    if (result == null) {
                        Notifications.create()
                            .title("Предупреждение!")
                            .text("Запись с выбранным id в базе отсуствует.")
                            .showWarning()
                    } else {
                        val result1 = Data.dbTypeOfHardware
                            .where { (TypeOfHardwares.name eq fieldName.text) }
                            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                            .firstOrNull()
                        if (result1 == null) {
                            val database = SqliteDatabase.connect(Data.config.pathDB)
                            database.update(TypeOfHardwares) {
                                set(it.name, fieldName.text)
                                where { it.id eq result.id !! }
                            }
                            FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                            Data.run {
                                updateDB()
                                dbModelController.reloadTable()
                                dbTypeOfHardwareController.run {
                                    reloadTable()
                                    formStage.close()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbMakerController.formStage.close()
    }
}

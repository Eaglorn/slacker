package controllers

import Data
import SqliteDatabase
import db.TypeOfHardware
import db.TypeOfHardwares
import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class DBTypeOfHardwareFormDeleteController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldID: TextField

    @FXML
    lateinit var fieldName: TextField

    init {
        Data.dbTypeOfHardwareController.formDeleteController = this
    }

    @FXML
    private fun onButtonClickDelete() {
        if (Data.dbTypeOfHardwareController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        }
        runBlocking {
            launch {
                val database = SqliteDatabase.connect(Data.config.pathDB)
                val query = database.from(TypeOfHardwares).select()

                val result = query
                    .where { (TypeOfHardwares.id eq Data.dbTypeOfHardwareController.selectId) }
                    .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                    .firstOrNull()

                if (result == null) {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись с выбранным id в базе отсуствует.")
                        .showWarning()
                } else {
                    database.delete(TypeOfHardwares) {
                        it.id eq result.id!!
                    }
                    FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                    Data.dbTypeOfHardwareController.reloadTable()
                    Data.dbTypeOfHardwareController.buttonTableTypeOfHardwareEdit.disableProperty().set(true)
                    Data.dbTypeOfHardwareController.buttonTableTypeOfHardwareDelete.disableProperty().set(true)
                    Data.dbTypeOfHardwareController.formStage.close()
                }
            }
        }
    }

    @FXML
    private fun onButtonClickCancel() {
        Data.dbTypeOfHardwareController.formStage.close()
    }
}

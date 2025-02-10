package controllers

import Data
import SqliteDatabase
import db.TypeOfHardware
import db.TypeOfHardwares
import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.controlsfx.control.Notifications
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DBTypeOfHardwareFormDeleteController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var data: Data.companion = Data.companion

    @FXML
    lateinit var fieldID: TextField

    @FXML
    lateinit var fieldName: TextField

    init {
        data.dbTypeOfHardwareController.formDeleteController = this
    }

    @FXML
    private fun onButtonClickDelete() {
        if (data.dbTypeOfHardwareController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        }
        runBlocking {
            launch {
                val database = SqliteDatabase().connect()
                val query = database.from(TypeOfHardwares).select()

                val result = query
                    .where { (TypeOfHardwares.id eq data.dbTypeOfHardwareController.selectId) }
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
                    data.dbTypeOfHardwareController.reloadTable()
                    data.dbTypeOfHardwareController.buttonTableTypeOfHardwareEdit.disableProperty().set(true)
                    data.dbTypeOfHardwareController.buttonTableTypeOfHardwareDelete.disableProperty().set(true)
                    data.dbTypeOfHardwareController.formStage.close()
                }
            }
        }
    }

    @FXML
    private fun onButtonClickCancel() {
        data.dbTypeOfHardwareController.formStage.close()
    }
}

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

class DBTypeOfHardwareFormAddController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var data: Data.companion = Data.companion

    @FXML
    private lateinit var fieldName: TextField

    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                val database = SqliteDatabase().connect()
                val query = database.from(TypeOfHardwares).select()

                val result = query
                    .where { (TypeOfHardwares.name eq fieldName.text) }
                    .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                    .firstOrNull()

                if (result == null) {
                    database.insert(TypeOfHardwares) {
                        set(it.name, fieldName.text)
                    }
                    data.dbTypeOfHardwareController.reloadTable()
                    data.dbTypeOfHardwareController.buttonTableTypeOfHardwareEdit.disableProperty().set(true)
                    data.dbTypeOfHardwareController.buttonTableTypeOfHardwareDelete.disableProperty().set(true)
                    data.dbTypeOfHardwareController.formStage.close()
                } else {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись производитель с введённым названием уже существует.")
                        .showWarning()
                }
            }
        }
    }

    @FXML
    private fun onButtonClickCancel() {
        data.dbTypeOfHardwareController.formStage.close()
    }
}
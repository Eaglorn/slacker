package controllers

import Data
import SqliteDatabase
import db.Maker
import db.Makers
import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.controlsfx.control.Notifications
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DBMakerFormAddController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var data: Data.companion = Data.companion

    @FXML
    private lateinit var fieldName: TextField

    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                val database = SqliteDatabase().connect()
                val query = database.from(Makers).select()

                val result = query
                    .where { (Makers.name eq fieldName.text) }
                    .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                    .firstOrNull()

                if (result == null) {
                    database.insert(Makers) {
                        set(it.name, fieldName.text)
                    }
                    data.dbMakerController.reloadTable()
                    data.dbMakerController.buttonTableMakerEdit.disableProperty().set(true)
                    data.dbMakerController.buttonTableMakerDelete.disableProperty().set(true)
                    data.dbMakerController.formStage.close()
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
        data.dbMakerController.formStage.close()
    }
}
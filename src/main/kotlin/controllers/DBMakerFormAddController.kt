package controllers

import SqliteDatabase
import db.Maker
import db.MakerTable
import db.Makers
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.controlsfx.control.Notifications
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.text.insert

class DBMakerFormAddController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private lateinit var fieldName: TextField

    @FXML
    private lateinit var buttonAdd: Button

    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                val database = SqliteDatabase().connect()
                var query = database.from(Makers).select()

                val result = query
                    .where { (Makers.name eq fieldName.text) }
                    .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                    .firstOrNull()

                if (result == null) {
                    database.insert(Makers) {
                        set(it.name, fieldName.text)
                    }
                    Data.companion.controller.dbMakerController.formStage.close()
                } else {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись производитель с введённым названием уже существует.")
                        .showWarning()
                }

                Data.companion.controller.dbMakerController.reloadTable()
            }
        }
    }

    @FXML
    private fun onButtonClickCancel() {
        Data.companion.controller.dbMakerController.formStage.close()
    }
}
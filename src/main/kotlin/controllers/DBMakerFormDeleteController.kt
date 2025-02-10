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

class DBMakerFormDeleteController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var data: Data.companion = Data.companion

    @FXML
    lateinit var fieldID: TextField

    @FXML
    lateinit var fieldName: TextField

    init {
        data.dbMakerController.formDeleteController = this
    }

    @FXML
    private fun onButtonClickDelete() {
        if (data.dbMakerController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        }
        runBlocking {
            launch {
                val database = SqliteDatabase().connect()
                val query = database.from(Makers).select()

                val result = query
                    .where { (Makers.id eq data.dbMakerController.selectId) }
                    .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                    .firstOrNull()

                if (result == null) {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись с выбранным id в базе отсуствует.")
                        .showWarning()
                } else {
                    database.delete(Makers) {
                        it.id eq result.id!!
                    }
                    data.dbMakerController.reloadTable()
                    data.dbMakerController.buttonTableMakerEdit.disableProperty().set(true)
                    data.dbMakerController.buttonTableMakerDelete.disableProperty().set(true)
                    data.dbMakerController.formStage.close()
                }
            }
        }
    }

    @FXML
    private fun onButtonClickCancel() {
        Data.companion.dbMakerController.formStage.close()
    }
}

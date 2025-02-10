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

class DBMakerFormEditController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var data: Data.companion = Data.companion

    @FXML
    lateinit var fieldName: TextField

    init {
        data.dbMakerController.formEditController = this
    }

    @FXML
    private fun onButtonClickEdit() {
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
                    if (result.name.equals(fieldName.text)) {
                        Notifications.create()
                            .title("Предупреждение!")
                            .text("Введённое наименование уже существует.")
                            .showWarning()
                    } else {
                        val result1 = query
                            .where { (Makers.name eq fieldName.text) }
                            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                            .firstOrNull()
                        if (result1 == null) {
                            database.update(Makers) {
                                set(it.name, fieldName.text)
                                where { it.id eq result.id!! }
                            }
                            data.dbMakerController.reloadTable()
                            data.dbMakerController.buttonTableMakerEdit.disableProperty().set(true)
                            data.dbMakerController.buttonTableMakerDelete.disableProperty().set(true)
                            data.dbMakerController.formStage.close()
                        } else {
                            Notifications.create()
                                .title("Предупреждение!")
                                .text("Введённое наименование уже существует.")
                                .showWarning()
                        }
                    }
                }
            }
        }
    }

    @FXML
    private fun onButtonClickCancel() {
        data.dbMakerController.formStage.close()
    }
}

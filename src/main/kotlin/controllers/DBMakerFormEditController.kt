package controllers

import Data
import SqliteDatabase
import db.Maker
import db.Makers
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.controlsfx.control.Notifications
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DBMakerFormEditController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private lateinit var fieldName: TextField

    @FXML
    private lateinit var buttonEdit: Button

    @FXML
    private fun onButtonClickEdit() {
        if (Data.companion.controller.dbMakerController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        }
        runBlocking {
            launch {
                val database = SqliteDatabase().connect()
                var query = database.from(Makers).select()

                val result = query
                    .where { (Makers.id eq Data.companion.controller.dbMakerController.selectId) }
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
                            .text("Введённое название уже существует.")
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
                        } else {
                            Notifications.create()
                                .title("Предупреждение!")
                                .text("Введённое навзание уже существует/")
                                .showWarning()
                        }
                    }
                }

                Data.companion.controller.dbMakerController.reloadTable()
                Data.companion.controller.dbMakerController.formStage.close()
            }
        }
    }

    @FXML
    private fun onButtonClickCancel() {
        Data.companion.controller.dbMakerController.formStage.close()
    }
}

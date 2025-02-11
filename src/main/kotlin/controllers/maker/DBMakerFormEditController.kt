package controllers.maker

import Config
import Data
import SqliteDatabase
import db.Maker
import db.Makers
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

class DBMakerFormEditController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName: TextField

    init {
        Data.dbMakerController.formEditController = this
    }

    @FXML
    private fun onButtonClickEdit() {
        if (Data.dbMakerController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        }
        runBlocking {
            launch {
                val database = SqliteDatabase.connect(Data.config.pathDB)
                val query = database.from(Makers).select()

                val result = query
                    .where { (Makers.id eq Data.dbMakerController.selectId) }
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
                            FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                            Data.dbMakerController.reloadTable()
                            Data.dbMakerController.buttonTableMakerEdit.disableProperty().set(true)
                            Data.dbMakerController.buttonTableMakerDelete.disableProperty().set(true)
                            Data.dbMakerController.formStage.close()
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
        Data.dbMakerController.formStage.close()
    }
}

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

class DBMakerFormDeleteController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldID: TextField

    @FXML
    lateinit var fieldName: TextField

    init {
        Data.dbMakerController.formDeleteController = this
    }

    @FXML
    private fun onButtonClickDelete() {
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
                    database.delete(Makers) {
                        it.id eq result.id!!
                    }
                    FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                    Data.dbMakerController.reloadTable()
                    Data.dbMakerController.buttonTableMakerEdit.disableProperty().set(true)
                    Data.dbMakerController.buttonTableMakerDelete.disableProperty().set(true)
                    Data.dbMakerController.formStage.close()
                }
            }
        }
    }

    @FXML
    private fun onButtonClickCancel() {
        Data.dbMakerController.formStage.close()
    }
}

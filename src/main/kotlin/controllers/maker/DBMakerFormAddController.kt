package controllers

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

class DBMakerFormAddController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private lateinit var fieldName: TextField

    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                val database = SqliteDatabase.connect(Data.config.pathDB)
                val query = database.from(Makers).select()

                val result = query
                    .where { (Makers.name eq fieldName.text) }
                    .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                    .firstOrNull()

                if (result == null) {
                    database.insert(Makers) {
                        set(it.name, fieldName.text)
                    }
                    FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                    Data.dbMakerController.reloadTable()
                    Data.dbMakerController.buttonTableMakerEdit.disableProperty().set(true)
                    Data.dbMakerController.buttonTableMakerDelete.disableProperty().set(true)
                    Data.dbMakerController.formStage.close()
                } else {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись с введённым наименованием уже существует.")
                        .showWarning()
                }
            }
        }
    }

    @FXML
    private fun onButtonClickCancel() {
        Data.dbMakerController.formStage.close()
    }
}
package controllers.maker

import Config
import Data
import db.Maker
import db.Makers
import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.update
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.SqliteDatabase
import java.io.File

class DBMakerFormEditController {
    @Suppress("unused") private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML lateinit var fieldName: TextField

    init {
        Data.dbMakerController.formEditController = this
    }

    @Suppress("unused") @FXML private fun onButtonClickEdit() {
        if (Data.dbMakerController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        }
        runBlocking {
            launch {
                Data.updateDB()
                val result = Data.dbMaker
                    .where { (Makers.id eq Data.dbMakerController.selectId) }
                    .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                    .firstOrNull()
                if (result == null) {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись с выбранным id в базе отсуствует.")
                        .showWarning()
                } else {
                    val result1 = Data.dbMaker
                        .where { (Makers.name eq fieldName.text) }
                        .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                        .firstOrNull()
                    if (result1 == null) {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        database.update(Makers) {
                            set(it.name, fieldName.text)
                            where { it.id eq result.id!! }
                        }
                        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                        Data.updateDB()
                        Data.dbMakerController.reloadTable()
                        Data.dbModelController.reloadTable()
                        Data.dbMakerController.buttonEdit.disableProperty().set(true)
                        Data.dbMakerController.buttonDelete.disableProperty().set(true)
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

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbMakerController.formStage.close()
    }
}

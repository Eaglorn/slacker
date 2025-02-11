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

class DBTypeOfHardwareFormEditController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName: TextField

    init {
        Data.dbTypeOfHardwareController.formEditController = this
    }

    @FXML
    private fun onButtonClickEdit() {
        if (Data.dbTypeOfHardwareController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        }
        runBlocking {
            launch {
                val database = SqliteDatabase.connect(Data.config.pathDB)
                val query = database.from(TypeOfHardwares).select()

                val result = query
                    .where { (TypeOfHardwares.id eq Data.dbTypeOfHardwareController.selectId) }
                    .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
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
                            .where { (TypeOfHardwares.name eq fieldName.text) }
                            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                            .firstOrNull()
                        if (result1 == null) {
                            database.update(TypeOfHardwares) {
                                set(it.name, fieldName.text)
                                where { it.id eq result.id!! }
                            }
                            FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                            Data.dbTypeOfHardwareController.reloadTable()
                            Data.dbTypeOfHardwareController.buttonTableTypeOfHardwareEdit.disableProperty().set(true)
                            Data.dbTypeOfHardwareController.buttonTableTypeOfHardwareDelete.disableProperty().set(true)
                            Data.dbTypeOfHardwareController.formStage.close()
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

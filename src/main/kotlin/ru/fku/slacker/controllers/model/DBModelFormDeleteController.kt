package ru.fku.slacker.controllers.model

import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.db.Model
import ru.fku.slacker.db.Models
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBModelFormDeleteController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName : TextField

    @FXML
    lateinit var fieldMaker : TextField

    @FXML
    lateinit var fieldTypeOfHardware : TextField

    init {
        Data.Companion.dbModelController.formDeleteController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDelete() {
        if (Data.Companion.dbModelController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        } else {
            runBlocking {
                launch {
                    Data.Companion.updateDB()
                    val result = Data.Companion.dbModel
                        .where { (Models.id eq Data.Companion.dbModelController.selectId) }
                        .map { row ->
                            Model(
                                row[Models.id],
                                row[Models.name],
                                row[Models.maker_id],
                                row[Models.type_of_hardware_id]
                            )
                        }
                        .firstOrNull()
                    if (result == null) {
                        Notifications.create()
                            .title("Предупреждение!")
                            .text("Запись с выбранным id в базе отсуствует.")
                            .showWarning()
                    } else {
                        val database = SqliteDatabase.connect(Data.Companion.config.pathDB)
                        database.delete(Models) {
                            it.id eq result.id !!
                        }
                        FileUtils.copyFile(File(Data.Companion.config.pathDB), File(Config.Companion.pathDBLocal))
                        Data.Companion.run {
                            updateDB()
                            dbModelController.run {
                                reloadTable()
                                formStage.close()
                            }
                        }
                    }
                }
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.Companion.dbModelController.formStage.close()
    }
}

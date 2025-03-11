package ru.fku.slacker.controllers.defect

import javafx.fxml.FXML
import javafx.scene.control.TextArea
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
import ru.fku.slacker.db.Defect
import ru.fku.slacker.db.Defects
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBDefectFormDeleteController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var boxHardware : TextField

    @FXML
    lateinit var areaResultView : TextArea

    @FXML
    lateinit var areaDetect : TextArea

    @FXML
    lateinit var areaReason : TextArea

    init {
        Data.Companion.dbDefectController.formDeleteController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDelete() {
        if (Data.Companion.dbDefectController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        } else {
            runBlocking {
                launch {
                    Data.Companion.updateDB()
                    val result = Data.Companion.dbDefect
                        .where { (Defects.id eq Data.Companion.dbDefectController.selectId) }
                        .map { row ->
                            Defect(
                                row[Defects.id],
                                row[Defects.hardware],
                                row[Defects.result_view],
                                row[Defects.detect],
                                row[Defects.reason]
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
                        database.delete(Defects) {
                            it.id eq result.id !!
                        }
                        FileUtils.copyFile(File(Data.Companion.config.pathDB), File(Config.Companion.pathDBLocal))
                        Data.Companion.run {
                            updateDB()
                            dbDefectController.run {
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
        Data.Companion.dbDefectController.formStage.close()
    }
}
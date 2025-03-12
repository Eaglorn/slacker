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
        Data.dbDefectController.formDeleteController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDelete() {
        if (Data.dbDefectController.selectId < 0) {
            Data.showMessage("Warning", "Отсутсвует выбор записи в таблице.")
        } else {
            runBlocking {
                launch {
                    Data.updateDB()
                    val result = Data.dbDefect
                        .where { (Defects.id eq Data.dbDefectController.selectId) }
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
                        Data.showMessage("Warning", "Запись с выбранным id в базе отсуствует.")
                    } else {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        database.delete(Defects) {
                            it.id eq result.id !!
                        }
                        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                        Data.run {
                            updateDB()
                            reloadTable("Defect")
                            dbDefectController.formStage.close()
                        }
                    }
                }
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbDefectController.formStage.close()
    }
}
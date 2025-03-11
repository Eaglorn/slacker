package ru.fku.slacker.controllers.defect

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.controlsfx.control.SearchableComboBox
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.db.Defect
import ru.fku.slacker.db.Defects
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBDefectFormAddController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var boxHardware : SearchableComboBox<String>

    @FXML
    private lateinit var areaResultView : TextArea

    @FXML
    private lateinit var areaDetect : TextArea

    @FXML
    private lateinit var areaReason : TextArea

    init {
        Data.Companion.dbDefectController.formAddController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                Data.Companion.updateDB()
                val result = Data.Companion.dbDefect
                    .where { ((Defects.result_view eq areaResultView.text) or (Defects.detect eq areaDetect.text) or (Defects.reason eq areaReason.text)) and (Defects.hardware eq boxHardware.selectionModel.selectedItem) }
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
                    val database = SqliteDatabase.connect(Data.Companion.config.pathDB)
                    database.insert(Defects) {
                        set(it.hardware, boxHardware.selectionModel.selectedItem)
                        set(it.result_view, areaResultView.text)
                        set(it.detect, areaDetect.text)
                        set(it.reason, areaReason.text)
                    }
                    FileUtils.copyFile(File(Data.Companion.config.pathDB), File(Config.Companion.pathDBLocal))
                    Data.Companion.run {
                        updateDB()
                        dbDefectController.run {
                            reloadTable()
                            formStage.close()
                        }
                    }
                } else {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("У записи присутсвует совпадение по заполненному полю.")
                        .showWarning()
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
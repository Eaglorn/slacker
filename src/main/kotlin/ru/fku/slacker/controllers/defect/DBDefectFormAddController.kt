package ru.fku.slacker.controllers.defect

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import org.apache.commons.io.FileUtils
import org.controlsfx.control.SearchableComboBox
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseFormController
import ru.fku.slacker.db.Defect
import ru.fku.slacker.db.Defects
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBDefectFormAddController : BaseFormController() {
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
        tableName = "Defect"
        Data.dbDefectController.formAddController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickAdd() {
        val hardware = boxHardware.selectionModel.selectedItem
        val resultView = areaResultView.text
        val detect = areaDetect.text
        val reason = areaReason.text
        if (resultView.isNotEmpty() && detect.isNotEmpty() && reason.isNotEmpty() && hardware.isNotEmpty()) {
            Data.updateDB()
            val result = Data.dbDefect
                .where { ((Defects.result_view eq hardware) and (Defects.detect eq resultView) and (Defects.reason eq detect)) and (Defects.hardware eq reason) }
                .map { Defect.getRows(it) }
                .firstOrNull()
            if (result == null) {
                val database = SqliteDatabase.connect(Data.config.pathDB)
                database.insert(Defects) {
                    set(it.hardware, hardware)
                    set(it.result_view, resultView)
                    set(it.detect, detect)
                    set(it.reason, reason)
                }
                FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                Data.run {
                    updateDB()
                    reloadTable(tableName)
                    dbDefectController.formStage.close()
                }
            } else {
                Data.showMessage("Warning", Data.textDict("DB.IsIndentFields", tableName))
            }
        } else {
            Data.showMessage("Warning", Data.textDict("DB.IsEmptyFields"))
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbDefectController.formStage.close()
    }
}
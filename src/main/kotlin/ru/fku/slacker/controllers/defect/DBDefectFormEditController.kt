package ru.fku.slacker.controllers.defect

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.controlsfx.control.SearchableComboBox
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.update
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseFormController
import ru.fku.slacker.db.Defect
import ru.fku.slacker.db.Defects
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBDefectFormEditController : BaseFormController() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var boxHardware : SearchableComboBox<String>

    @FXML
    lateinit var areaResultView : TextArea

    @FXML
    lateinit var areaDetect : TextArea

    @FXML
    lateinit var areaReason : TextArea

    init {
        tableName = "Defect"
        Data.dbDefectController.formEditController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickEdit() {
        val selectId = Data.dbDefectController.selectId
        if (selectId < 0) {
            Data.showMessage("Warning", Data.textDict("DB.IsSelectRecord"))
        } else {
            val hardware = boxHardware.selectionModel.selectedItem
            val resultView = areaResultView.text
            val detect = areaDetect.text
            val reason = areaReason.text
            if (resultView.isNotEmpty() && detect.isNotEmpty() && reason.isNotEmpty() && hardware.isNotEmpty()) {
                Data.updateDB()
                val result = Data.dbDefect
                    .where { (Defects.id eq selectId) }
                    .map { Defect.getRows(it) }
                    .firstOrNull()
                if (result == null) {
                    Data.showMessage("Warning", Data.textDict("DB.IsSelectId"))
                } else {
                    if (result.hardware.equals(hardware) && result.equals(resultView) && result.equals(detect) && result.equals(reason)
                    ) {
                        Data.showMessage("Warning", Data.textDict("DB.IsIndentFields"))
                    } else {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        database.update(Defects) {
                            set(it.hardware, hardware)
                            set(it.result_view, resultView)
                            set(it.detect, detect)
                            set(it.reason, reason)
                            where { it.id eq result.id !! }
                        }
                        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                        Data.run {
                            updateDB()
                            reloadTable("Defect")
                            dbDefectController.formStage.close()
                        }
                    }
                }
            } else {
                Data.showMessage("Warning", Data.textDict("DB.IsEmptyFields"))
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbDefectController.formStage.close()
    }
}
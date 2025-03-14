package ru.fku.slacker.controllers.defect

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import org.apache.commons.io.FileUtils
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
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

class DBDefectFormDeleteController : BaseFormController() {
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
        tableName = "Defect"
        Data.dbDefectController.formDeleteController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDelete() {
        val selectId = Data.dbDefectController.selectId
        if (selectId < 0) {
            Data.showMessage("Warning", Data.textDict("DB.IsSelectRecord"))
        } else {
            Data.updateDB()
            val result = Data.dbDefect
                .where { (Defects.id eq selectId) }
                .map { row -> Defect.getRows(row) }
                .firstOrNull()
            if (result == null) {
                Data.showMessage("Warning", Data.textDict("DB.IsSelectId"))
            } else {
                val database = SqliteDatabase.connect(Data.config.pathDB)
                database.delete(Defects) {
                    it.id eq result.id !!
                }
                FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                Data.run {
                    updateDB()
                    reloadTable(tableName)
                    dbDefectController.formStage.close()
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
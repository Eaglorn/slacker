package controllers.defect

import Config
import Data
import db.Defects
import javafx.fxml.FXML
import javafx.scene.control.TextArea
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.SearchableComboBox
import org.ktorm.dsl.insert
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.SqliteDatabase
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
        Data.dbDefectController.formAddController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                Data.updateDB()
                val database = SqliteDatabase.connect(Data.config.pathDB)
                database.insert(Defects) {
                    set(it.hardware, boxHardware.selectionModel.selectedItem)
                    set(it.result_view, areaResultView.text)
                    set(it.detect, areaDetect.text)
                    set(it.reason, areaReason.text)
                }
                FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                Data.run {
                    updateDB()
                    dbDefectController.run {
                        reloadTable()
                        formStage.close()
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
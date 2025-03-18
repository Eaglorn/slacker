package ru.fku.slacker.controllers.maker

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
import ru.fku.slacker.controllers.BaseFormController
import ru.fku.slacker.db.Maker
import ru.fku.slacker.db.Makers
import ru.fku.slacker.db.Models
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBMakerFormDeleteController : BaseFormController() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName : TextField

    init {
        tableName = "Maker"
        Data.dbMakerController.formDeleteController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDelete() {
        if (Data.dbMakerController.selectId < 0) {
            Data.showMessage("Warning", Data.textDict("DB.IsSelectRecord"))
        } else {
            Data.updateDB()
            val result = Data.dbMaker
                .where { (Makers.id eq Data.dbMakerController.selectId) }
                .map { Maker.getRows(it) }
                .firstOrNull()
            if (result == null) {
                Data.showMessage("Warning", Data.textDict("DB.IsSelectId"))
            } else {
                val database = SqliteDatabase.connect(Data.config.pathDB)
                database.delete(Makers) {
                    it.id eq result.id !!
                }
                database.delete(Models) {
                    it.maker_id eq result.id !!
                }
                FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                Data.run {
                    updateDB()
                    reloadTable("Maker", "Model")
                    dbMakerController.formStage.close()
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

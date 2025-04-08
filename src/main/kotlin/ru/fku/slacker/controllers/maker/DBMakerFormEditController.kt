package ru.fku.slacker.controllers.maker

import javafx.fxml.FXML
import javafx.scene.control.TextField
import org.apache.commons.io.FileUtils
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.update
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseFormController
import ru.fku.slacker.db.Maker
import ru.fku.slacker.db.Makers
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBMakerFormEditController : BaseFormController() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName : TextField

    init {
        tableName = "Maker"
        Data.dbMakerController.formEditController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickEdit() {
        if (Data.dbMakerController.selectId < 0) {
            Data.showMessage("Warning", Data.textDict("DB.IsSelectRecord"))
        } else {
            val name = fieldName.text
            if (name.isNotEmpty()) {
                Data.updateDB()
                val result = Data.dbMaker
                    .where { (Makers.id eq Data.dbMakerController.selectId) }
                    .map { Maker.getRows(it) }
                    .firstOrNull()
                if (result == null) {
                    Data.showMessage("Warning", Data.textDict("DB.IsSelectId"))
                } else {
                    val result1 = Data.dbMaker
                        .where { Makers.name eq name }
                        .map { Maker.getRows(it) }
                        .firstOrNull()
                    if (result1 == null) {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        database.update(Makers) {
                            set(it.name, name)
                            where { it.id eq result.id !! }
                        }
                        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                        Data.run {
                            updateDB()
                            reloadTable(tableName, "Model")
                            dbMakerController.formStage.close()
                        }
                    } else {
                        Data.showMessage("Warning", Data.textDict("DB.IsIndentFields"))
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
        Data.dbMakerController.formStage.close()
    }
}

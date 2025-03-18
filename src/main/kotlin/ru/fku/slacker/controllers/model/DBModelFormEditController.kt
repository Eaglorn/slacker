package ru.fku.slacker.controllers.model

import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
import ru.fku.slacker.db.*
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBModelFormEditController : BaseFormController() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName : TextField

    @FXML
    lateinit var boxMaker : SearchableComboBox<String>

    @FXML
    lateinit var boxTypeOfHardware : SearchableComboBox<String>

    init {
        tableName = "Model"
        Data.dbModelController.formEditController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickEdit() {
        if (Data.dbModelController.selectId < 0) {
            Data.showMessage("Warning", Data.textDict("DB.IsSelectRecord"))
        } else {
            val name = fieldName.text
            val maker = boxMaker.selectionModel.selectedItem
            val typeOfHardware = boxTypeOfHardware.selectionModel.selectedItem
            if(name.isNotEmpty() && maker.isNotEmpty() && typeOfHardware.isNotEmpty()) {
                Data.updateDB()
                val result = Data.dbModel
                    .where { (Models.id eq Data.dbModelController.selectId) }
                    .map { Model.getRows(it) }
                    .firstOrNull()
                if (result == null) {
                    Data.showMessage("Warning", Data.textDict("DB.IsSelectRecord"))
                } else {
                    var resultMaker = Data.dbMaker
                        .where { Makers.name eq maker }
                        .map { Maker.getRows(it) }
                        .firstOrNull()
                    var resultTypeOfHardware = Data.dbTypeOfHardware
                        .where { TypeOfHardwares.name eq typeOfHardware }
                        .map { TypeOfHardware.getRows(it) }
                        .firstOrNull()
                    if (result.name.equals(name) && result.maker_id == resultMaker?.id && result.type_of_hardware_id == resultTypeOfHardware?.id) {
                        Data.showMessage("Warning", Data.textDict("DB.IsIndentFields"))
                    } else {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        resultMaker = Data.dbMaker
                            .where { Makers.name eq maker }
                            .map { Maker.getRows(it) }
                            .firstOrNull()
                        resultTypeOfHardware = Data.dbTypeOfHardware
                            .where { TypeOfHardwares.name eq typeOfHardware }
                            .map { TypeOfHardware.getRows(it) }
                            .firstOrNull()
                        if (maker != null && typeOfHardware != null) {
                            database.update(Models) {
                                set(it.name, name)
                                set(it.maker_id, resultMaker?.id)
                                set(it.type_of_hardware_id, resultTypeOfHardware?.id)
                                where { it.id eq result.id !! }
                            }
                        }
                        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                        Data.run {
                            updateDB()
                            reloadTable(tableName)
                            dbModelController.formStage.close()
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
        Data.dbModelController.formStage.close()
    }
}

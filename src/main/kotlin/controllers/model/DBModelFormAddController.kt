package controllers.model

import Config
import Data
import db.*
import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.controlsfx.control.SearchableComboBox
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.SqliteDatabase
import java.io.File

class DBModelFormAddController {
    @Suppress("unused") private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML private lateinit var fieldName: TextField

    @FXML lateinit var boxMaker: SearchableComboBox<String>

    @FXML lateinit var boxTypeOfHardware: SearchableComboBox<String>

    init {
        Data.dbModelController.formAddController = this
    }

    @Suppress("unused") @FXML private fun onButtonClickAdd() {
        runBlocking {
            launch {
                Data.updateDB()

                val result = Data.dbModel
                    .where { (Models.name eq fieldName.text) }
                    .map { row ->
                        Model(
                            row[Models.id],
                            row[Models.name],
                            row[Models.maker_id],
                            row[Models.type_of_hardware_id]
                        )
                    }
                    .firstOrNull()

                if (result == null) {
                    val database = SqliteDatabase.connect(Data.config.pathDB)
                    val maker = Data.dbMaker
                        .where { (Makers.name eq boxMaker.selectionModel.selectedItem) }
                        .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                        .firstOrNull()
                    val typeOfHardware = Data.dbTypeOfHardware
                        .where { (TypeOfHardwares.name eq boxTypeOfHardware.selectionModel.selectedItem) }
                        .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                        .firstOrNull()
                    if (maker != null && typeOfHardware != null) {
                        database.insert(Models) {
                            set(it.name, fieldName.text)
                            set(it.maker_id, maker.id)
                            set(it.type_of_hardware_id, typeOfHardware.id)
                        }
                    }
                    FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                    Data.updateDB()
                    Data.dbModelController.reloadTable()
                    Data.dbModelController.buttonEdit.disableProperty().set(true)
                    Data.dbModelController.buttonDelete.disableProperty().set(true)
                    Data.dbModelController.formStage.close()
                } else {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись с введённым наименованием уже существует.")
                        .showWarning()
                }
            }
        }
    }

    @Suppress("unused") @FXML private fun onButtonClickCancel() {
        Data.dbModelController.formStage.close()
    }
}
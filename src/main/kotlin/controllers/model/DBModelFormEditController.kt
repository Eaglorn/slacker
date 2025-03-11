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
import org.ktorm.dsl.map
import org.ktorm.dsl.update
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.SqliteDatabase
import java.io.File

class DBModelFormEditController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName : TextField

    @FXML
    lateinit var boxMaker : SearchableComboBox<String>

    @FXML
    lateinit var boxTypeOfHardware : SearchableComboBox<String>

    init {
        Data.dbModelController.formEditController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickEdit() {
        if (Data.dbModelController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        } else {
            runBlocking {
                launch {
                    Data.updateDB()
                    val result = Data.dbModel
                        .where { (Models.id eq Data.dbModelController.selectId) }
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
                        Notifications.create()
                            .title("Предупреждение!")
                            .text("Запись с выбранным id в базе отсуствует.")
                            .showWarning()
                    } else {
                        var maker = Data.dbMaker
                            .where { (Makers.name eq boxMaker.selectionModel.selectedItem) }
                            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                            .firstOrNull()
                        var typeOfHardware = Data.dbTypeOfHardware
                            .where { (TypeOfHardwares.name eq boxTypeOfHardware.selectionModel.selectedItem) }
                            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                            .firstOrNull()
                        if (result.name.equals(fieldName.text) && result.maker_id == maker?.id && result.type_of_hardware_id == typeOfHardware?.id) {
                            Notifications.create()
                                .title("Предупреждение!")
                                .text("Запись модель с введёнными значениями уже существует.")
                                .showWarning()
                        } else {
                            val database = SqliteDatabase.connect(Data.config.pathDB)
                            maker = Data.dbMaker
                                .where { (Makers.name eq boxMaker.selectionModel.selectedItem) }
                                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                                .firstOrNull()
                            typeOfHardware = Data.dbTypeOfHardware
                                .where { (TypeOfHardwares.name eq boxTypeOfHardware.selectionModel.selectedItem) }
                                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                                .firstOrNull()
                            if (maker != null && typeOfHardware != null) {
                                database.update(Models) {
                                    set(it.name, fieldName.text)
                                    set(it.maker_id, maker.id)
                                    set(it.type_of_hardware_id, typeOfHardware.id)
                                    where { it.id eq result.id !! }
                                }
                            }
                            FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                            Data.run {
                                updateDB()
                                dbModelController.run {
                                    reloadTable()
                                    formStage.close()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbModelController.formStage.close()
    }
}

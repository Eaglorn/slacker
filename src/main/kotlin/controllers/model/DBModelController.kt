package controllers.model

import Data
import db.*
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Modality
import javafx.stage.Stage
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DBModelController(
    val table: TableView2<ModelTable>,
    val buttonEdit: Button,
    val buttonDelete: Button
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    lateinit var formStage: Stage

    var selectId: Int = -1

    lateinit var formAddController: DBModelFormAddController

    init {
        buttonEdit.disableProperty().set(true)
        buttonDelete.disableProperty().set(true)
        table.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue.let {
                if (it != null) {
                    selectId = it.getId()
                    if (buttonEdit.disableProperty().get()) {
                        buttonEdit.disableProperty().set(false)
                    }
                    if (buttonDelete.disableProperty().get()) {
                        buttonDelete.disableProperty().set(false)
                    }
                } else {
                    selectId = -1
                    if (!buttonEdit.disableProperty().get()) {
                        buttonEdit.disableProperty().set(true)
                    }
                    if (!buttonDelete.disableProperty().get()) {
                        buttonDelete.disableProperty().set(true)
                    }
                }
            }
        }
    }

    fun reloadTable() {
        table.items.clear()
        Data.dbModel
            .map { row ->
                Model(
                    row[Models.id],
                    row[Models.name],
                    row[Models.maker_id],
                    row[Models.type_of_hardware_id]
                )
            }
            .forEach {
                val maker = Data.dbMaker
                    .where { (Makers.id eq it.maker_id!!) }
                    .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                    .firstOrNull()
                val typeOfHardware = Data.dbTypeOfHardware
                    .where { (TypeOfHardwares.id eq it.type_of_hardware_id!!) }
                    .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                    .firstOrNull()
                table.items.add(ModelTable(it.id, it.name, maker?.name, typeOfHardware?.name))
            }
    }

    fun onButtonClickAdd() {
        val fxmlLoader = FXMLLoader(DBModelFormAddController::class.java.getResource("/DBModelFormAdd.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Создание записи модель"
        formStage.scene = formScene

        Data.updateDB()

        Data.dbMaker
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .forEach {
                Data.dbModelController.formAddController.boxMaker.items.add(it.name)
            }

        Data.dbTypeOfHardware
            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
            .forEach {
                Data.dbModelController.formAddController.boxTypeOfHardware.items.add(it.name)
            }

        formStage.showAndWait()
    }

    fun onButtonClickEdit() {}

    fun onButtonClickDelete() {
    }
}
package controllers.typeofhardware

import Data
import db.TypeOfHardware
import db.TypeOfHardwareTable
import db.TypeOfHardwares
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

class DBTypeOfHardwareController(
    private val table: TableView2<TypeOfHardwareTable>,
    val buttonEdit: Button,
    val buttonDelete: Button
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    lateinit var formStage: Stage

    var selectId: Int = -1

    lateinit var formEditController: DBTypeOfHardwareFormEditController
    lateinit var formDeleteController: DBTypeOfHardwareFormDeleteController

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
        Data.dbTypeOfHardware
            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
            .forEach {
                table.items.add(TypeOfHardwareTable(it.id, it.name))
            }
    }

    fun onButtonClickAdd() {
        val fxmlLoader =
            FXMLLoader(DBTypeOfHardwareFormAddController::class.java.getResource("/DBTypeOfHardwareFormAdd.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Создание записи тип оборудования"
        formStage.scene = formScene
        formStage.showAndWait()
    }

    fun onButtonClickEdit() {
        val fxmlLoader =
            FXMLLoader(DBTypeOfHardwareController::class.java.getResource("/DBTypeOfHardwareFormEdit.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Редактирование записи тип оборудования"
        formStage.scene = formScene
        val result = Data.dbTypeOfHardware
            .where { (TypeOfHardwares.id eq selectId) }
            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
            .firstOrNull()
        if (result != null) {
            formEditController.fieldName.text = result.name
        }
        formStage.showAndWait()
    }

    fun onButtonClickDelete() {
        val fxmlLoader =
            FXMLLoader(DBTypeOfHardwareController::class.java.getResource("/DBTypeOfHardwareFormDelete.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Удаление записи тип оборудования"
        formStage.scene = formScene
        val result = Data.dbTypeOfHardware
            .where { (TypeOfHardwares.id eq selectId) }
            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
            .firstOrNull()
        if (result != null) {
            formDeleteController.fieldName.text = result.name
        }
        formStage.showAndWait()
    }
}
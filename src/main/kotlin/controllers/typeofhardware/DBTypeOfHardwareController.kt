package controllers.typeofhardware

import Config
import SqliteDatabase
import db.TypeOfHardware
import db.TypeOfHardwareTable
import db.TypeOfHardwares
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Modality
import javafx.stage.Stage
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DBTypeOfHardwareController(
    val tableTypeOfHardware: TableView2<TypeOfHardwareTable>,
    val buttonTableTypeOfHardwareEdit: Button,
    val buttonTableTypeOfHardwareDelete: Button
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    lateinit var formStage: Stage

    var selectId: Int = -1

    lateinit var formEditController: DBTypeOfHardwareFormEditController
    lateinit var formDeleteController: DBTypeOfHardwareFormDeleteController

    init {
        buttonTableTypeOfHardwareEdit.disableProperty().set(true)
        buttonTableTypeOfHardwareDelete.disableProperty().set(true)
        tableTypeOfHardware.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue.let {
                if (it != null) {
                    selectId = it.getId()
                    if (buttonTableTypeOfHardwareEdit.disableProperty().get()) {
                        buttonTableTypeOfHardwareEdit.disableProperty().set(false)
                    }
                    if (buttonTableTypeOfHardwareDelete.disableProperty().get()) {
                        buttonTableTypeOfHardwareDelete.disableProperty().set(false)
                    }
                } else {
                    selectId = -1
                    if (!buttonTableTypeOfHardwareEdit.disableProperty().get()) {
                        buttonTableTypeOfHardwareEdit.disableProperty().set(true)
                    }
                    if (!buttonTableTypeOfHardwareDelete.disableProperty().get()) {
                        buttonTableTypeOfHardwareDelete.disableProperty().set(true)
                    }
                }
            }
        }
    }

    fun reloadTable() {
        val database = SqliteDatabase.connect(Config.pathDBLocal)

        val query = database.from(TypeOfHardwares).select()

        tableTypeOfHardware.items.clear()

        query
            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
            .forEach {
                tableTypeOfHardware.items.add(TypeOfHardwareTable(it.id, it.name))
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
        val database = SqliteDatabase.connect(Config.pathDBLocal)
        val query = database.from(TypeOfHardwares).select()
        val result = query
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
        val database = SqliteDatabase.connect(Config.pathDBLocal)
        val query = database.from(TypeOfHardwares).select()
        val result = query
            .where { (TypeOfHardwares.id eq selectId) }
            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
            .firstOrNull()
        if (result != null) {
            formDeleteController.fieldID.text = result.id.toString()
            formDeleteController.fieldName.text = result.name
        }
        formStage.showAndWait()
    }
}
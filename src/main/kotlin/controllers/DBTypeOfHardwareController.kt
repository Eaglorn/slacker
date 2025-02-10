package controllers

import Data
import SqliteDatabase
import db.Maker
import db.Makers
import db.TypeOfHardwareTable
import db.TypeOfHardwares
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Modality
import javafx.stage.Stage
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DBTypeOfHardwareController(
    val tableTypeOfHardware: TableView2<TypeOfHardwareTable>,
    val buttonTableTypeOfHardwareEdit: Button,
    val buttonTableTypeOfHardwareDelete: Button
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var data: Data.companion = Data.companion

    lateinit var formStage: Stage

    var selectId: Int = -1

    lateinit var formEditController: DBMakerFormEditController
    //lateinit var formDeleteController: DBMakerFormDeleteController


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
        val database = SqliteDatabase().connect()

        val query = database.from(TypeOfHardwares).select()

        data.controller.tableTypeOfHardware.items.clear()

        query
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .forEach {
                data.controller.tableTypeOfHardware.items.add(TypeOfHardwareTable(it.id, it.name))
            }
    }

    fun onButtonClickAdd() {
        val fxmlLoader =
            FXMLLoader(DBTypeOfHardwareFormAddController::class.java.getResource("/DBTypeOfHardwareFormAdd.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Создание типа оборудование"
        formStage.scene = formScene
        formStage.showAndWait()
    }

    fun onButtonClickEdit() {

    }

    fun onButtonClickDelete() {

    }
}
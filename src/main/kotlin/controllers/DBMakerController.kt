package controllers

import Data
import SqliteDatabase
import db.Maker
import db.MakerTable
import db.Makers
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Modality
import javafx.stage.Stage
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DBMakerController(
    val tableMaker: TableView2<MakerTable>,
    val buttonTableMakerEdit: Button,
    val buttonTableMakerDelete: Button
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var data: Data.companion = Data.companion

    lateinit var formStage: Stage

    var selectId: Int = -1

    lateinit var formEditController: DBMakerFormEditController
    lateinit var formDeleteController: DBMakerFormDeleteController

    init {
        buttonTableMakerEdit.disableProperty().set(true)
        buttonTableMakerDelete.disableProperty().set(true)
        tableMaker.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue.let {
                if (it != null) {
                    selectId = it.getId()
                    if (buttonTableMakerEdit.disableProperty().get()) {
                        buttonTableMakerEdit.disableProperty().set(false)
                    }
                    if (buttonTableMakerDelete.disableProperty().get()) {
                        buttonTableMakerDelete.disableProperty().set(false)
                    }
                } else {
                    selectId = -1
                    if (!buttonTableMakerEdit.disableProperty().get()) {
                        buttonTableMakerEdit.disableProperty().set(true)
                    }
                    if (!buttonTableMakerDelete.disableProperty().get()) {
                        buttonTableMakerDelete.disableProperty().set(true)
                    }
                }
            }
        }
    }

    fun reloadTable() {
        val database = SqliteDatabase().connect()

        val query = database.from(Makers).select()

        tableMaker.items.clear()

        query
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .forEach {
                tableMaker.items.add(MakerTable(it.id, it.name))
            }
    }

    fun onButtonClickAdd() {
        val fxmlLoader = FXMLLoader(DBMakerFormAddController::class.java.getResource("/DBMakerFormAdd.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Создание записи производитель"
        formStage.scene = formScene
        formStage.showAndWait()
    }

    fun onButtonClickEdit() {
        val fxmlLoader = FXMLLoader(DBMakerFormEditController::class.java.getResource("/DBMakerFormEdit.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Редактирование записи производитель"
        formStage.scene = formScene
        val database = SqliteDatabase().connect()
        val query = database.from(Makers).select()
        val result = query
            .where { (Makers.id eq selectId) }
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .firstOrNull()
        if (result != null) {
            formEditController.fieldName.text = result.name
        }
        formStage.showAndWait()
    }

    fun onButtonClickDelete() {
        val fxmlLoader = FXMLLoader(DBMakerFormDeleteController::class.java.getResource("/DBMakerFormDelete.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Удаление записи производитель"
        formStage.scene = formScene
        val database = SqliteDatabase().connect()
        val query = database.from(Makers).select()
        val result = query
            .where { (Makers.id eq selectId) }
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .firstOrNull()
        if (result != null) {
            formDeleteController.fieldID.text = result.id.toString()
            formDeleteController.fieldName.text = result.name
        }
        formStage.showAndWait()
    }
}
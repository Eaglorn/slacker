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
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DBMakerController(
    val tableMaker: TableView2<MakerTable>,
    val buttonTableMakerEdit: Button,
    val buttonTableMakerDelete: Button
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    lateinit var formStage: Stage

    var selectId: Int = -1

    init {
        tableMaker.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue.let {
                if (it != null) {
                    selectId = it.getId()
                } else {
                    selectId = -1
                }
            }
        }
    }

    fun reloadTable() {
        val database = SqliteDatabase().connect()

        var query = database.from(Makers).select()

        Data.companion.controller.tableMaker.items.clear()

        query
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .forEach {
                Data.companion.controller.tableMaker.items.add(MakerTable(it.id, it.name))
            }
    }

    fun onButtonClickAdd() {
        val fxmlLoader = FXMLLoader(DBMakerFormAddController::class.java.getResource("/DBMakerFormAdd.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Создание производителя"
        formStage.scene = formScene
        formStage.showAndWait()
    }

    fun onButtonClickEdit() {
        val fxmlLoader = FXMLLoader(DBMakerFormEditController::class.java.getResource("/DBMakerFormEdit.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Изменение производителя"
        formStage.scene = formScene
        formStage.showAndWait()
    }


    fun onButtonClickDelete() {

    }
}
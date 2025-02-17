package controllers.maker

import Config
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
    private val table: TableView2<MakerTable>,
    val buttonEdit: Button,
    val buttonDelete: Button
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    lateinit var formStage: Stage

    var selectId: Int = -1

    lateinit var formEditController: DBMakerFormEditController
    lateinit var formDeleteController: DBMakerFormDeleteController

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
        val database = SqliteDatabase.connect(Config.pathDBLocal)

        val query = database.from(Makers).select()

        table.items.clear()

        query
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .forEach {
                table.items.add(MakerTable(it.id, it.name))
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
        val database = SqliteDatabase.connect(Config.pathDBLocal)
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
        val database = SqliteDatabase.connect(Config.pathDBLocal)
        val query = database.from(Makers).select()
        val result = query
            .where { (Makers.id eq selectId) }
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .firstOrNull()
        if (result != null) {
            formDeleteController.fieldName.text = result.name
        }
        formStage.showAndWait()
    }
}
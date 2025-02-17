package controllers.model

import Data
import controllers.maker.DBMakerFormEditController
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
    lateinit var formEditController: DBModelFormEditController
    lateinit var formDeleteController: DBModelFormDeleteController

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
        val fxmlLoader = FXMLLoader(DBModelFormAddController::class.java.getResource("/db/model/Add.fxml"))
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

    fun onButtonClickEdit() {
        val fxmlLoader = FXMLLoader(DBMakerFormEditController::class.java.getResource("/db/model/Edit.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Редактирование записи модель"
        formStage.scene = formScene

        Data.updateDB()

        val result = Data.dbModel
            .where { (Models.id eq selectId) }
            .map { row ->
                Model(
                    row[Models.id],
                    row[Models.name],
                    row[Models.maker_id],
                    row[Models.type_of_hardware_id]
                )
            }
            .firstOrNull()
        if (result != null) {
            formEditController.fieldName.text = result.name
            Data.dbMaker
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .forEach { formEditController.boxMaker.items.add(it.name) }
            Data.dbTypeOfHardware
                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                .forEach { formEditController.boxTypeOfHardware.items.add(it.name) }
            val maker = Data.dbMaker
                .where { (Makers.id eq result.maker_id!!) }
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .firstOrNull()
            val typeOfHardware = Data.dbTypeOfHardware
                .where { (TypeOfHardwares.id eq result.type_of_hardware_id!!) }
                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                .firstOrNull()
            if (maker != null && typeOfHardware != null) {
                formEditController.boxMaker.selectionModel.select(maker.name)
                formEditController.boxTypeOfHardware.selectionModel.select(typeOfHardware.name)
            }
        }
        formStage.showAndWait()
    }

    fun onButtonClickDelete() {
        val fxmlLoader =
            FXMLLoader(DBModelFormDeleteController::class.java.getResource("/db/model/Delete.fxml"))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = "Удаление записи модель"
        formStage.scene = formScene

        Data.updateDB()

        val result = Data.dbModel
            .where { (Models.id eq selectId) }
            .map { row ->
                Model(
                    row[Models.id],
                    row[Models.name],
                    row[Models.maker_id],
                    row[Models.type_of_hardware_id]
                )
            }
            .firstOrNull()
        if (result != null) {
            formDeleteController.fieldName.text = result.name
            Data.dbMaker
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .forEach { formDeleteController.boxMaker.items.add(it.name) }
            Data.dbTypeOfHardware
                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                .forEach { formDeleteController.boxTypeOfHardware.items.add(it.name) }
            val maker = Data.dbMaker
                .where { (Makers.id eq result.maker_id!!) }
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .firstOrNull()
            val typeOfHardware = Data.dbTypeOfHardware
                .where { (TypeOfHardwares.id eq result.type_of_hardware_id!!) }
                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                .firstOrNull()
            if (maker != null && typeOfHardware != null) {
                formDeleteController.boxMaker.selectionModel.select(maker.name)
                formDeleteController.boxTypeOfHardware.selectionModel.select(typeOfHardware.name)
            }
        }
        formStage.showAndWait()
    }
}
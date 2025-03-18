package ru.fku.slacker.controllers.model

import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseController
import ru.fku.slacker.db.*

class DBModelController(table : TableView2<ModelTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<ModelTable>(table, buttonEdit, buttonDelete) {

    lateinit var formAddController : DBModelFormAddController
    lateinit var formEditController : DBModelFormEditController
    lateinit var formDeleteController : DBModelFormDeleteController

    init {
        tableName = "Model"
        createMethods(tableName)
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbModel
            .map { Model.getRows(it) }
            .forEach {
                val maker = Data.dbMaker
                    .where { (Makers.id eq it.maker_id !!) }
                    .map { Maker.getRows(it) }
                    .firstOrNull()
                val typeOfHardware = Data.dbTypeOfHardware
                    .where { (TypeOfHardwares.id eq it.type_of_hardware_id !!) }
                    .map { TypeOfHardware.getRows(it) }
                    .firstOrNull()
                table.items.add(ModelTable(it.id, it.name, maker?.name, typeOfHardware?.name))
            }
    }

    override fun onButtonClickAdd() {
        showModal("/db/model/Add.fxml", "Создание записи модель") {
            Data.updateDB()
            Data.dbMaker
                .map { Maker.getRows(it) }
                .forEach { Data.dbModelController.formAddController.boxMaker.items.add(it.name) }
            Data.dbTypeOfHardware
                .map { TypeOfHardware.getRows(it) }
                .forEach { Data.dbModelController.formAddController.boxTypeOfHardware.items.add(it.name) }
        }
    }

    override fun onButtonClickEdit() {
        showModal("/db/model/Edit.fxml", "Редактирование записи модель") {
            Data.updateDB()
            val result = Data.dbModel
                .where { (Models.id eq selectId) }
                .map { Model.getRows(it) }
                .firstOrNull()
            if (result != null) {
                formEditController.fieldName.text = result.name
                Data.dbMaker
                    .map { Maker.getRows(it) }
                    .forEach { formEditController.boxMaker.items.add(it.name) }
                Data.dbTypeOfHardware
                    .map { TypeOfHardware.getRows(it) }
                    .forEach { formEditController.boxTypeOfHardware.items.add(it.name) }
                val maker = Data.dbMaker
                    .where { (Makers.id eq result.maker_id !!) }
                    .map { Maker.getRows(it) }
                    .firstOrNull()
                val typeOfHardware = Data.dbTypeOfHardware
                    .where { (TypeOfHardwares.id eq result.type_of_hardware_id !!) }
                    .map { TypeOfHardware.getRows(it) }
                    .firstOrNull()
                if (maker != null && typeOfHardware != null) {
                    formEditController.run {
                        boxMaker.selectionModel.select(maker.name)
                        boxTypeOfHardware.selectionModel.select(typeOfHardware.name)
                    }
                }
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/db/model/Delete.fxml", "Удаление записи модель") {
            Data.updateDB()
            val result = Data.dbModel
                .where { (Models.id eq selectId) }
                .map { Model.getRows(it) }
                .firstOrNull()
            if (result != null) {
                formDeleteController.fieldName.text = result.name
                val maker = Data.dbMaker
                    .where { (Makers.id eq result.maker_id !!) }
                    .map { Maker.getRows(it) }
                    .firstOrNull()
                val typeOfHardware = Data.dbTypeOfHardware
                    .where { (TypeOfHardwares.id eq result.type_of_hardware_id !!) }
                    .map { TypeOfHardware.getRows(it) }
                    .firstOrNull()
                if (maker != null && typeOfHardware != null) {
                    formDeleteController.run {
                        fieldMaker.text = maker.name
                        fieldTypeOfHardware.text = typeOfHardware.name
                    }
                }
            }
        }
    }
}
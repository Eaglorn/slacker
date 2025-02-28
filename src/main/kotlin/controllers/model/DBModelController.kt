package controllers.model

import Data
import db.*
import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import utils.BaseController

class DBModelController(table: TableView2<ModelTable>, buttonEdit: Button, buttonDelete: Button) :
    BaseController<ModelTable>(table, buttonEdit, buttonDelete) {
    lateinit var formAddController: DBModelFormAddController
    lateinit var formEditController: DBModelFormEditController
    lateinit var formDeleteController: DBModelFormDeleteController

    init {
        setupTableListener()
    }

    override fun reloadTable() {
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

    override fun onButtonClickAdd() {
        showModal("/db/model/Add.fxml", "Создание записи модель") {
            Data.updateDB()
            Data.dbMaker
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .forEach { Data.dbModelController.formAddController.boxMaker.items.add(it.name) }
            Data.dbTypeOfHardware
                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                .forEach { Data.dbModelController.formAddController.boxTypeOfHardware.items.add(it.name) }
        }
    }

    override fun onButtonClickEdit() {
        showModal("/db/model/Edit.fxml", "Редактирование записи модель") {
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
        }
    }

    override fun onButtonClickDelete() {
        showModal("/db/model/Delete.fxml", "Удаление записи модель") {
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
        }
    }
}
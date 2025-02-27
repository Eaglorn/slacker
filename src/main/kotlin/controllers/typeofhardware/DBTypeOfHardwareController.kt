package controllers.typeofhardware

import Data
import controllers.BaseController
import db.TypeOfHardware
import db.TypeOfHardwareTable
import db.TypeOfHardwares
import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where

class DBTypeOfHardwareController(
    table: TableView2<TypeOfHardwareTable>,
    buttonEdit: Button,
    buttonDelete: Button
) : BaseController<TypeOfHardwareTable>(table, buttonEdit, buttonDelete) {
    lateinit var formEditController: DBTypeOfHardwareFormEditController
    lateinit var formDeleteController: DBTypeOfHardwareFormDeleteController

    init {
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbTypeOfHardware
            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
            .forEach {
                table.items.add(TypeOfHardwareTable(it.id, it.name))
            }
    }

    override fun onButtonClickAdd() {
        showModal("/db/typeofhardware/Add.fxml", "Создание записи тип оборудования") {}
    }

    override fun onButtonClickEdit() {
        showModal("/db/typeofhardware/Edit.fxml", "Редактирование записи тип оборудования") { controller ->
            val result = Data.dbTypeOfHardware
                .where { (TypeOfHardwares.id eq selectId) }
                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                .firstOrNull()
            if (result != null) {
                formEditController.fieldName.text = result.name
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/db/typeofhardware/Delete.fxml", "Удаление записи тип оборудования") { controller ->
            val result = Data.dbTypeOfHardware
                .where { (TypeOfHardwares.id eq selectId) }
                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                .firstOrNull()
            if (result != null) {
                formDeleteController.fieldName.text = result.name
            }
        }
    }
}
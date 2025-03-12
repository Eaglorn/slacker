package ru.fku.slacker.controllers.typeofhardware

import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseController
import ru.fku.slacker.db.TypeOfHardware
import ru.fku.slacker.db.TypeOfHardwareTable
import ru.fku.slacker.db.TypeOfHardwares

class DBTypeOfHardwareController(table : TableView2<TypeOfHardwareTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<TypeOfHardwareTable>(table, buttonEdit, buttonDelete) {

    lateinit var formEditController : DBTypeOfHardwareFormEditController
    lateinit var formDeleteController : DBTypeOfHardwareFormDeleteController

    init {
        setupTableListener()
        val name = "TypeOfHardware"
        Data.hMap["Table.Reload.${name}"] = { _ -> this.reloadTable() }
        Data.hMap["Table.Add.${name}"] = { _ -> this.onButtonClickAdd() }
        Data.hMap["Table.Edit.${name}"] = { _ -> this.onButtonClickEdit() }
        Data.hMap["Table.Delete.${name}"] = { _ -> this.onButtonClickDelete() }
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbTypeOfHardware
            .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
            .forEach { table.items.add(TypeOfHardwareTable(it.id, it.name)) }
    }

    override fun onButtonClickAdd() {
        showModal("/db/typeofhardware/Add.fxml", "Создание записи тип оборудования") {}
    }

    override fun onButtonClickEdit() {
        showModal("/db/typeofhardware/Edit.fxml", "Редактирование записи тип оборудования") { controller ->
            Data.updateDB()
            val result = Data.dbTypeOfHardware
                .where { (TypeOfHardwares.id eq selectId) }
                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                .firstOrNull()
            result?.let {
                formEditController.fieldName.text = it.name
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/db/typeofhardware/Delete.fxml", "Удаление записи тип оборудования") { controller ->
            Data.updateDB()
            val result = Data.dbTypeOfHardware
                .where { (TypeOfHardwares.id eq selectId) }
                .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                .firstOrNull()
            result?.let {
                formDeleteController.fieldName.text = it.name
            }
        }
    }
}
package ru.fku.slacker.controllers.manager

import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseController
import ru.fku.slacker.db.Manager
import ru.fku.slacker.db.ManagerTable
import ru.fku.slacker.db.Managers

class DBManagerController(table : TableView2<ManagerTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<ManagerTable>(table, buttonEdit, buttonDelete) {

    lateinit var formEditController : DBManagerFormEditController
    lateinit var formDeleteController : DBManagerFormDeleteController

    init {
        createMethods("Manager")
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbManager
            .map { Manager.getRows(it) }
            .forEach { table.items.add(ManagerTable(it.id, it.name, it.post)) }
    }

    override fun onButtonClickAdd() {
        showModal("/ru/fku/slacker/db/manager/Add.fxml", Data.textDict("DB.Create", tableName)) {}
    }

    override fun onButtonClickEdit() {
        showModal("/ru/fku/slacker/db/manager/Edit.fxml", Data.textDict("DB.Edit", tableName)) {
            Data.updateDB()
            val result = Data.dbManager
                .where { Managers.id eq selectId }
                .map { Manager.getRows(it) }
                .firstOrNull()
            result?.let {
                formEditController.run {
                    fieldName.text = it.name
                    fieldPost.text = it.post
                }
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/ru/fku/slacker/db/manager/Delete.fxml", Data.textDict("DB.Delete", tableName)) {
            Data.updateDB()
            val result = Data.dbManager
                .where { Managers.id eq selectId }
                .map { Manager.getRows(it) }
                .firstOrNull()
            result?.let {
                formDeleteController.run {
                    fieldName.text = it.name
                    fieldPost.text = it.post
                }
            }
        }
    }
}

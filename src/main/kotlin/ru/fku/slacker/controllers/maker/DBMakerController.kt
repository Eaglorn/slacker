package ru.fku.slacker.controllers.maker

import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseController
import ru.fku.slacker.db.Maker
import ru.fku.slacker.db.MakerTable
import ru.fku.slacker.db.Makers

class DBMakerController(table : TableView2<MakerTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<MakerTable>(table, buttonEdit, buttonDelete) {

    lateinit var formEditController : DBMakerFormEditController
    lateinit var formDeleteController : DBMakerFormDeleteController

    init {
        tableName = "Maker"
        createMethods(tableName)
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbMaker
            .map { Maker.getRows(it) }
            .forEach { table.items.add(MakerTable(it.id, it.name)) }
    }

    override fun onButtonClickAdd() {
        showModal("/ru/fku/slacker/db/maker/Add.fxml", Data.textDict("DB.Create", tableName)) {}
    }

    override fun onButtonClickEdit() {
        showModal("/ru/fku/slacker/db/maker/Edit.fxml", Data.textDict("DB.Edit", tableName)) { controller ->
            val result = Data.dbMaker
                .where { (Makers.id eq selectId) }
                .map { Maker.getRows(it) }
                .firstOrNull()
            result?.let {
                formEditController.fieldName.text = it.name
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/ru/fku/slacker/db/maker/Delete.fxml", Data.textDict("DB.Delete", tableName)) { controller ->
            val result = Data.dbMaker
                .where { (Makers.id eq selectId) }
                .map { Maker.getRows(it) }
                .firstOrNull()
            result?.let {
                formDeleteController.fieldName.text = it.name
            }
        }
    }
}
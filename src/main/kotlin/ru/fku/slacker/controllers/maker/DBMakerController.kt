package ru.fku.slacker.controllers.maker

import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import ru.fku.slacker.Data
import ru.fku.slacker.db.Maker
import ru.fku.slacker.db.MakerTable
import ru.fku.slacker.db.Makers
import ru.fku.slacker.utils.BaseController

class DBMakerController(table : TableView2<MakerTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<MakerTable>(table, buttonEdit, buttonDelete) {

    lateinit var formEditController : DBMakerFormEditController
    lateinit var formDeleteController : DBMakerFormDeleteController

    init {
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.Companion.dbMaker
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .forEach { table.items.add(MakerTable(it.id, it.name)) }
    }

    override fun onButtonClickAdd() {
        showModal("/db/maker/Add.fxml", "Создание записи производитель") {}
    }

    override fun onButtonClickEdit() {
        showModal("/db/maker/Edit.fxml", "Редактирование записи производитель") { controller ->
            val result = Data.Companion.dbMaker
                .where { (Makers.id eq selectId) }
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .firstOrNull()
            result?.let {
                formEditController.fieldName.text = it.name
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/db/maker/Delete.fxml", "Удаление записи производитель") { controller ->
            val result = Data.Companion.dbMaker
                .where { (Makers.id eq selectId) }
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .firstOrNull()
            result?.let {
                formDeleteController.fieldName.text = it.name
            }
        }
    }
}
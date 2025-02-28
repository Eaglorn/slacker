package controllers.maker

import Data
import db.Maker
import db.MakerTable
import db.Makers
import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import utils.BaseController

class DBMakerController(table: TableView2<MakerTable>, buttonEdit: Button, buttonDelete: Button) : BaseController<MakerTable>(table, buttonEdit, buttonDelete) {
    lateinit var formEditController: DBMakerFormEditController
    lateinit var formDeleteController: DBMakerFormDeleteController

    init {
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbMaker
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .forEach { table.items.add(MakerTable(it.id, it.name)) }
    }

    override fun onButtonClickAdd() {
        showModal("/db/maker/Add.fxml", "Создание записи производитель") {}
    }

    override fun onButtonClickEdit() {
        showModal("/db/maker/Edit.fxml", "Редактирование записи производитель") { controller ->
            val result = Data.dbMaker
                .where { (Makers.id eq selectId) }
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .firstOrNull()
            if (result != null) {
                (controller as DBMakerFormEditController).fieldName.text = result.name
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/db/maker/Delete.fxml", "Удаление записи производитель") { controller ->
            val result = Data.dbMaker
                .where { (Makers.id eq selectId) }
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .firstOrNull()
            if (result != null) {
                (controller as DBMakerFormDeleteController).fieldName.text = result.name
            }
        }
    }
}
package controllers.defect

import Data
import db.Defect
import db.DefectTable
import db.Defects
import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.map
import utils.BaseController

class DBDefectController(table : TableView2<DefectTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<DefectTable>(table, buttonEdit, buttonDelete) {

    lateinit var formAddController : DBDefectFormAddController
    //lateinit var formEditController : DBDefectFormEditController
    //lateinit var formDeleteController : DBDefectFormDeleteController

    init {
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbUser
            .map { row ->
                Defect(
                    row[Defects.id],
                    row[Defects.hardware],
                    row[Defects.result_view],
                    row[Defects.detect],
                    row[Defects.reason]
                )
            }
            .forEach { table.items.add(DefectTable(it.id, it.hardware, it.result_view, it.detect, it.reason)) }
    }

    override fun onButtonClickAdd() {
        showModal("/db/defect/Add.fxml", "Создание записи дефект") {
            formAddController.boxHardware.items.apply {
                add("ИБП")
                add("Коммутатор")
                add("Монитор")
                add("МФУ")
                add("Ноутбук")
                add("Принтер термотрансферный")
                add("Принтер")
                add("Рабочая станция")
                add("Сервер")
                add("Сканер штрихового кода")
                add("Сканер")
            }
        }
    }

    override fun onButtonClickEdit() {
    }

    override fun onButtonClickDelete() {
    }
}
package ru.fku.slacker.controllers.defect

import javafx.scene.control.Button
import org.controlsfx.control.SearchableComboBox
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseController
import ru.fku.slacker.db.Defect
import ru.fku.slacker.db.DefectTable
import ru.fku.slacker.db.Defects

class DBDefectController(table : TableView2<DefectTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<DefectTable>(table, buttonEdit, buttonDelete) {

    lateinit var formAddController : DBDefectFormAddController
    lateinit var formEditController : DBDefectFormEditController
    lateinit var formDeleteController : DBDefectFormDeleteController

    init {
        tableName = "Defect"
        createMethods(tableName)
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbDefect
            .map { row -> Defect.getRows(row) }
            .forEach { table.items.add(DefectTable(it.id, it.hardware, it.result_view, it.detect, it.reason)) }
    }

    fun boxHardwareApply(box : SearchableComboBox<String>) {
        box.items.apply {
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

    override fun onButtonClickAdd() {
        showModal("/db/defect/Add.fxml", Data.textDict("DB.Create", tableName)) {
            boxHardwareApply(Data.dbDefectController.formAddController.boxHardware)
        }
    }

    override fun onButtonClickEdit() {
        showModal("/db/defect/Edit.fxml", Data.textDict("DB.Edit", tableName)) {
            boxHardwareApply(Data.dbDefectController.formEditController.boxHardware)
            Data.updateDB()
            val result = Data.dbDefect
                .where { Defects.id eq selectId }
                .map { row -> Defect.getRows(row) }
                .firstOrNull()
            result?.let {
                formEditController.run {
                    boxHardware.selectionModel.select(it.hardware)
                    areaResultView.text = it.result_view
                    areaDetect.text = it.detect
                    areaReason.text = it.reason
                }
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/db/defect/Delete.fxml", Data.textDict("DB.Delete", tableName)) {
            Data.updateDB()
            val result = Data.dbDefect
                .where { Defects.id eq selectId }
                .map { row -> Defect.getRows(row) }
                .firstOrNull()
            result?.let {
                formDeleteController.run {
                    boxHardware.text = it.hardware
                    areaResultView.text = it.result_view
                    areaDetect.text = it.detect
                    areaReason.text = it.reason
                }
            }
        }
    }
}
package controllers.defect

import Data
import db.Defect
import db.DefectTable
import db.Defects
import javafx.scene.control.Button
import org.controlsfx.control.SearchableComboBox
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import utils.BaseController

class DBDefectController(table : TableView2<DefectTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<DefectTable>(table, buttonEdit, buttonDelete) {

    lateinit var formAddController : DBDefectFormAddController
    lateinit var formEditController : DBDefectFormEditController
    lateinit var formDeleteController : DBDefectFormDeleteController

    init {
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbDefect
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

    fun BoxHardwareApply(box : SearchableComboBox<String>) {
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
        showModal("/db/defect/Add.fxml", "Создание записи дефект") {
            BoxHardwareApply(Data.dbDefectController.formAddController.boxHardware)
        }
    }

    override fun onButtonClickEdit() {
        showModal("/db/defect/Edit.fxml", "Редактирование записи модель") {
            BoxHardwareApply(Data.dbDefectController.formEditController.boxHardware)
            Data.updateDB()
            val result = Data.dbDefect
                .where { Defects.id eq selectId }
                .map { row ->
                    Defect(
                        row[Defects.id],
                        row[Defects.hardware],
                        row[Defects.result_view],
                        row[Defects.detect],
                        row[Defects.reason]
                    )
                }
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
        showModal("/db/defect/Delete.fxml", "Удаление записи дефект") {
            Data.updateDB()
            val result = Data.dbDefect
                .where { Defects.id eq selectId }
                .map { row ->
                    Defect(
                        row[Defects.id],
                        row[Defects.hardware],
                        row[Defects.result_view],
                        row[Defects.detect],
                        row[Defects.reason]
                    )
                }
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
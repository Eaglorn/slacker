package controllers

import db.MakerTable
import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2

class DBMakerController(
    private val tableMaker: TableView2<MakerTable>,
    val buttonTableMakerEdit: Button,
    val buttonTableMakerDelete: Button
) {

    fun onTableSelect() {
        tableMaker.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue.let {
                println("Selected Person: ${it.getId()} ${it.getName()}")
            }
        }
    }

    fun onButtonClickAdd() {

    }


    fun onButtonClickEdit() {

    }


    fun onButtonClickDelete() {

    }
}
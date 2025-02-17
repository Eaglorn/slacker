package controllers.model

import Config
import SqliteDatabase
import db.Model
import db.ModelTable
import db.Models
import javafx.scene.control.Button
import javafx.stage.Stage
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DBModelController(
    val table: TableView2<ModelTable>,
    val buttonEdit: Button,
    val buttonDelete: Button
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    lateinit var formStage: Stage

    var selectId: Int = -1

    init {
        buttonEdit.disableProperty().set(true)
        buttonDelete.disableProperty().set(true)
        table.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue.let {
                if (it != null) {
                    selectId = it.getId()
                    if (buttonEdit.disableProperty().get()) {
                        buttonEdit.disableProperty().set(false)
                    }
                    if (buttonDelete.disableProperty().get()) {
                        buttonDelete.disableProperty().set(false)
                    }
                } else {
                    selectId = -1
                    if (!buttonEdit.disableProperty().get()) {
                        buttonEdit.disableProperty().set(true)
                    }
                    if (!buttonDelete.disableProperty().get()) {
                        buttonDelete.disableProperty().set(true)
                    }
                }
            }
        }
    }

    fun reloadTable() {
        val database = SqliteDatabase.connect(Config.pathDBLocal)

        val query = database.from(Models).select()

        table.items.clear()

        query
            .map { row -> Model(row[Models.id], row[Models.name], row[Models.maker], row[Models.typeofhardware]) }
            .forEach {
                table.items.add(ModelTable(it.id, it.name, it.maker, it.typeofhardware))
            }
    }
}
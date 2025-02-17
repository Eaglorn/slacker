package controllers.model

import Data
import db.*
import javafx.scene.control.Button
import javafx.stage.Stage
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
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
        table.items.clear()
        Data.dbModel
            .map { row -> Model(row[Models.id], row[Models.name], row[Models.maker], row[Models.type_of_hardware]) }
            .forEach {
                val maker = Data.dbMaker
                    .where { (Makers.id eq it.maker!!) }
                    .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                    .firstOrNull()
                val typeOfHardware = Data.dbTypeOfHardware
                    .where { (TypeOfHardwares.id eq it.type_of_hardware!!) }
                    .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                    .firstOrNull()
                table.items.add(ModelTable(it.id, it.name, maker?.name, typeOfHardware?.name))
            }
    }

    fun onButtonClickAdd() {
    }

    fun onButtonClickEdit() {}

    fun onButtonClickDelete() {
    }
}
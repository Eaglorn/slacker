package controllers

import Data
import db.MakerTable
import db.ModelTable
import db.TypeOfHardwareTable
import db.UserTable
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import org.controlsfx.control.tableview2.TableColumn2
import org.controlsfx.control.tableview2.TableView2
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
open class SlackerControllerData {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML lateinit var tabWriteOff: Tab

    @FXML lateinit var tabExpertise: Tab

    @FXML lateinit var tabDataBase: Tab

    @FXML lateinit var fieldLoadDatabase: TextField

    @FXML lateinit var fieldLoadTemplates: TextField

    @FXML lateinit var tableMaker: TableView2<MakerTable>

    @FXML lateinit var tableMakerColumnId: TableColumn2<MakerTable, String>

    @FXML lateinit var tableMakerColumnName: TableColumn2<MakerTable, String>

    @FXML lateinit var buttonTableMakerEdit: Button

    @FXML lateinit var buttonTableMakerDelete: Button

    @FXML lateinit var tableTypeOfHardware: TableView2<TypeOfHardwareTable>

    @FXML lateinit var tableTypeOfHardwareColumnId: TableColumn2<TypeOfHardwareTable, String>

    @FXML lateinit var tableTypeOfHardwareColumnName: TableColumn2<TypeOfHardwareTable, String>

    @FXML lateinit var buttonTableTypeOfHardwareEdit: Button

    @FXML lateinit var buttonTableTypeOfHardwareDelete: Button

    @FXML lateinit var tableModel: TableView2<ModelTable>

    @FXML lateinit var tableModelColumnId: TableColumn2<ModelTable, String>

    @FXML lateinit var tableModelColumnName: TableColumn2<ModelTable, String>

    @FXML lateinit var tableModelColumnMaker: TableColumn2<ModelTable, String>

    @FXML lateinit var tableModelColumnTypeOfHardware: TableColumn2<ModelTable, String>

    @FXML lateinit var buttonTableModelEdit: Button

    @FXML lateinit var buttonTableModelDelete: Button

    @FXML lateinit var tableUser: TableView2<UserTable>

    @FXML lateinit var tableUserColumnId: TableColumn2<UserTable, String>

    @FXML lateinit var tableUserColumnName: TableColumn2<UserTable, String>

    @FXML lateinit var tableUserColumnPost: TableColumn2<UserTable, String>

    @FXML lateinit var tableUserColumnAddress: TableColumn2<UserTable, String>

    @FXML lateinit var buttonTableUserEdit: Button

    @FXML lateinit var buttonTableUserDelete: Button

    @FXML private fun onButtonClickLoadDataBase() {
        Data.settingController.onButtonClickLoadDataBase()
    }

    @FXML private fun onButtonClickLoadTemplates() {
        Data.settingController.onButtonClickLoadTemplates()
    }

    @FXML private fun onButtonClickDBMakerAdd() {
        Data.dbMakerController.onButtonClickAdd()
    }

    @FXML private fun onButtonClickDBMakerEdit() {
        Data.dbMakerController.onButtonClickEdit()
    }

    @FXML private fun onButtonClickDBMakerDelete() {
        Data.dbMakerController.onButtonClickDelete()
    }

    @FXML private fun onButtonClickDBTypeOfHardwareAdd() {
        Data.dbTypeOfHardwareController.onButtonClickAdd()
    }

    @FXML private fun onButtonClickDBTypeOfHardwareEdit() {
        Data.dbTypeOfHardwareController.onButtonClickEdit()
    }

    @FXML private fun onButtonClickDBTypeOfHardwareDelete() {
        Data.dbTypeOfHardwareController.onButtonClickDelete()
    }

    @FXML private fun onButtonClickDBModelAdd() {
        Data.dbModelController.onButtonClickAdd()
    }

    @FXML private fun onButtonClickDBModelEdit() {
        Data.dbModelController.onButtonClickEdit()
    }

    @FXML private fun onButtonClickDBModelDelete() {
        Data.dbModelController.onButtonClickDelete()
    }

    @FXML private fun onButtonClickDBUserAdd() {
        Data.dbUserController.onButtonClickAdd()
    }

    @FXML private fun onButtonClickDBUserEdit() {
        Data.dbUserController.onButtonClickEdit()
    }

    @FXML private fun onButtonClickDBUserDelete() {
        Data.dbUserController.onButtonClickDelete()
    }

    @FXML private fun onButtonLoadApp() {
        if (Data.config.pathDB.isNotEmpty() && Data.config.pathTemplates.isNotEmpty()) {

            Data.updateDB()

            Data.dbMakerController.reloadTable()
            Data.dbTypeOfHardwareController.reloadTable()
            Data.dbModelController.reloadTable()
            Data.dbUserController.reloadTable()

            tabWriteOff.disableProperty().set(false)
            tabExpertise.disableProperty().set(false)
            tabDataBase.disableProperty().set(false)
        }
    }
}
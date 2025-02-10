package controllers

import Config
import Data
import db.MakerTable
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import org.controlsfx.control.tableview2.TableColumn2
import org.controlsfx.control.tableview2.TableView2
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SlackerController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var data: Data.companion = Data.companion

    @FXML
    lateinit var tabWriteOff: Tab

    @FXML
    lateinit var tabExpertise: Tab

    @FXML
    lateinit var tabDataBase: Tab

    @FXML
    lateinit var fieldLoadDatabase: TextField

    @FXML
    lateinit var fieldLoadTemplates: TextField

    @FXML
    lateinit var tableMaker: TableView2<MakerTable>

    @FXML
    lateinit var tableMakerColumnId: TableColumn2<MakerTable, String>

    @FXML
    lateinit var tableMakerColumnName: TableColumn2<MakerTable, String>

    @FXML
    lateinit var buttonTableMakerEdit: Button

    @FXML
    lateinit var buttonTableMakerDelete: Button

    init {
        Data.companion.controller = this
        Data.companion.config = Config.load()
    }

    fun beforeShow() {
        tableMakerColumnId.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getId().toString()) }
        tableMakerColumnName.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getName()) }

        data.settingController = SettingController(fieldLoadDatabase, fieldLoadTemplates)
        data.dbMakerController = DBMakerController(tableMaker, buttonTableMakerEdit, buttonTableMakerDelete)

        data.dbMakerController.reloadTable()

        fieldLoadDatabase.text = Data.companion.config.pathDB
        fieldLoadTemplates.text = Data.companion.config.pathTemplates
    }

    @FXML
    private fun onButtonClickLoadDataBase() {
        data.settingController.onButtonClickLoadDataBase()
    }

    @FXML
    private fun onButtonClickLoadTemplates() {
        data.settingController.onButtonClickLoadTemplates()
    }

    @FXML
    private fun onButtonClickDBMakerAdd() {
        data.dbMakerController.onButtonClickAdd()
    }

    @FXML
    private fun onButtonClickDBMakerEdit() {
        data.dbMakerController.onButtonClickEdit()
    }

    @FXML
    private fun onButtonClickDBMakerDelete() {
        data.dbMakerController.onButtonClickDelete()
    }

    @FXML
    private fun onButtonLoadApp() {
        tabWriteOff.disableProperty().set(false)
        tabExpertise.disableProperty().set(false)
        tabDataBase.disableProperty().set(false)
    }
}
package controllers

import Config
import Data
import controllers.maker.DBMakerController
import controllers.model.DBModelController
import controllers.typeofhardware.DBTypeOfHardwareController
import db.MakerTable
import db.ModelTable
import db.TypeOfHardwareTable
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import org.apache.commons.io.FileUtils
import org.controlsfx.control.tableview2.TableColumn2
import org.controlsfx.control.tableview2.TableView2
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class SlackerController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

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

    @FXML
    lateinit var tableTypeOfHardware: TableView2<TypeOfHardwareTable>

    @FXML
    lateinit var tableTypeOfHardwareColumnId: TableColumn2<TypeOfHardwareTable, String>

    @FXML
    lateinit var tableTypeOfHardwareColumnName: TableColumn2<TypeOfHardwareTable, String>

    @FXML
    lateinit var buttonTableTypeOfHardwareEdit: Button

    @FXML
    lateinit var buttonTableTypeOfHardwareDelete: Button

    @FXML
    lateinit var tableModel: TableView2<ModelTable>

    @FXML
    lateinit var tableModelColumnId: TableColumn2<ModelTable, String>

    @FXML
    lateinit var tableModelColumnName: TableColumn2<ModelTable, String>

    @FXML
    lateinit var tableModelColumnMaker: TableColumn2<ModelTable, String>

    @FXML
    lateinit var tableModelColumnTypeOfHardware: TableColumn2<ModelTable, String>

    @FXML
    lateinit var buttonTableModelEdit: Button

    @FXML
    lateinit var buttonTableModelDelete: Button

    init {
        Data.controller = this
        Data.config = Config.load()

        if (Data.config.pathDB.isNotEmpty()) {
            val file: File = File(Data.config.pathDB)
            if (file.exists()) {
                val lastModified: Date = Date(file.lastModified())
                val instant: Instant = lastModified.toInstant()
                val localDateTime: LocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                if (Data.config.dateTimeDB.isBefore(localDateTime)) {
                    FileUtils.copyFile(file, File(Config.pathDirectory + "slacker.db"))
                }
            }
        }
    }

    fun setTabsDisabled() {
        tabWriteOff.disableProperty().set(true)
        tabExpertise.disableProperty().set(true)
        tabDataBase.disableProperty().set(true)
    }

    fun beforeShow() {
        tableMakerColumnId.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getId().toString()) }
        tableMakerColumnName.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getName()) }

        tableTypeOfHardwareColumnId.setCellValueFactory { cellData ->
            SimpleStringProperty(
                cellData.value.getId().toString()
            )
        }
        tableTypeOfHardwareColumnName.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getName()) }

        tableModelColumnId.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getId().toString()) }
        tableModelColumnName.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getName()) }
        tableModelColumnMaker.setCellValueFactory { cellData ->
            SimpleStringProperty(
                cellData.value.getMaker().toString()
            )
        }
        tableModelColumnTypeOfHardware.setCellValueFactory { cellData ->
            SimpleStringProperty(
                cellData.value.getTypeOfHardware().toString()
            )
        }

        Data.settingController = SettingController(fieldLoadDatabase, fieldLoadTemplates)

        Data.dbMakerController = DBMakerController(tableMaker, buttonTableMakerEdit, buttonTableMakerDelete)
        Data.dbTypeOfHardwareController = DBTypeOfHardwareController(
            tableTypeOfHardware,
            buttonTableTypeOfHardwareEdit,
            buttonTableTypeOfHardwareDelete
        )
        Data.dbModelController = DBModelController(tableModel, buttonTableModelEdit, buttonTableModelDelete)

        if (Data.config.pathDB.isNotEmpty()) {
            Data.updateDB()
            Data.dbMakerController.reloadTable()
            Data.dbTypeOfHardwareController.reloadTable()
            Data.dbModelController.reloadTable()
            if (Data.config.pathTemplates.isNotEmpty()) {
                fieldLoadDatabase.text = Data.config.pathDB
                fieldLoadTemplates.text = Data.config.pathTemplates
                tabWriteOff.disableProperty().set(false)
                tabExpertise.disableProperty().set(false)
                tabDataBase.disableProperty().set(false)
            } else {
                setTabsDisabled()
            }
        } else {
            setTabsDisabled()
        }
    }

    @FXML
    private fun onButtonClickLoadDataBase() {
        Data.settingController.onButtonClickLoadDataBase()
    }

    @FXML
    private fun onButtonClickLoadTemplates() {
        Data.settingController.onButtonClickLoadTemplates()
    }

    @FXML
    private fun onButtonClickDBMakerAdd() {
        Data.dbMakerController.onButtonClickAdd()
    }

    @FXML
    private fun onButtonClickDBMakerEdit() {
        Data.dbMakerController.onButtonClickEdit()
    }

    @FXML
    private fun onButtonClickDBMakerDelete() {
        Data.dbMakerController.onButtonClickDelete()
    }

    @FXML
    private fun onButtonClickDBTypeOfHardwareAdd() {
        Data.dbTypeOfHardwareController.onButtonClickAdd()
    }

    @FXML
    private fun onButtonClickDBTypeOfHardwareEdit() {
        Data.dbTypeOfHardwareController.onButtonClickEdit()
    }

    @FXML
    private fun onButtonClickDBTypeOfHardwareDelete() {
        Data.dbTypeOfHardwareController.onButtonClickDelete()
    }

    @FXML
    private fun onButtonClickDBModelAdd() {
        Data.dbModelController.onButtonClickAdd()
    }

    @FXML
    private fun onButtonClickDBModelEdit() {
        Data.dbModelController.onButtonClickEdit()
    }

    @FXML
    private fun onButtonClickDBModelDelete() {
        Data.dbModelController.onButtonClickDelete()
    }

    @FXML
    private fun onButtonLoadApp() {
        if (Data.config.pathDB.isNotEmpty() && Data.config.pathTemplates.isNotEmpty()) {

            Data.updateDB()

            Data.dbMakerController.reloadTable()
            Data.dbTypeOfHardwareController.reloadTable()
            Data.dbModelController.reloadTable()

            tabWriteOff.disableProperty().set(false)
            tabExpertise.disableProperty().set(false)
            tabDataBase.disableProperty().set(false)
        }
    }
}
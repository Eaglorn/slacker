package ru.fku.slacker.controllers

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
import org.springframework.context.annotation.Bean
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.SlackerApplication
import ru.fku.slacker.controllers.defect.DBDefectController
import ru.fku.slacker.controllers.maker.DBMakerController
import ru.fku.slacker.controllers.model.DBModelController
import ru.fku.slacker.controllers.typeofhardware.DBTypeOfHardwareController
import ru.fku.slacker.controllers.user.DBUserController
import ru.fku.slacker.db.*
import java.io.File
import java.lang.reflect.Method
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class SlackerController {
    lateinit var buttonLoadTemplates : Button
    lateinit var buttonLoadDatabase : Button

    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var tabWriteOff : Tab

    @FXML
    lateinit var tabExpertise : Tab

    @FXML
    lateinit var tabDataBase : Tab

    @FXML
    lateinit var fieldLoadDatabase : TextField

    @FXML
    lateinit var fieldLoadTemplates : TextField

    @FXML
    lateinit var tableMaker : TableView2<MakerTable>

    @FXML
    lateinit var tableMakerColumnId : TableColumn2<MakerTable, String>

    @FXML
    lateinit var tableMakerColumnName : TableColumn2<MakerTable, String>

    @FXML
    lateinit var buttonTableMakerEdit : Button

    @FXML
    lateinit var buttonTableMakerDelete : Button

    @FXML
    lateinit var tableTypeOfHardware : TableView2<TypeOfHardwareTable>

    @FXML
    lateinit var tableTypeOfHardwareColumnId : TableColumn2<TypeOfHardwareTable, String>

    @FXML
    lateinit var tableTypeOfHardwareColumnName : TableColumn2<TypeOfHardwareTable, String>

    @FXML
    lateinit var buttonTableTypeOfHardwareEdit : Button

    @FXML
    lateinit var buttonTableTypeOfHardwareDelete : Button

    @FXML
    lateinit var tableModel : TableView2<ModelTable>

    @FXML
    lateinit var tableModelColumnId : TableColumn2<ModelTable, String>

    @FXML
    lateinit var tableModelColumnName : TableColumn2<ModelTable, String>

    @FXML
    lateinit var tableModelColumnMaker : TableColumn2<ModelTable, String>

    @FXML
    lateinit var tableModelColumnTypeOfHardware : TableColumn2<ModelTable, String>

    @FXML
    lateinit var buttonTableModelEdit : Button

    @FXML
    lateinit var buttonTableModelDelete : Button

    @FXML
    lateinit var tableUser : TableView2<UserTable>

    @FXML
    lateinit var tableUserColumnId : TableColumn2<UserTable, String>

    @FXML
    lateinit var tableUserColumnName : TableColumn2<UserTable, String>

    @FXML
    lateinit var tableUserColumnPost : TableColumn2<UserTable, String>

    @FXML
    lateinit var tableUserColumnAddress : TableColumn2<UserTable, String>

    @FXML
    lateinit var buttonTableUserEdit : Button

    @FXML
    lateinit var buttonTableUserDelete : Button

    @FXML
    lateinit var tableDefect : TableView2<DefectTable>

    @FXML
    lateinit var tableDefectColumnId : TableColumn2<DefectTable, String>

    @FXML
    lateinit var tableDefectColumnHardware : TableColumn2<DefectTable, String>

    @FXML
    lateinit var tableDefectColumnResultView : TableColumn2<DefectTable, String>

    @FXML
    lateinit var tableDefectColumnDetect : TableColumn2<DefectTable, String>

    @FXML
    lateinit var tableDefectColumnReason : TableColumn2<DefectTable, String>

    @FXML
    lateinit var buttonTableDefectEdit : Button

    @FXML
    lateinit var buttonTableDefectDelete : Button

    init {
        Data.controller = this
        Data.config = Config.load()
        if (Data.config.pathDB.isNotEmpty()) {
            val file = File(Data.config.pathDB)
            if (file.exists()) {
                val lastModified = Date(file.lastModified())
                val instant : Instant = lastModified.toInstant()
                val localDateTime : LocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                if (Data.config.dateTimeDB.isBefore(localDateTime)) {
                    FileUtils.copyFile(file, File(Config.pathDirectory + "slacker.db"))
                }
            }
        }
    }

    private fun setTabsDisabled() {
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
                cellData.value.getMaker()
            )
        }
        tableModelColumnTypeOfHardware.setCellValueFactory { cellData ->
            SimpleStringProperty(
                cellData.value.getTypeOfHardware()
            )
        }

        tableUserColumnId.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getId().toString()) }
        tableUserColumnName.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getName()) }
        tableUserColumnPost.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getPost()) }
        tableUserColumnAddress.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getAddress()) }

        tableDefectColumnId.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getId().toString()) }
        tableDefectColumnHardware.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getHardware()) }
        tableDefectColumnResultView.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getResultView()) }
        tableDefectColumnDetect.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getDetect()) }
        tableDefectColumnReason.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getReason()) }

        Data.settingController = SettingController(fieldLoadDatabase, fieldLoadTemplates)
        Data.dbMakerController = DBMakerController(tableMaker, buttonTableMakerEdit, buttonTableMakerDelete)
        Data.dbTypeOfHardwareController = DBTypeOfHardwareController(
            tableTypeOfHardware,
            buttonTableTypeOfHardwareEdit,
            buttonTableTypeOfHardwareDelete
        )
        Data.dbModelController = DBModelController(tableModel, buttonTableModelEdit, buttonTableModelDelete)
        Data.dbUserController = DBUserController(tableUser, buttonTableUserEdit, buttonTableUserDelete)
        Data.dbDefectController =
            DBDefectController(tableDefect, buttonTableDefectEdit, buttonTableDefectDelete)

        if (SlackerApplication.databaseCreate) {
            SlackerApplication.applicationContext.let { context ->
                val beanNames = context.beanDefinitionNames
                for (beanName in beanNames) {
                    if (beanName.contains("DB.Class.")) {
                        val bean = context.getBean(beanName)
                        val methods : Array<Method> = bean.javaClass.methods
                        for (method in methods) {
                            method.getAnnotationsByType(Bean::class.java).firstOrNull()?.let {
                                for (item in it.name) {
                                    if (item.contains("DB.Create.")) {
                                        method.invoke(bean)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Data.run {
            updateDB()
            reloadTable()
        }
        if (Data.config.pathTemplates.isNotEmpty()) {
            fieldLoadDatabase.text = Data.config.pathDB
            fieldLoadTemplates.text = Data.config.pathTemplates
            tabWriteOff.disableProperty().set(false)
            tabExpertise.disableProperty().set(false)
            tabDataBase.disableProperty().set(false)
        } else {
            setTabsDisabled()
        }
    }


    @Suppress("unused")
    @FXML
    private fun onButtonClickLoadDataBase() {
        Data.settingController.onButtonClickLoadDataBase()
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickLoadTemplates() {
        Data.settingController.onButtonClickLoadTemplates()
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBMakerAdd() {
        Data.onButtonClickTable("Maker", "Add")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBMakerEdit() {
        Data.onButtonClickTable("Maker", "Edit")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBMakerDelete() {
        Data.onButtonClickTable("Maker", "Delete")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBTypeOfHardwareAdd() {
        Data.onButtonClickTable("TypeOfHardware", "Add")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBTypeOfHardwareEdit() {
        Data.onButtonClickTable("TypeOfHardware", "Edit")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBTypeOfHardwareDelete() {
        Data.onButtonClickTable("TypeOfHardware", "Delete")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBModelAdd() {
        Data.onButtonClickTable("Model", "Add")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBModelEdit() {
        Data.onButtonClickTable("Model", "Edit")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBModelDelete() {
        Data.onButtonClickTable("Model", "Delete")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBUserAdd() {
        Data.onButtonClickTable("User", "Add")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBUserEdit() {
        Data.onButtonClickTable("User", "Edit")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBUserDelete() {
        Data.onButtonClickTable("User", "Delete")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBDefectAdd() {
        Data.onButtonClickTable("Defect", "Add")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBDefectEdit() {
        Data.onButtonClickTable("Defect", "Edit")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDBDefectDelete() {
        Data.onButtonClickTable("Defect", "Delete")
    }

    @Suppress("unused")
    @FXML
    private fun onButtonLoadApp() {
        if (Data.config.pathDB.isNotEmpty() && Data.config.pathTemplates.isNotEmpty()) {
            Data.run {
                updateDB()
                reloadTable()
            }
            tabWriteOff.disableProperty().set(false)
            tabExpertise.disableProperty().set(false)
            tabDataBase.disableProperty().set(false)
        }
    }
}

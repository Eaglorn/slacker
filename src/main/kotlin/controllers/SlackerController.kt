package controllers

import Config
import Data
import controllers.defect.DBDefectController
import controllers.maker.DBMakerController
import controllers.model.DBModelController
import controllers.typeofhardware.DBTypeOfHardwareController
import controllers.user.DBUserController
import db.DBCreate
import javafx.beans.property.SimpleStringProperty
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.DBCreateAnnotation
import utils.SqliteDatabase
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class SlackerController : SlackerControllerData() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

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
        Data.dbDefectController = DBDefectController(tableDefect, buttonTableDefectEdit, buttonTableDefectDelete)

        if (Data.config.pathDB.isNotEmpty()) {
            val database = SqliteDatabase.connect(Data.config.pathDB)
            database.useConnection { conn ->
                val dbCreate = DBCreate()
                val methods = dbCreate::class.java.declaredMethods
                val params : ArrayList<Any> = ArrayList()
                params.add(conn)
                for (method in methods) {
                    if (method.isAnnotationPresent(DBCreateAnnotation::class.java)) {
                        method.isAccessible = true
                        method.invoke(dbCreate, *params.toTypedArray())
                    }
                }
            }
        }
        Data.run {
            updateDB()
            dbMakerController.reloadTable()
            dbTypeOfHardwareController.reloadTable()
            dbModelController.reloadTable()
            dbUserController.reloadTable()
            dbDefectController.reloadTable()
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
}
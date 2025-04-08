package ru.fku.slacker.controllers.manager

import javafx.fxml.FXML
import javafx.scene.control.TextField
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseFormController

class DBManagerDeleteController : BaseFormController() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName : TextField

    @FXML
    lateinit var fieldPost : TextField

    init {
        tableName = "manager"
        Data.dbManagerController.formDeleteController = this
    }
}
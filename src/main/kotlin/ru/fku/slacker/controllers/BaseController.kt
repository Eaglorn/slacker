package ru.fku.slacker.controllers

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Modality
import javafx.stage.Stage
import org.controlsfx.control.tableview2.TableView2
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Data
import ru.fku.slacker.utils.Identifiable

abstract class BaseController<T : Identifiable>(
    protected val table : TableView2<T>,
    val buttonEdit : Button,
    val buttonDelete : Button
) {
    protected val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    lateinit var formStage : Stage
    var selectId : Int = - 1
    var tableName : String = ""

    init {
        setButtonsDisable(true)
    }

    fun createMethods(name : String) {
        Data.run {
            metMap["Table.Add.$name"] = { _ -> onButtonClickAdd() }
            metMap["Table.Edit.$name"] = { _ -> onButtonClickEdit() }
            metMap["Table.Delete.$name"] = { _ -> onButtonClickDelete() }
        }
        Data.metMap["Table.Reload.$name"] = { _ -> reloadTable() }
    }

    fun setButtonsDisable(value : Boolean) {
        buttonEdit.isDisable = value
        buttonDelete.isDisable = value
    }

    private fun toggleButtons(isEnabled : Boolean) {
        buttonEdit.isDisable = ! isEnabled
        buttonDelete.isDisable = ! isEnabled
    }

    protected fun setupTableListener() {
        table.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            selectId = newValue?.getId() ?: - 1
            toggleButtons(selectId != - 1)
        }
    }

    protected fun showModal(fxmlPath : String, title : String?, setupController : (Any) -> Unit) {
        val fxmlLoader = FXMLLoader(javaClass.getResource(fxmlPath))
        val formScene = Scene(fxmlLoader.load())
        formStage = Stage()
        formStage.initModality(Modality.APPLICATION_MODAL)
        formStage.title = title
        formStage.scene = formScene
        setupController(fxmlLoader.getController())
        formStage.showAndWait()
    }

    abstract fun reloadTable()
    abstract fun onButtonClickAdd()
    abstract fun onButtonClickEdit()
    abstract fun onButtonClickDelete()
}
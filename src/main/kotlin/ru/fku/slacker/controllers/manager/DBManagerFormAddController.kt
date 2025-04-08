package ru.fku.slacker.controllers.manager

import javafx.fxml.FXML
import javafx.scene.control.TextField
import org.apache.commons.io.FileUtils
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.controllers.BaseFormController
import ru.fku.slacker.db.Manager
import ru.fku.slacker.db.Managers
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBManagerFormAddController : BaseFormController() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private lateinit var fieldName : TextField

    @FXML
    private lateinit var fieldPost : TextField

    init {
        tableName = "Manager"
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickAdd() {
        val name = fieldName.text
        val post = fieldPost.text
        if (name.isNotEmpty() && post.isNotEmpty()) {
            Data.updateDB()
            val result = Data.dbManager
                .where { Managers.name eq name }
                .map { Manager.getRows(it) }
                .firstOrNull()
            if (result == null) {
                val database = SqliteDatabase.connect(Data.config.pathDB)
                database.insert(Managers) {
                    set(it.name, name)
                    set(it.post, post)
                }
                FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                Data.run {
                    updateDB()
                    reloadTable(tableName)
                    dbManagerController.formStage.close()
                }
            } else {
                Data.showMessage("Warning", Data.textDict("DB.IsIndentFields", tableName))
            }
        } else {
            Data.showMessage("Warning", Data.textDict("DB.IsEmptyFields"))
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbManagerController.formStage.close()
    }
}
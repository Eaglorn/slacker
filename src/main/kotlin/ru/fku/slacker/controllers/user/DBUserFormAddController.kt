package ru.fku.slacker.controllers.user

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
import ru.fku.slacker.db.User
import ru.fku.slacker.db.Users
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBUserFormAddController : BaseFormController() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private lateinit var fieldName : TextField

    @FXML
    private lateinit var fieldPost : TextField

    @FXML
    private lateinit var areaAddress : TextArea

    init {
        tableName = "Defect"
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                val name = fieldName.text
                val post = fieldPost.text
                val address = areaAddress.text
                if (name.isNotEmpty() && post.isNotEmpty() && address.isNotEmpty()) {
                    Data.updateDB()
                    val result = Data.dbUser
                        .where { Users.name eq name }
                        .map { User.getRows(it) }
                        .firstOrNull()
                    if (result == null) {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        database.insert(Users) {
                            set(it.name, name)
                            set(it.post, post)
                            set(it.address, address)
                        }
                        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                        Data.run {
                            updateDB()
                            reloadTable(tableName)
                            dbUserController.formStage.close()
                        }
                    } else {
                        Data.showMessage("Warning", Data.textDict("DB.IsIndentFields", tableName))
                    }
                } else {
                    Data.showMessage("Warning", Data.textDict("DB.IsEmptyFields"))
                }
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbUserController.formStage.close()
    }
}
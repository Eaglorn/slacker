package controllers.user

import Config
import Data
import db.User
import db.Users
import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.SqliteDatabase
import java.io.File

class DBUserFormAddController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private lateinit var fieldName : TextField

    @FXML
    private lateinit var fieldPost : TextField

    @FXML
    private lateinit var areaAddress : TextArea

    @Suppress("unused")
    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                Data.updateDB()
                val result = Data.dbUser
                    .where { (Users.name eq fieldName.text) }
                    .map { row -> User(row[Users.id], row[Users.name], row[Users.post], row[Users.address]) }
                    .firstOrNull()
                if (result == null) {
                    val database = SqliteDatabase.connect(Data.config.pathDB)
                    database.insert(Users) {
                        set(it.name, fieldName.text)
                        set(it.post, fieldPost.text)
                        set(it.address, areaAddress.text)
                    }
                    FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                    Data.updateDB()
                    Data.dbUserController.reloadTable()
                    Data.dbUserController.buttonEdit.disableProperty().set(true)
                    Data.dbUserController.buttonDelete.disableProperty().set(true)
                    Data.dbUserController.formStage.close()
                } else {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись с введённым ФИО уже существует.")
                        .showWarning()
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
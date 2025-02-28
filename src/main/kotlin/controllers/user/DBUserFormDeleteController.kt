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
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.SqliteDatabase
import java.io.File

class DBUserFormDeleteController {
    @Suppress("unused")
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName: TextField
    @FXML
    lateinit var fieldPost: TextField
    @FXML
    lateinit var areaAddress: TextArea

    init {
        Data.dbUserController.formDeleteController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickDelete() {
        if (Data.dbUserController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        }
        runBlocking {
            launch {
                Data.updateDB()

                val result = Data.dbUser
                    .where { (Users.id eq Data.dbUserController.selectId) }
                    .map { row -> User(row[Users.id], row[Users.name], row[Users.post], row[Users.address]) }
                    .firstOrNull()

                if (result == null) {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись с выбранным id в базе отсуствует.")
                        .showWarning()
                } else {
                    val database = SqliteDatabase.connect(Data.config.pathDB)
                    database.delete(Users) {
                        it.id eq result.id!!
                    }
                    FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                    Data.updateDB()
                    Data.dbUserController.reloadTable()
                    Data.dbUserController.buttonEdit.disableProperty().set(true)
                    Data.dbUserController.buttonDelete.disableProperty().set(true)
                    Data.dbUserController.formStage.close()
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
package controllers.user

import Config
import Data
import SqliteDatabase
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
import org.ktorm.dsl.map
import org.ktorm.dsl.update
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class DBUserFormEditController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName: TextField

    @FXML
    lateinit var fieldPost: TextField

    @FXML
    lateinit var areaAddress: TextArea

    init {
        Data.dbUserController.formEditController = this
    }

    @FXML
    private fun onButtonClickEdit() {
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
                    if (result.name.equals(fieldName.text) && result.post.equals(fieldPost.text) && result.address.equals(
                            areaAddress.text
                        )
                    ) {
                        Notifications.create()
                            .title("Предупреждение!")
                            .text("Запись составитель с введёнными значениями уже существует.")
                            .showWarning()
                    } else {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        database.update(Users) {
                            set(it.name, fieldName.text)
                            set(it.post, fieldPost.text)
                            set(it.address, areaAddress.text)
                            where { it.id eq result.id!! }
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
    }

    @FXML
    private fun onButtonClickCancel() {
        Data.dbUserController.formStage.close()
    }
}
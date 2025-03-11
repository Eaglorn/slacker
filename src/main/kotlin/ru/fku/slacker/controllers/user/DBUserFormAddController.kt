package ru.fku.slacker.controllers.user

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
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.db.User
import ru.fku.slacker.db.Users
import ru.fku.slacker.utils.SqliteDatabase
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
                Data.Companion.updateDB()
                val result = Data.Companion.dbUser
                    .where { (Users.name eq fieldName.text) }
                    .map { row -> User(row[Users.id], row[Users.name], row[Users.post], row[Users.address]) }
                    .firstOrNull()
                if (result == null) {
                    val database = SqliteDatabase.connect(Data.Companion.config.pathDB)
                    database.insert(Users) {
                        set(it.name, fieldName.text)
                        set(it.post, fieldPost.text)
                        set(it.address, areaAddress.text)
                    }
                    FileUtils.copyFile(File(Data.Companion.config.pathDB), File(Config.Companion.pathDBLocal))
                    Data.Companion.run {
                        updateDB()
                        dbUserController.run {
                            reloadTable()
                            formStage.close()
                        }
                    }
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
        Data.Companion.dbUserController.formStage.close()
    }
}
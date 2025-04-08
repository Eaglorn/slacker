package ru.fku.slacker.controllers.user

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import org.apache.commons.io.FileUtils
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.update
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

class DBUserFormEditController : BaseFormController() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var fieldName : TextField

    @FXML
    lateinit var fieldPost : TextField

    @FXML
    lateinit var areaAddress : TextArea

    init {
        tableName = "User"
        Data.dbUserController.formEditController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickEdit() {
        if (Data.dbUserController.selectId < 0) {
            Data.showMessage("Warning", Data.textDict("DB.IsSelectRecord"))
        } else {
            val name = fieldName.text
            val post = fieldPost.text
            val address = areaAddress.text
            if (name.isNotEmpty() && post.isNotEmpty() && address.isNotEmpty()) {
                Data.updateDB()
                val result = Data.dbUser
                    .where { (Users.id eq Data.dbUserController.selectId) }
                    .map { User.getRows(it) }
                    .firstOrNull()
                if (result == null) {
                    Data.showMessage("Warning", Data.textDict("DB.IsSelectId"))
                } else {
                    if (result.name.equals(name) && result.post.equals(post) && result.address.equals(address)) {
                        Data.showMessage("Warning", Data.textDict("DB.IsIndentFields"))
                    } else {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        database.update(Users) {
                            set(it.name, fieldName.text)
                            set(it.post, fieldPost.text)
                            set(it.address, areaAddress.text)
                            where { it.id eq result.id !! }
                        }
                        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                        Data.run {
                            updateDB()
                            reloadTable(tableName)
                            dbUserController.formStage.close()
                        }
                    }
                }
            } else {
                Data.showMessage("Warning", Data.textDict("DB.IsEmptyFields"))
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbUserController.formStage.close()
    }
}
package controllers.user

import Data
import db.User
import db.UserTable
import db.Users
import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import utils.BaseController

class DBUserController(table : TableView2<UserTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<UserTable>(table, buttonEdit, buttonDelete) {

    lateinit var formEditController : DBUserFormEditController
    lateinit var formDeleteController : DBUserFormDeleteController

    init {
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.dbUser
            .map { row -> User(row[Users.id], row[Users.name], row[Users.post], row[Users.address]) }
            .forEach { table.items.add(UserTable(it.id, it.name, it.post, it.address)) }
    }

    override fun onButtonClickAdd() {
        showModal("/db/user/Add.fxml", "Создание записи составитель") {}
    }

    override fun onButtonClickEdit() {
        showModal("/db/user/Edit.fxml", "Редактирование записи составитель") {
            val result = Data.dbUser
                .where { (Users.id eq selectId) }
                .map { row -> User(row[Users.id], row[Users.name], row[Users.post], row[Users.address]) }
                .firstOrNull()
            if (result != null) {
                formEditController.fieldName.text = result.name
                formEditController.fieldPost.text = result.post
                formEditController.areaAddress.text = result.address
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/db/user/Delete.fxml", "Удаление записи составитель") {
            val result = Data.dbUser
                .where { (Users.id eq selectId) }
                .map { row -> User(row[Users.id], row[Users.name], row[Users.post], row[Users.address]) }
                .firstOrNull()
            if (result != null) {
                formDeleteController.fieldName.text = result.name
                formDeleteController.fieldPost.text = result.post
                formDeleteController.areaAddress.text = result.address
            }
        }
    }
}
package ru.fku.slacker.controllers.user

import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.where
import ru.fku.slacker.Data
import ru.fku.slacker.db.User
import ru.fku.slacker.db.UserTable
import ru.fku.slacker.db.Users
import ru.fku.slacker.utils.BaseController

class DBUserController(table : TableView2<UserTable>, buttonEdit : Button, buttonDelete : Button) :
    BaseController<UserTable>(table, buttonEdit, buttonDelete) {

    lateinit var formEditController : DBUserFormEditController
    lateinit var formDeleteController : DBUserFormDeleteController

    init {
        setupTableListener()
    }

    override fun reloadTable() {
        table.items.clear()
        Data.Companion.dbUser
            .map { row -> User(row[Users.id], row[Users.name], row[Users.post], row[Users.address]) }
            .forEach { table.items.add(UserTable(it.id, it.name, it.post, it.address)) }
    }

    override fun onButtonClickAdd() {
        showModal("/db/user/Add.fxml", "Создание записи составитель") {}
    }

    override fun onButtonClickEdit() {
        showModal("/db/user/Edit.fxml", "Редактирование записи составитель") {
            Data.Companion.updateDB()
            val result = Data.Companion.dbUser
                .where { (Users.id eq selectId) }
                .map { row -> User(row[Users.id], row[Users.name], row[Users.post], row[Users.address]) }
                .firstOrNull()
            result?.let {
                formEditController.run {
                    fieldName.text = it.name
                    fieldPost.text = it.post
                    areaAddress.text = it.address
                }
            }
        }
    }

    override fun onButtonClickDelete() {
        showModal("/db/user/Delete.fxml", "Удаление записи составитель") {
            Data.Companion.updateDB()
            val result = Data.Companion.dbUser
                .where { Users.id eq selectId }
                .map { row -> User(row[Users.id], row[Users.name], row[Users.post], row[Users.address]) }
                .firstOrNull()
            result?.let {
                formDeleteController.run {
                    fieldName.text = it.name
                    fieldPost.text = it.post
                    areaAddress.text = it.address
                }
            }
        }
    }
}
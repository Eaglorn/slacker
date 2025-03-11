package ru.fku.slacker.controllers.maker

import javafx.fxml.FXML
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
import ru.fku.slacker.db.Maker
import ru.fku.slacker.db.Makers
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBMakerFormAddController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private lateinit var fieldName : TextField

    @Suppress("unused")
    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                Data.Companion.updateDB()
                val result = Data.Companion.dbMaker
                    .where { (Makers.name eq fieldName.text) }
                    .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                    .firstOrNull()
                if (result == null) {
                    val database = SqliteDatabase.connect(Data.Companion.config.pathDB)
                    database.insert(Makers) {
                        set(it.name, fieldName.text)
                    }
                    FileUtils.copyFile(File(Data.Companion.config.pathDB), File(Config.Companion.pathDBLocal))
                    Data.Companion.run {
                        updateDB()
                        dbModelController.reloadTable()
                        dbMakerController.run {
                            reloadTable()
                            formStage.close()
                        }
                    }
                } else {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись с введённым наименованием уже существует.")
                        .showWarning()
                }
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.Companion.dbMakerController.formStage.close()
    }
}
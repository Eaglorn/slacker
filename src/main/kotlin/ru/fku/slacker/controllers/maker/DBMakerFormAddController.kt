package ru.fku.slacker.controllers.maker

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
        val name = fieldName.text
        if (name.isNotEmpty()) {
            Data.updateDB()
            val result = Data.dbMaker
                .where { Makers.name eq name }
                .map { row -> Maker(row[Makers.id], row[Makers.name]) }
                .firstOrNull()
            if (result == null) {
                val database = SqliteDatabase.connect(Data.config.pathDB)
                database.insert(Makers) {
                    set(it.name, name)
                }
                FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                Data.run {
                    updateDB()
                    reloadTable("Maker", "Model")
                    dbMakerController.formStage.close()
                }
            } else {
                Data.showMessage("Warning", "Запись с введённым наименованием уже существует.")
            }
        } else {
            Data.showMessage("Warning", "У записи присутсвуют незаполненные поля.")
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbMakerController.formStage.close()
    }
}
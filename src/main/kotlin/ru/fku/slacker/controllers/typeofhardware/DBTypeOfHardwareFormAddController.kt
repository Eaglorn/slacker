package ru.fku.slacker.controllers.typeofhardware

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
import ru.fku.slacker.db.TypeOfHardware
import ru.fku.slacker.db.TypeOfHardwares
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBTypeOfHardwareFormAddController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private lateinit var fieldName : TextField

    @Suppress("unused")
    @FXML
    private fun onButtonClickAdd() {
        runBlocking {
            launch {
                Data.updateDB()
                val result = Data.dbTypeOfHardware
                    .where { (TypeOfHardwares.name eq fieldName.text) }
                    .map { row -> TypeOfHardware(row[TypeOfHardwares.id], row[TypeOfHardwares.name]) }
                    .firstOrNull()
                if (result == null) {
                    val database = SqliteDatabase.connect(Data.config.pathDB)
                    database.insert(TypeOfHardwares) {
                        set(it.name, fieldName.text)
                    }
                    FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                    Data.run {
                        updateDB()
                        reloadTable("TypeOfHardware", "Model")
                        dbTypeOfHardwareController.formStage.close()
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
        Data.dbTypeOfHardwareController.formStage.close()
    }
}
package ru.fku.slacker.controllers.defect

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FileUtils
import org.controlsfx.control.Notifications
import org.controlsfx.control.SearchableComboBox
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.update
import org.ktorm.dsl.where
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import ru.fku.slacker.db.Defect
import ru.fku.slacker.db.Defects
import ru.fku.slacker.utils.SqliteDatabase
import java.io.File

class DBDefectFormEditController {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    lateinit var boxHardware : SearchableComboBox<String>

    @FXML
    lateinit var areaResultView : TextArea

    @FXML
    lateinit var areaDetect : TextArea

    @FXML
    lateinit var areaReason : TextArea

    init {
        Data.dbDefectController.formEditController = this
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickEdit() {
        if (Data.dbDefectController.selectId < 0) {
            Notifications.create()
                .title("Предупреждение!")
                .text("Отсутсвует выбор записи в таблице.")
                .showWarning()
        }
        runBlocking {
            launch {
                Data.updateDB()
                val result = Data.dbDefect
                    .where { (Defects.id eq Data.dbDefectController.selectId) }
                    .map { row ->
                        Defect(
                            row[Defects.id],
                            row[Defects.hardware],
                            row[Defects.result_view],
                            row[Defects.detect],
                            row[Defects.reason]
                        )
                    }
                    .firstOrNull()
                if (result == null) {
                    Notifications.create()
                        .title("Предупреждение!")
                        .text("Запись с выбранным id в базе отсуствует.")
                        .showWarning()
                } else {
                    if (result.hardware.equals(boxHardware.selectionModel.selectedItem) && result.equals(areaResultView.text) && result.equals(
                            areaDetect.text
                        ) && result.equals(areaReason.text)
                    ) {
                        Notifications.create()
                            .title("Предупреждение!")
                            .text("Запись модель с введёнными значениями уже существует.")
                            .showWarning()
                    } else {
                        val database = SqliteDatabase.connect(Data.config.pathDB)
                        database.update(Defects) {
                            set(it.hardware, boxHardware.selectionModel.selectedItem)
                            set(it.result_view, areaResultView.text)
                            set(it.detect, areaDetect.text)
                            set(it.reason, areaReason.text)
                            where { it.id eq result.id !! }
                        }
                        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
                        Data.run {
                            updateDB()
                            reloadTable("Defect")
                            dbDefectController.formStage.close()
                        }
                    }
                }
            }
        }
    }

    @Suppress("unused")
    @FXML
    private fun onButtonClickCancel() {
        Data.dbDefectController.formStage.close()
    }
}